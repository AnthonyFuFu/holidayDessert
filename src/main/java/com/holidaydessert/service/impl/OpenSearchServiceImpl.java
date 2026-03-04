package com.holidaydessert.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.common.unit.TimeValue;
import org.opensearch.index.query.BoolQueryBuilder;
import org.opensearch.index.query.Operator;
import org.opensearch.index.query.QueryBuilder;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.index.query.RangeQueryBuilder;
import org.opensearch.search.SearchHit;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.opensearch.search.sort.FieldSortBuilder;
import org.opensearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.model.SearchConditionDto;
import com.holidaydessert.repository.MemberRepository;
import com.holidaydessert.service.OpenSearchService;

import com.holidaydessert.model.Member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.holidaydessert.constant.MemberSearchCondition.*;

@Service
public class OpenSearchServiceImpl implements OpenSearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private MemberRepository memberRepository;

    @Value("${opensearch.index_member}")
    private String INDEX;

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(OpenSearchServiceImpl.class);

    @Override
    public ApiReturnObject OpenSearch(List<SearchConditionDto> conditionList) throws Exception {
        try {
            // 用於operator為 =
            Map<String, String> eqCondition = new HashMap<>();
            // 用於operator為 !=
            Map<String, String> neqCondition = new HashMap<>();
            // 用於input 欄位的模糊查詢
            Map<String, String> likeCondition = new HashMap<>();
            // 用於input 欄位的模糊查詢
            Map<String, String> nlikeCondition = new HashMap<>();

            for (SearchConditionDto dto : conditionList) {
                String columnName = dto.getCondition();
                String operator = dto.getOperator();
                
                String value = "";
                if (StringUtils.isEmpty(dto.getConditionValue())) {
                    List<String> conditionValues = dto.getConditionValues();
                    if (conditionValues == null || conditionValues.isEmpty()) {
                        throw new IllegalArgumentException("ConditionValues cannot be empty when ConditionValue is empty.");
                    }
                    if (DATE_COLUMN_LIST.contains(columnName) && conditionValues.size() >= 2) {
                        value = conditionValues.get(0) + "~" + conditionValues.get(1);
                    } else if (MULTIPLE_COLUMN_LIST.contains(columnName)) {
                        value = String.join("-", conditionValues);
                    } else {
                        value = conditionValues.get(0);
                    }
                } else {
                    value = dto.getConditionValue();
                }

                if (EQUAL.equalsIgnoreCase(operator)) {
                    if (FUZZY_COLUMN_LIST.contains(columnName)) {
                        if (likeCondition.containsKey(columnName)) {
                            // 相同的key，則查詢時使用分詞查詢方式(查詢條件含有-號)
                            likeCondition.put(columnName, likeCondition.get(columnName) + "-" + value);
                        } else {
                            likeCondition.put(columnName, value);
                        }
                    } else {
                        if (eqCondition.containsKey(columnName)) {
                            // 相同的key，則查詢時使用分詞查詢方式(查詢條件含有-號)
                            eqCondition.put(columnName, eqCondition.get(columnName) + "-" + value);
                        } else {
                            eqCondition.put(columnName, value);
                        }
                    }
                }

                if (NOT_EQUAL.equalsIgnoreCase(operator)) {
                    if (FUZZY_COLUMN_LIST.contains(columnName)) {
                        if (nlikeCondition.containsKey(columnName)) {
                            // 相同的key，則查詢時使用分詞查詢方式(查詢條件含有-號)
                            nlikeCondition.put(columnName, nlikeCondition.get(columnName) + "-" + value);
                        } else {
                            nlikeCondition.put(columnName, value);
                        }
                    } else {
                        if (neqCondition.containsKey(columnName)) {
                            // 相同的key，則查詢時使用分詞查詢方式(查詢條件含有-號)
                            neqCondition.put(columnName, neqCondition.get(columnName) + "-" + value);
                        } else {
                            neqCondition.put(columnName, value);
                        }
                    }
                }
            }

            List<Map<String, Object>> resultList = new ArrayList<>();
            if (eqCondition.size() > 0 || neqCondition.size() > 0 || likeCondition.size() > 0 || nlikeCondition.size() > 0) {
                SearchRequest searchRequest = createSearchRequest(INDEX, eqCondition, neqCondition, likeCondition, nlikeCondition);
                SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

                logger.info("hits size = " + searchResponse.getHits().getHits().length);

                for (SearchHit searchHit : searchResponse.getHits()) {
                    Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                    resultList.add(sourceAsMap);
                }
            }

            List<Member> memberList = convertToMemberList(resultList);
            
            // 返回API結果
            return ApiReturnObject.builder()
                    .code(200)
                    .message("Success")
                    .result(memberList)
                    .build();

        } catch (Exception e) {
            logger.error("OpenSearch error: ", e);
            return ApiReturnObject.builder()
                    .code(500)
                    .message("Error: " + e.getMessage())
                    .result(null)
                    .build();
        }
    }

    private SearchRequest createSearchRequest(String index, Map<String, String> eqCondition, Map<String, String> neqCondition, Map<String, String> likeCondition, Map<String, String> nlikeCondition) {

        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.sort(new FieldSortBuilder(MEM_ID).order(SortOrder.ASC)); // 預設按會員ID排序
        sourceBuilder.timeout(new TimeValue(60000));

        // 用於條件拼接，must等於sql語句的=，mustNot為!=，should為like
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        String strCondition;
        List<String> conditionList = new ArrayList<>();

        // 處理相等條件
        if (!eqCondition.isEmpty()) {
            for (String key : eqCondition.keySet()) {
                if (DATE_COLUMN_LIST.contains(key)) {
                    // 日期區間查詢
                    String start = eqCondition.get(key) != null ? eqCondition.get(key).substring(0, eqCondition.get(key).indexOf('~')) : "";
                    String end = eqCondition.get(key) != null ? eqCondition.get(key).replace(start + "~", "") : "";
                    RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(key).gte(start).lte(end);
                    boolQueryBuilder.must(rangeQueryBuilder);
                } else {
                    strCondition = eqCondition.get(key);
                    if (!strCondition.contains("-")) {
                        if (NUM_COLUMN_LIST.contains(key)) {
                            QueryBuilder matchQueryBuilder = QueryBuilders.termQuery(key, Double.parseDouble(strCondition));
                            boolQueryBuilder.must(matchQueryBuilder); // 全字匹配
                        } else if (BOOL_COLUMN_LIST.contains(key)) {
                            QueryBuilder matchQueryBuilder = QueryBuilders.termQuery(key, strCondition);
                            boolQueryBuilder.must(matchQueryBuilder); // 全字匹配
                        } else {
                            QueryBuilder matchQueryBuilder = QueryBuilders.termQuery(key + ".keyword", strCondition);
                            boolQueryBuilder.must(matchQueryBuilder); // 全字匹配
                        }
                    } else {
                        if (BOOL_COLUMN_LIST.contains(key)) {
                            String[] split = strCondition.split("-");
                            for (String s : split) {
                                boolQueryBuilder.must(QueryBuilders.matchQuery(key, s).operator(Operator.AND));
                            }
                        } else if (EXACT_MATCH_COLUMN_LIST.contains(key)) {
                            String[] valueList = strCondition.split("-");
                            QueryBuilder matchQueryBuilder = QueryBuilders.termsQuery(key + ".keyword", valueList);
                            boolQueryBuilder.must(matchQueryBuilder); // 全字匹配
                        } else {
                            QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(key, QueryParserBase.escape(strCondition)).operator(Operator.OR);
                            boolQueryBuilder.must(matchQueryBuilder); // 分詞查詢
                        }
                    }
                }
            }
        }

        // 處理不相等條件
        if (!neqCondition.isEmpty()) {
            for (String key : neqCondition.keySet()) {
                if (DATE_COLUMN_LIST.contains(key)) {
                    // 日期區間查詢
                    String start = neqCondition.get(key) != null ? neqCondition.get(key).substring(0, neqCondition.get(key).indexOf('~')) : "";
                    String end = neqCondition.get(key) != null ? neqCondition.get(key).replace(start + "~", "") : "";
                    RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(key).gte(start).lte(end);
                    boolQueryBuilder.mustNot(rangeQueryBuilder);
                } else {
                    strCondition = neqCondition.get(key);
                    if (!strCondition.contains("-")) {
                        if (NUM_COLUMN_LIST.contains(key)) {
                            QueryBuilder matchQueryBuilder = QueryBuilders.termQuery(key, Double.parseDouble(strCondition));
                            boolQueryBuilder.mustNot(matchQueryBuilder); // 全字匹配
                        } else if (BOOL_COLUMN_LIST.contains(key)) {
                            QueryBuilder matchQueryBuilder = QueryBuilders.termQuery(key, strCondition);
                            boolQueryBuilder.mustNot(matchQueryBuilder); // 全字匹配
                        } else {
                            QueryBuilder matchQueryBuilder = QueryBuilders.termQuery(key + ".keyword", strCondition);
                            boolQueryBuilder.mustNot(matchQueryBuilder); // 全字匹配
                        }
                    } else {
                        if (BOOL_COLUMN_LIST.contains(key)) {
                            String[] split = strCondition.split("-");
                            for (String s : split) {
                                boolQueryBuilder.mustNot(QueryBuilders.matchQuery(key, s));
                            }
                        } else if (EXACT_MATCH_COLUMN_LIST.contains(key)) {
                            String[] valueList = strCondition.split("-");
                            QueryBuilder matchQueryBuilder = QueryBuilders.termsQuery(key + ".keyword", valueList);
                            boolQueryBuilder.mustNot(matchQueryBuilder); // 全字匹配
                        } else {
                            QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(key, QueryParserBase.escape(strCondition)).operator(Operator.OR);
                            boolQueryBuilder.mustNot(matchQueryBuilder); // 分詞查詢
                        }
                    }
                }
            }
        }

        // 處理模糊查詢條件
		if (!likeCondition.isEmpty()) {
			for (String key : likeCondition.keySet()) {
				if (MEM_NAME.equals(key) && !hasDigit(likeCondition.get(key))) {
					// 特殊處理邏輯
					List <Member> memberList = memberRepository.findByMemName(likeCondition.get(key).trim().toUpperCase().replace("*", ""));
                    for (Member member : memberList) {
                        strCondition = member != null ? member.getMemName() : likeCondition.get(key);
                        conditionList.add(strCondition);
                    }
                    QueryBuilder matchQueryBuilder = QueryBuilders.termsQuery(key, conditionList + "*");
                    boolQueryBuilder.must(matchQueryBuilder);
				} else {
					strCondition = likeCondition.get(key);
					if (!strCondition.contains("-")) {
						// 處理不含分隔符的情況
						if (WILDCARD_COLUMN_LIST.contains(key)) {
							QueryBuilder matchQueryBuilder = QueryBuilders.wildcardQuery(key + ".keyword", "*" + strCondition + "*");
							boolQueryBuilder.must(matchQueryBuilder);
						} else {
							QueryBuilder matchQueryBuilder = QueryBuilders.wildcardQuery(key + ".keyword", "*" + strCondition + "*");
							boolQueryBuilder.must(matchQueryBuilder);
						}
					} else if (WILDCARD_COLUMN_LIST.contains(key)) {
						// 處理含分隔符的通配符搜索
						QueryBuilder matchQueryBuilder = QueryBuilders.wildcardQuery(key + ".keyword", "*" + strCondition + "*");
						boolQueryBuilder.must(matchQueryBuilder);
					} else if (FUZZY_COLUMN_LIST.contains(key)) {
						// 處理含分隔符的模糊查詢，使用 OR 組合多個條件
						String[] terms = strCondition.split("-");
						BoolQueryBuilder subOrQueryBuilder = QueryBuilders.boolQuery();
						for (String item : terms) {
							subOrQueryBuilder.should(QueryBuilders.wildcardQuery(key + ".keyword", "*" + item + "*"));
						}
						boolQueryBuilder.must(subOrQueryBuilder);
					} else {
						// 分詞查詢
						QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(key, QueryParserBase.escape(strCondition)).operator(Operator.OR);
						boolQueryBuilder.must(matchQueryBuilder);
					}
				}
			}
        }

        // 處理不包含模糊查詢條件
        if (!nlikeCondition.isEmpty()) {
			for (String key : nlikeCondition.keySet()) {
				if (MEM_NAME.equals(key) && !hasDigit(nlikeCondition.get(key))) {
                    List<Member> memberList = memberRepository.findByMemName(nlikeCondition.get(key).trim().toUpperCase().replace("*", ""));
                    for (Member member : memberList) {
                        strCondition = member != null ? member.getMemName() : nlikeCondition.get(key);
                        conditionList.add(strCondition);
                    }
                    QueryBuilder matchQueryBuilder = QueryBuilders.termsQuery(key, conditionList + "*");
                    boolQueryBuilder.mustNot(matchQueryBuilder);
				} else {
					strCondition = nlikeCondition.get(key);
					if (!strCondition.contains("-")) {
						// 處理不含分隔符的情況
						if (WILDCARD_COLUMN_LIST.contains(key)) {
							QueryBuilder matchQueryBuilder = QueryBuilders.wildcardQuery(key + ".keyword", "*" + strCondition + "*");
							boolQueryBuilder.mustNot(matchQueryBuilder);
						} else {
							QueryBuilder matchQueryBuilder = QueryBuilders.wildcardQuery(key + ".keyword", "*" + strCondition + "*");
							boolQueryBuilder.mustNot(matchQueryBuilder);
						}
					} else if (WILDCARD_COLUMN_LIST.contains(key)) {
						// 處理含分隔符的通配符搜索
						QueryBuilder matchQueryBuilder = QueryBuilders.wildcardQuery(key, "*" + strCondition.replace("-", "?") + "*");
						boolQueryBuilder.mustNot(matchQueryBuilder);
					} else if (FUZZY_COLUMN_LIST.contains(key)) {
						// 處理含分隔符的模糊查詢，使用 OR 組合多個條件
						String[] terms = strCondition.split("-");
						BoolQueryBuilder subOrQueryBuilder = QueryBuilders.boolQuery();
						for (String item : terms) {
							subOrQueryBuilder.should(QueryBuilders.wildcardQuery(key + ".keyword", "*" + item + "*"));
						}
						boolQueryBuilder.mustNot(subOrQueryBuilder);
					} else {
						// 分詞查詢
						QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(key, QueryParserBase.escape(strCondition)).operator(Operator.OR);
						boolQueryBuilder.mustNot(matchQueryBuilder);
					}
				}
			}
        }

        sourceBuilder.query(boolQueryBuilder);
        sourceBuilder.from(0);  // 分頁起始索引
        sourceBuilder.size(1000); // 每頁大小
        searchRequest.source(sourceBuilder);

        return searchRequest;
    }

    private boolean hasDigit(String content) {
        if (content == null || content.isEmpty()) {
            return false;
        }
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(content);
        return m.matches();
    }

    private List<Member> convertToMemberList(List<Map<String, Object>> resultList) {
        List<Member> memberList = new ArrayList<>();
        
        for (Map<String, Object> map : resultList) {
            Member member = new Member();
            
            // 根據 Member 類的屬性設置值
            member.setMemId((String) map.get(MEM_ID));
            member.setMemName((String) map.get(MEM_NAME));
            member.setMemAccount((String) map.get(MEM_ACCOUNT));
            member.setMemPassword((String) map.get(MEM_PASSWORD));
            member.setMemGender((String) map.get(MEM_GENDER));
            member.setMemPhone((String) map.get(MEM_PHONE));
            member.setMemEmail((String) map.get(MEM_EMAIL));
            member.setMemAddress((String) map.get(MEM_ADDRESS));
            member.setMemBirthday((String) map.get(MEM_BIRTHDAY));
            member.setMemPicture((String) map.get(MEM_PICTURE));
            member.setMemImage((String) map.get(MEM_IMAGE));
            member.setMemStatus((String) map.get(MEM_STATUS));
            member.setMemVerificationStatus((String) map.get(MEM_VERIFICATION_STATUS));
            member.setMemVerificationCode((String) map.get(MEM_VERIFICATION_CODE));
            member.setMemGoogleUid((String) map.get(MEM_GOOGLE_UID));
            
            // 添加到列表
            memberList.add(member);
        }
        
        return memberList;
    }
}