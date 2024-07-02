package com.holidaydessert.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.dao.CommentDao;

@Repository
public class CommentDaoImpl implements CommentDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> getCommentList() {
		
		String sql = " SELECT c.CMT_PICTURE,c.CMT_CONTENT,c.CMT_CREATE_BY, DATE_FORMAT(c.CMT_CREATE_TIME, '%Y/%m/%d') CMT_CREATE_TIME, m.MEM_PICTURE, m.MEM_IMAGE "
				   + " FROM holiday_dessert.comment c "
				   + " LEFT JOIN holiday_dessert.member m ON c.MEM_ID = m.MEM_ID "
				   + " WHERE MEM_STATUS = 1 "
				   + " AND MEM_VERIFICATION_STATUS = 1 "
				   + " AND CMT_STATUS = 1 "
				   + " AND CMT_CHECK = 1 "
				   + " ORDER BY RAND() "
				   + " LIMIT 10 ";
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

		if(list!=null && list.size()>0) {
			return list;
		} else {
			return null;
		}
		
	}

}
