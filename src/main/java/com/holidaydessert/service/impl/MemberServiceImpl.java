package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.MemberDao;
import com.holidaydessert.model.Member;
import com.holidaydessert.service.MemberService;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberDao memberDao;

	@Override
	public List<Map<String, Object>> list(Member member) {
		return memberDao.list(member);
	}

	@Override
	public int getCount(Member member) {
		return memberDao.getCount(member);
	}

	@Override
	public List<Map<String, Object>> issueCouponList(Member member) {
		return memberDao.issueCouponList(member);
	}

	@Override
	public int getIssueCouponCount(Member member) {
		return memberDao.getIssueCouponCount(member);
	}
	
	@Override
	public void register(Member member) {
		memberDao.register(member);
	}

	@Override
	public void edit(Member member) {
		memberDao.edit(member);
	}

	@Override
	public void verificationEmail(Member member) {
		memberDao.verificationEmail(member);
	}

	@Override
	public Member getCheckMemberEmail(Member member) {
		return memberDao.getCheckMemberEmail(member);
	}

	@Override
	public void updateVerification(Member member) {
		memberDao.updateVerification(member);
	}

	@Override
	public void updatePassword(Member member) {
		memberDao.updatePassword(member);
	}

	@Override
	public Member login(Member member) {
		return memberDao.login(member);
	}
	
	@Override
	public Optional<Member> getDataByGoogleUid(String googleUid) {
		return Optional.ofNullable(memberDao.getDataByGoogleUid(googleUid));
	}
	
}
