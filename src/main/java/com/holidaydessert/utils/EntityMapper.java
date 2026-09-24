package com.holidaydessert.utils;

import org.apache.commons.collections4.CollectionUtils;

import io.micrometer.common.util.StringUtils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * EntityMapper
 *
 * @author Sun Lu
 * @date 2025/6/5
 */
public class EntityMapper {

    public static <T> List<T> mapToEntity(Class<T> clazz, List<Map<String, String>> rows) throws Exception {
        List<T> entities = new ArrayList<>();

        for (Map<String, String> row : rows) {
            T entity = clazz.getDeclaredConstructor().newInstance(); // 新建实体

            // 反射逻辑
            for (String column : row.keySet()) {
                try {
                    Field field = clazz.getDeclaredField(column); // 列名 mapping 字段
                    field.setAccessible(true);

                    // 将数据转换成field对应的类型
                    Object value = convertValue(row.get(column), field.getType());
                    field.set(entity, value);

                } catch (Exception e) {
                    // 如果实体中没有对应的字段，就产生一笔log
                    System.out.println("No matching field for column: " + column);
                }
            }

            entities.add(entity);
        }

        return entities;
    }

    public static <T> List<T> entitiesToDto(Class<T> targetClazz, List<? extends Object> sourceEntities) throws Exception {
        List<T> dtos = new ArrayList<>();
        if (CollectionUtils.isEmpty(sourceEntities)) {
            throw new IllegalArgumentException("SourceEntities  cannot be null");
        }

        for (Object entity : sourceEntities) {
            if (entity == null) {
                throw new IllegalArgumentException("Individual source entity cannot be null");
            }
            T dto = entityToDto(targetClazz, entity);
            dtos.add(dto);
        }

        return dtos;
    }

    public static <T> T entityToDto(Class<T> targetClazz, Object sourceEntity) throws Exception {
        T dto = targetClazz.getDeclaredConstructor().newInstance(); // 新建实体
        Class<? extends Object> clazz = sourceEntity.getClass();
        Field[] ids = clazz.getDeclaredFields();
        for (Field id : ids) {
            // 反射逻辑
            try {
                String fieldName = id.getName();  // 字段名
                Field targetField = targetClazz.getDeclaredField(fieldName); // 列名 mapping 字段
                targetField.setAccessible(true);
                id.setAccessible(true);


                // 将数据转换成field对应的类型
                if (targetField != null && (id.getType().equals(targetField.getType()) || isCompatible(id.getType(), targetField.getType()))) {
                    // 将源对象字段的值赋给目标对象字段
                    Object value = id.get(sourceEntity);
                    targetField.set(dto, value);
                } else {
                    if (id.get(sourceEntity) != null) {
                        mapAssociatedEntityFields(id, id.get(sourceEntity), targetClazz, dto);
                        entityToDto(targetClazz, id.get(sourceEntity));
                    }
                }

            } catch (Exception e) {
                // 如果实体中没有对应的字段，就产生一笔log
                //                System.out.println("No matching field for column: " + id.getName());
            }
        }

        return dto;
    }

    private static <T> void mapAssociatedEntityFields(Field sourceField, Object associatedEntity, Class<T> targetClazz, T dto) throws Exception {
        Field[] associatedFields = associatedEntity.getClass().getDeclaredFields();
        for (Field associatedField : associatedFields) {
            associatedField.setAccessible(true);
            String associatedFieldName = associatedField.getName();

            try {
                // 查找 DTO 中的字段
                Field dtoField = targetClazz.getDeclaredField(associatedFieldName);
                dtoField.setAccessible(true);
                Object value = associatedField.get(associatedEntity);
                if (dtoField.get(targetClazz) == null) {
                    dtoField.set(dto, value);
                }
            } catch (NoSuchFieldException e) {
                // 如果 DTO 中没有匹配字段，忽略
                System.out.println("No matching DTO field for associated field: " + associatedFieldName);
            }
        }
    }

