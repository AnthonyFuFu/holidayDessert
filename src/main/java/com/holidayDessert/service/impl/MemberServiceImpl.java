package com.holidayDessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidayDessert.dao.MemberDao;
import com.holidayDessert.model.Member;
import com.holidayDessert.service.MemberService;

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
	public void update(Member member) {
		memberDao.update(member);
	}

	@Override
	public void delete(Member member) {
		memberDao.delete(member);
	}

	@Override
	public void register(Member member) {
		memberDao.register(member);
	}

	@Override
	public void edit(Member member) {
		memberDao.edit(member);
	}

}
