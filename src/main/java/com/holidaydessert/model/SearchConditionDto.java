package com.holidaydessert.model;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SearchConditionDto {
    String condition;
    String conditionValue;
    String operator;
    List<String> conditionValues;
}
