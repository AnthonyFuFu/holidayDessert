package com.holidaydessert.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.holidaydessert.model.Member;

public interface MemberService {
	
	// back
	public List<Map<String, Object>> list(Member member);
	public int getCount(Member member);
	public List<Map<String, Object>> issueCouponList(Member member);
	public int getIssueCouponCount(Member member);
	
	// front
	public void register(Member member);
	public void edit(Member member);
	public void verificationEmail(Member member);
	public Member getCheckMemberEmail(Member member);
	public void updateVerification(Member member);
	public void updatePassword(Member member);
	public Member login(Member member);
	public Optional<Member> getDataByGoogleUid(String googleUid);
	
}
