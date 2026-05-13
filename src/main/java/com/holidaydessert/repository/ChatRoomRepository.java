package com.holidaydessert.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.ChatRoom;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // =============================================
    // addChatRoom → 用 save() 處理，Repository 不需要額外方法
    // =============================================

    // =============================================
    // getChatRoomByMessage
    // =============================================
    @Query(value = "SELECT ROOM_ID FROM holiday_dessert.message msg " +
                   "WHERE MEM_ID = :memId " +
                   "GROUP BY ROOM_ID",
           nativeQuery = true)
    List<Map<String, Object>> getChatRoomByMessage(@Param("memId") String memId);

    // =============================================
    // getChatRoom
    // =============================================
    @Query(value = "SELECT chat.*, msg.EMP_ID, msg.MEM_ID, " +
                   "       (SELECT e.EMP_NAME FROM employee e WHERE e.EMP_ID = msg.EMP_ID LIMIT 1) AS EMP_NAME " +
                   "FROM holiday_dessert.chat_room chat " +
                   "LEFT JOIN message msg ON chat.ROOM_ID = msg.ROOM_ID " +
                   "WHERE chat.ROOM_ID = :chatRoomId " +
                   "GROUP BY chat.ROOM_ID",
           nativeQuery = true)
    List<Map<String, Object>> getChatRoom(@Param("chatRoomId") String chatRoomId);

    // =============================================
    // getServiceStaff
    // =============================================
    @Query(value = "SELECT msg.*, MEM_NAME, EMP_NAME " +
                   "FROM holiday_dessert.message msg " +
                   "LEFT JOIN member m ON m.MEM_ID = msg.MEM_ID " +
                   "LEFT JOIN employee e ON e.EMP_ID = msg.EMP_ID " +
                   "WHERE m.MEM_ID = :memId",
           nativeQuery = true)
    List<Map<String, Object>> getServiceStaff(@Param("memId") String memId);
    
}