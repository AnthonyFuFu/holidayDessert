package com.holidaydessert.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.holidaydessert.constant.SortFieldsConstant;
import com.holidaydessert.model.Member;
import com.holidaydessert.repository.MemberRepository;
import com.holidaydessert.utils.PageableUtil;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;
    
	// back
	public Map<String, Object> list(Map<String, String> dataTableParams) {
		// 1. 取得 DataTables 參數
		String keyword = PageableUtil.getSearchValue(dataTableParams);
		String draw = PageableUtil.getDraw(dataTableParams);
		// 2. 建立分頁與排序（start / length / order）
		Pageable pageable = PageableUtil.buildDataTablePageable(
				dataTableParams,
				SortFieldsConstant.MEMBER_TABLE,
				SortFieldsConstant.MEMBER_DEFAULT,
				Sort.Direction.ASC
		);
		// 3. 查詢資料
		Page<Map<String, Object>> memberPage = memberRepository.backList(keyword, pageable);
		// 4. 封裝成 DataTables 回傳格式
		return PageableUtil.buildDataTable(memberPage, PageableUtil.toMutableList(memberPage), draw);
	}

	public Map<String, Object> issueCouponList(Map<String, String> dataTableParams) {
		// 1. 取得 DataTables 參數
		String keyword = PageableUtil.getSearchValue(dataTableParams);
		String draw = PageableUtil.getDraw(dataTableParams);
		// 搜尋框輸入純數字時，視為「近一個月消費金額 >= 此數字」
		Long totalExpense = keyword.matches("\\d{1,18}") ? Long.valueOf(keyword) : null;
		// 2. 建立分頁與排序（start / length / order）
		Pageable pageable = PageableUtil.buildDataTablePageable(
				dataTableParams,
				SortFieldsConstant.ISSUE_COUPON_TABLE,
				SortFieldsConstant.MEMBER_DEFAULT,
				Sort.Direction.ASC
		);
		// 3. 查詢資料
		Page<Map<String, Object>> memberPage = memberRepository.issueCouponList(keyword, totalExpense, pageable);
		// 4. 封裝成 DataTables 回傳格式
		return PageableUtil.buildDataTable(memberPage, PageableUtil.toMutableList(memberPage), draw);
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
