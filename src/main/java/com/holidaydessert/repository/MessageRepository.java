package com.holidaydessert.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.holidaydessert.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {

    // =============================================
    // getMessageByMem
    // =============================================
    @Query(value = "SELECT msg.*, MEM_NAME, EMP_NAME " +
                   "FROM holiday_dessert.message msg " +
                   "LEFT JOIN member m ON m.MEM_ID = msg.MEM_ID " +
                   "LEFT JOIN employee e ON e.EMP_ID = msg.EMP_ID " +
                   "WHERE m.MEM_ID = :memId",
           nativeQuery = true)
    List<Map<String, Object>> getMessageByMem(@Param("memId") Integer memId);

    // =============================================
    // saveMessage → save() 內建，不需要額外方法
    // =============================================

    // =============================================
    // edit
    // =============================================
    @Modifying
    @Transactional
    @Query(value = "UPDATE holiday_dessert.message " +
                   "SET MSG_CONTENT = :msgContent, MSG_TIME = :msgTime " +
                   "WHERE MSG_ID = :msgId",
           nativeQuery = true)
    void edit(@Param("msgId")      String msgId,
              @Param("msgContent") String msgContent,
              @Param("msgTime")    String msgTime);

    // =============================================
    // delete
    // =============================================
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM holiday_dessert.message " +
                   "WHERE MSG_ID = :msgId",
           nativeQuery = true)
    void deleteByMsgId(@Param("msgId") String msgId);
    
}
