package com.holidayDessert.dao;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.Member;

public interface MemberDao {

	public List<Map<String, Object>> list(Member member);

}
