package com.holidaydessert.model;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class ApiReturnObject {
	private List<Map<String, Object>> result;
}
