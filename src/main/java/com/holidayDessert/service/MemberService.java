package com.holidayDessert.service;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.Member;

public interface MemberService {
	
	// back
	public List<Map<String, Object>> list(Member member);
	public int getCount(Member member);
	
	// front
	public void register(Member member);
	public void edit(Member member);
	
}
