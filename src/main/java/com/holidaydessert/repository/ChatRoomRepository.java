package com.holidaydessert.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
    // findByMemId：會員的聊天室（MEM_ID 有 UNIQUE，最多一筆）
    // =============================================
    Optional<ChatRoom> findByMemId(Integer memId);

    // =============================================
    // getChatRoom：前端需要的欄位 ROOM_ID / ROOM_URL / MEM_ID / EMP_ID / EMP_NAME
    // =============================================
    @Query(value = "SELECT chat.*, e.EMP_NAME " +
                   "FROM holiday_dessert.chat_room chat " +
                   "LEFT JOIN employee e ON e.EMP_ID = chat.EMP_ID " +
                   "WHERE chat.ROOM_ID = :chatRoomId",
           nativeQuery = true)
    List<Map<String, Object>> getChatRoom(@Param("chatRoomId") Long chatRoomId);

    // =============================================
    // claimChatRoom：認領聊天室（誰先點誰負責）
    // =============================================
    @Modifying
    @Query(value = "UPDATE holiday_dessert.chat_room SET EMP_ID = :empId " +
                   "WHERE ROOM_ID = :roomId AND EMP_ID IS NULL",
           nativeQuery = true)
    int claimChatRoom(@Param("empId") String empId, @Param("roomId") String roomId);

    // =============================================
    // getServiceStaff
    // =============================================
    @Query(value = "SELECT msg.*, MEM_NAME, EMP_NAME " +
                   "FROM holiday_dessert.message msg " +
                   "LEFT JOIN member m ON m.MEM_ID = msg.MEM_ID " +
                   "LEFT JOIN employee e ON e.EMP_ID = msg.EMP_ID " +
                   "WHERE m.MEM_ID = :memId",
           nativeQuery = true)
    List<Map<String, Object>> getServiceStaff(@Param("memId") Integer memId);
    
}