    /**
     * getEntityCombinationDataByKeyColumn
     *
     * @param dto
     * @param keyColumns
     * @return java.lang.String
     * @author Sun Lu
     * @date 2025/06/17
     */
    public static String getEntityCombinationDataByKeyColumn(Object dto, List<String> keyColumns) {
        List<String> values = new ArrayList<>();
        try {
            if (dto == null) {
                throw new IllegalArgumentException("DTO cannot be null when getEntityCombinationData");
            }

            if (CollectionUtils.isEmpty(keyColumns)) {
                throw new IllegalArgumentException("KeyColumns and keyColumns cannot be null when getEntityCombinationData");
            }
            Class<? extends Object> clazz = dto.getClass();
            for (String column : keyColumns) {
                if (StringUtils.isBlank(column)) {
                    values.add("");
                    continue;
                }
                // 利用反射获取指定字段的值
                Field field = clazz.getDeclaredField(column);
                field.setAccessible(true);
                Object value = field.get(dto);
                values.add(value == null ? "null" : value.toString());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error accessing field: " + e.getMessage());
        }

        return String.join("|", values);
    }

    public static String getEntitySingleColumnDataByKeyColumn(Object dto, String keyColumn) {

        if (dto == null) {
            throw new IllegalArgumentException("DTO cannot be null when getEntityCombinationData");
        }

        if (StringUtils.isBlank(keyColumn)) {
            return "";
        }

        try {
            Class<? extends Object> clazz = dto.getClass();

            // 利用反射获取指定字段的值
            Field field = clazz.getDeclaredField(keyColumn);
            field.setAccessible(true);
            Object value = field.get(dto);
            return value != null ? value.toString() : "";

        } catch (Exception e) {
            throw new RuntimeException("Error accessing field: " + e.getMessage());
        }

    }

    /**
     * 要使用此共用方法请确认 dto 中确实包含 columnInternalName 栏位
     *
     * @param dtos
     * @param keyColumns
     * @return java.util.List<java.lang.String>
     * @author Sun Lu
     * @date 2025/06/17
     */
    public static List<String> getKeyColumnListBySettingKeyColumn(List<? extends Object> dtos, List<String> keyColumns) {
        List<String> values = new ArrayList<>();
        try {
            if (CollectionUtils.isEmpty(dtos)) {
                throw new IllegalArgumentException("DTO and keyColumns cannot be null when getKeyColumnList");
            }
            if (CollectionUtils.isEmpty(keyColumns)) {
                throw new IllegalArgumentException("KeyColumns and keyColumns cannot be null when getKeyColumnList");
            }

            for (Object dto : dtos) {
                // 利用反射获取指定字段的值
                // 此处默认读取 column_internal_name 栏位,后续其他表单要使用此共用方法请确认 dto 中确实包含此栏位
                Class<? extends Object> clazz = dto.getClass();
                Field field = clazz.getDeclaredField("columnInternalName");
                field.setAccessible(true);
                Object value = field.get(dto);
                String valueStr = value == null ? "null" : value.toString();
                if (keyColumns.contains(valueStr)) {
                    values.add(valueStr);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error accessing field: " + e.getMessage());
        }
        return values;
    }

    // 类型转换,如果以后有其他的类型可持续增加
    private static Object convertValue(Object value, Class<?> targetType) {
        if (value == null) {
            return null;
        }

        if (targetType.isAssignableFrom(value.getClass())) {
            // 如果类型一致，则直接返回
            return value;
        } else if (targetType == String.class) {
            // 转换为 String
            return value.toString();
        } else if (targetType == Integer.class || targetType == int.class) {
            // 转换为 Integer
            return ((Number) value).intValue();
        } else if (targetType == Long.class || targetType == long.class) {
            // 转换为 Long
            return ((Number) value).longValue();
        } else if (targetType == Double.class || targetType == double.class) {
            // 转换为 Double
            return ((Number) value).doubleValue();
        } else if (targetType == Float.class || targetType == float.class) {
            // 转换为 Float
            return ((Number) value).floatValue();
        } else if (targetType == Boolean.class || targetType == boolean.class) {
            // 转换为 Boolean
            return Boolean.valueOf(value.toString());
        } else if (targetType == Date.class) {
            // 转换为 java.util.Date
            if (value instanceof Timestamp) {
                return new Date(((Timestamp) value).getTime());
            } else if (value instanceof java.sql.Date) {
                return new Date(((java.sql.Date) value).getTime());
            }
        } else if (targetType == LocalDate.class) {
            // 转换为 LocalDate
            if (value instanceof Timestamp) {
                return ((Timestamp) value).toLocalDateTime().toLocalDate();
            } else if (value instanceof java.sql.Date) {
                return ((java.sql.Date) value).toLocalDate();
            }
        } else if (targetType == LocalDateTime.class) {
            // 转换为 LocalDateTime
            if (value instanceof Timestamp) {
                return ((Timestamp) value).toLocalDateTime();
            }
        }

        // 如果没有找到匹配的类型，则直接返回原始值
        return value;
    }

    private static boolean isCompatible(Class<?> sourceType, Class<?> targetType) {
        if ((sourceType == int.class && targetType == Integer.class) || (sourceType == Integer.class && targetType == int.class)) {
            return true;
        }
        // 补充其他基本类型的兼容性
        return false;
    }

    public static <T> List<T> mapResultSetToObject(ResultSet resultSet, Class<T> clazz) throws Exception {
        List<T> resultList = new ArrayList<>();
        // try {
        while (resultSet.next()) {
            // 创建目标类的实例
            T instance = clazz.getDeclaredConstructor().newInstance();

            // 遍历目标类的所有字段
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                try {
                    field.setAccessible(true); // 设置字段可访问

                    // 获取列名（默认使用字段名）
                    String columnName = field.getName();

                    // 通过列名从 ResultSet 中获取数据
                    // Object value = resultSet.getObject(columnName.toUpperCase());
                    Object value = convertValue(resultSet.getObject(columnName.toUpperCase()), field.getType());
                    // 将值设置到目标字段
                    field.set(instance, value);
                } catch (Exception e) {
                    // 如果实体中没有对应的字段，就产生一笔log
                    //System.out.println("No matching field for column: " + field.getName());
                }
            }
            resultList.add(instance);
        }
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
        return resultList;
    }
}
