package com.holidaydessert.dao;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.Member;

public interface MemberDao {

	// back
	public List<Map<String, Object>> list(Member member);
	public int getCount(Member member);
	
	// front
	public void register(Member member);
	public void edit(Member member);
	public void verificationEmail(Member member);
	public Member getCheckMemberEmail(Member member);
	public void updateVerification(Member member);
	public void updatePassword(Member member);
	public Member login(Member member);
	public Member getDataByGoogleUid(String googleUid);
	
}
