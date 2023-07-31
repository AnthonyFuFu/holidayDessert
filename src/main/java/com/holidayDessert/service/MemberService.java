package com.holidayDessert.service;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.Member;

public interface MemberService {

	public List<Map<String, Object>> list(Member member);

}
