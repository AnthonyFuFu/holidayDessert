package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.holidaydessert.dao.MemberDao;
import com.holidaydessert.model.Member;
import com.holidaydessert.repository.MemberRepository;
import com.holidaydessert.service.MemberService;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberDao memberDao;
	
    @Autowired
    private MemberRepository memberRepository;
    
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
	
	// =============================================
	// register
	// =============================================
	@Override
	@Transactional
	public void register(Member member) {
	    memberRepository.save(member);
	}
	
	// =============================================
	// edit
	// =============================================
	@Override
	@Transactional
	public void edit(Member member) {
	    memberRepository.edit(
	        member.getMemId(),
	        member.getMemName(),
	        member.getMemAccount(),
	        member.getMemPassword(),
	        member.getMemGender(),
	        member.getMemPhone(),
	        member.getMemEmail(),
	        member.getMemAddress(),
	        member.getMemBirthday(),
	        member.getMemPicture(),
	        member.getMemImage()
	    );
	}
	
	// =============================================
	// verificationEmail
	// =============================================
	@Override
	@Transactional
	public void verificationEmail(Member member) {
	    memberRepository.verificationEmail(member.getMemId());
	}
	
    // =============================================
    // getCheckMemberEmail
    // =============================================
    @Override
    public Member getCheckMemberEmail(Member member) {
        return memberRepository.findByMemEmail(member.getMemEmail()).orElse(null);
    }

	// =============================================
	// updateVerification
	// =============================================
	@Override
	@Transactional
	public void updateVerification(Member member) {
	    memberRepository.updateVerification(
	        member.getMemId(),
	        member.getMemVerificationCode()
	    );
	}

	// =============================================
	// updatePassword
	// =============================================
	@Override
	@Transactional
	public void updatePassword(Member member) {
	    memberRepository.updatePassword(
	        member.getMemId(),
	        member.getMemPassword()
	    );
	}

	// =============================================
	// login
	// =============================================
	@Override
	public Member login(Member member) {
	    return memberRepository.findByMemEmailAndMemPassword(
	        member.getMemEmail(),
	        member.getMemPassword()
	    ).orElse(null);
	}

    // =============================================
    // getDataByGoogleUid
    // =============================================
    @Override
    public Optional<Member> getDataByGoogleUid(String googleUid) {
        return memberRepository.findByMemGoogleUid(googleUid);
    }
	
}
