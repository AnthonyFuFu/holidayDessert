package com.holidaydessert.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.holidaydessert.model.MemberCoupon;

@Repository
public interface MemberCouponRepository extends JpaRepository<MemberCoupon, String> {

    // =============================================
    // useCoupon：更新使用狀態
    // =============================================
    @Modifying
    @Transactional
    @Query(value = "UPDATE holiday_dessert.member_coupon " +
                   "SET MEM_CP_STATUS = 0, MEM_CP_RECORD = NOW() " +
                   "WHERE MEM_CP_ID = :memCpId",
           nativeQuery = true)
    void useCoupon(@Param("memCpId") String memCpId);

    // =============================================
    // receiveOneDayCoupon：領取一日券
    // =============================================
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO holiday_dessert.member_coupon " +
                   "(MEM_ID, CP_ID, MEM_CP_START, MEM_CP_END, MEM_CP_STATUS) " +
                   "VALUES (:memId, :cpId, " +
                   "        CONCAT(CURDATE(), ' 00:00:00'), " +
                   "        CONCAT(DATE_ADD(CURDATE(), INTERVAL 1 DAY), ' 23:59:59'), 1)",
           nativeQuery = true)
    void receiveOneDayCoupon(@Param("memId") Integer memId,
                              @Param("cpId") String cpId);

    // =============================================
    // receiveOneWeekCoupon：領取一週券
    // =============================================
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO holiday_dessert.member_coupon " +
                   "(MEM_ID, CP_ID, MEM_CP_START, MEM_CP_END, MEM_CP_STATUS) " +
                   "VALUES (:memId, :cpId, " +
                   "        CONCAT(CURDATE(), ' 00:00:00'), " +
                   "        CONCAT(DATE_ADD(CURDATE(), INTERVAL 1 WEEK), ' 23:59:59'), 1)",
           nativeQuery = true)
    void receiveOneWeekCoupon(@Param("memId") Integer memId,
                               @Param("cpId") String cpId);

    // =============================================
    // receiveOneMonthCoupon：領取一個月券
    // =============================================
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO holiday_dessert.member_coupon " +
                   "(MEM_ID, CP_ID, MEM_CP_START, MEM_CP_END, MEM_CP_STATUS) " +
                   "VALUES (:memId, :cpId, " +
                   "        CONCAT(CURDATE(), ' 00:00:00'), " +
                   "        CONCAT(DATE_ADD(CURDATE(), INTERVAL 1 MONTH), ' 23:59:59'), 1)",
           nativeQuery = true)
    void receiveOneMonthCoupon(@Param("memId") Integer memId,
                                @Param("cpId") String cpId);

    // =============================================
    // listMemberCoupon：查詢會員有效優惠券
    // =============================================
    @Query(value = "SELECT MEM_NAME, " +
                   "       DATE_FORMAT(MEM_CP_START, '%Y-%m-%d %H:%i:%s') MEM_CP_START, " +
                   "       DATE_FORMAT(MEM_CP_END,   '%Y-%m-%d %H:%i:%s') MEM_CP_END, " +
                   "       MEM_CP_ID, MEM_CP_STATUS, MEM_CP_RECORD, CP_NAME, CP_DISCOUNT " +
                   "FROM holiday_dessert.member_coupon mc " +
                   "LEFT JOIN member m ON m.MEM_ID = mc.MEM_ID " +
                   "LEFT JOIN coupon c ON c.CP_ID = mc.CP_ID " +
                   "WHERE mc.MEM_ID = :memId " +
                   "AND CURDATE() BETWEEN MEM_CP_START AND MEM_CP_END",
           nativeQuery = true)
    List<Map<String, Object>> listMemberCoupon(@Param("memId") String memId);
}
