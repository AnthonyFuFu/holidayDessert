package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.repository.CommentRepository;

@Service
public class CommentService {
	
	@Autowired
	private CommentRepository commentRepository;

	// front
	public ApiReturnObject getCommentList() {
		
		List<Map<String, Object>> commentList = commentRepository.getCommentList();
		
		if(commentList == null) {
			return ApiReturnObject.success("查無留言清單", null);
		}
		
		return ApiReturnObject.success("取得留言清單成功", commentList);
	}

}
