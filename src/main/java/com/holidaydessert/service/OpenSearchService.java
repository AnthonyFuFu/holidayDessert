package com.holidaydessert.service;

import java.util.List;

import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.model.SearchConditionDto;

public interface OpenSearchService {

	public ApiReturnObject OpenSearch(List<SearchConditionDto> conditionList) throws Exception;

}
