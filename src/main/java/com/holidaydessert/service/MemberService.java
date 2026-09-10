package com.holidaydessert.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.holidaydessert.dao.MemberDao;
import com.holidaydessert.model.Member;
import com.holidaydessert.repository.MemberRepository;

@Service
public class MemberService {

	@Autowired
	private MemberDao memberDao;
	
    @Autowired
    private MemberRepository memberRepository;
    
	// back
	public List<Map<String, Object>> list(Member member) {
		return memberDao.list(member);
	}

	public int getCount(Member member) {
		return memberDao.getCount(member);
	}

	public List<Map<String, Object>> issueCouponList(Member member) {
		return memberDao.issueCouponList(member);
	}

	public int getIssueCouponCount(Member member) {
		return memberDao.getIssueCouponCount(member);
	}
	
	// front
	@Transactional
	public void register(Member member) {
	    memberRepository.save(member);
	}
	
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

	@Transactional
	public void verificationEmail(Member member) {
	    memberRepository.verificationEmail(member.getMemId());
	}
	
    public Member getCheckMemberEmail(Member member) {
        return memberRepository.findByMemEmail(member.getMemEmail()).orElse(null);
    }
    
	@Transactional
	public void updateVerification(Member member) {
	    memberRepository.updateVerification(
	        member.getMemId(),
	        member.getMemVerificationCode()
	    );
	}
	
	@Transactional
	public void updatePassword(Member member) {
	    memberRepository.updatePassword(
	        member.getMemId(),
	        member.getMemPassword()
	    );
	}
	
	public Member login(Member member) {
	    return memberRepository.findByMemEmailAndMemPassword(
	        member.getMemEmail(),
	        member.getMemPassword()
	    ).orElse(null);
	}
	
    public Optional<Member> getDataByGoogleUid(String googleUid) {
        return memberRepository.findByMemGoogleUid(googleUid);
    }
	
}
