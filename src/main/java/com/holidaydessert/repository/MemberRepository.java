package com.holidaydessert.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.holidaydessert.model.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
	
	List<Member> findByMemName(String name);

	List<Member> findByMemEmail(String email);

	List<Member> findByMemNameAndMemEmail(String name, String email);
}
