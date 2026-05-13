package com.holidaydessert.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {

    @Query(value = "SELECT c.CMT_PICTURE, c.CMT_CONTENT, c.CMT_CREATE_BY, " +
                   "       DATE_FORMAT(c.CMT_CREATE_TIME, '%Y/%m/%d') CMT_CREATE_TIME, " +
                   "       m.MEM_PICTURE, m.MEM_IMAGE " +
                   "FROM holiday_dessert.comment c " +
                   "LEFT JOIN holiday_dessert.member m ON c.MEM_ID = m.MEM_ID " +
                   "WHERE MEM_STATUS = 1 " +
                   "AND MEM_VERIFICATION_STATUS = 1 " +
                   "AND CMT_STATUS = 1 " +
                   "AND CMT_CHECK = 1 " +
                   "ORDER BY RAND() " +
                   "LIMIT 10",
           nativeQuery = true)
    List<Map<String, Object>> getCommentList();

}