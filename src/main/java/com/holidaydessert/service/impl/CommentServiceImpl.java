package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.CommentDao;
import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {
	
	@Autowired
	private CommentDao commentDao;
	
	@Override
	public ApiReturnObject getCommentList() {
		
		List<Map<String, Object>> commentList = commentDao.getCommentList();
		
		if(commentList == null) {
			return new ApiReturnObject(200, "查無留言清單", null);
		}
		
		return new ApiReturnObject(200, "取得留言清單成功", commentList);
	}

}
