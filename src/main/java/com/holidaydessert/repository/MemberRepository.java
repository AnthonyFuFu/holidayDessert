package com.holidaydessert.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.Map;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.holidaydessert.model.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

    // =============================================
    // issueCouponList：後台 DataTables 分頁 - 發放優惠券：近一個月有消費的會員
    // 排序由 Pageable 的 Sort 動態產生，SQL 內不寫 ORDER BY
    // =============================================
    @Query(value =
               "SELECT * FROM ( " +
               "  SELECT m.MEM_ID, m.MEM_NAME, m.MEM_ACCOUNT, m.MEM_PHONE, m.MEM_EMAIL, " +
               "         CASE m.MEM_GENDER WHEN 'm' THEN '男' WHEN 'f' THEN '女' END AS GENDER, " +
               "         DATE_FORMAT(m.MEM_BIRTHDAY, '%Y-%m-%d') MEM_BIRTHDAY, SUM(mo.ORD_TOTAL) AS TOTAL_EXPENSE " +
               "  FROM holiday_dessert.member m " +
               "  INNER JOIN holiday_dessert.main_order mo ON m.MEM_ID = mo.MEM_ID " +
               "  WHERE mo.ORD_CREATE >= DATE_SUB(NOW(), INTERVAL 1 MONTH) " +
               "    AND m.MEM_STATUS = 1 AND m.MEM_VERIFICATION_STATUS = 1 " +
               "  GROUP BY m.MEM_ID, m.MEM_NAME, m.MEM_ACCOUNT, m.MEM_PHONE, m.MEM_EMAIL, m.MEM_GENDER " +
               ") AS subquery " +
               "WHERE ((:totalExpense IS NULL AND (:keyword = '' OR INSTR(MEM_NAME, :keyword) > 0 OR INSTR(GENDER, :keyword) > 0 OR INSTR(MEM_BIRTHDAY, :keyword) > 0)) OR TOTAL_EXPENSE >= :totalExpense) ",
           countQuery =
               "SELECT COUNT(*) FROM ( " +
               "  SELECT m.MEM_ID, m.MEM_NAME, m.MEM_ACCOUNT, m.MEM_PHONE, m.MEM_EMAIL, " +
               "         CASE m.MEM_GENDER WHEN 'm' THEN '男' WHEN 'f' THEN '女' END AS GENDER, " +
               "         DATE_FORMAT(m.MEM_BIRTHDAY, '%Y-%m-%d') MEM_BIRTHDAY, SUM(mo.ORD_TOTAL) AS TOTAL_EXPENSE " +
               "  FROM holiday_dessert.member m " +
               "  INNER JOIN holiday_dessert.main_order mo ON m.MEM_ID = mo.MEM_ID " +
               "  WHERE mo.ORD_CREATE >= DATE_SUB(NOW(), INTERVAL 1 MONTH) " +
               "    AND m.MEM_STATUS = 1 AND m.MEM_VERIFICATION_STATUS = 1 " +
               "  GROUP BY m.MEM_ID, m.MEM_NAME, m.MEM_ACCOUNT, m.MEM_PHONE, m.MEM_EMAIL, m.MEM_GENDER " +
               ") AS subquery " +
               "WHERE ((:totalExpense IS NULL AND (:keyword = '' OR INSTR(MEM_NAME, :keyword) > 0 OR INSTR(GENDER, :keyword) > 0 OR INSTR(MEM_BIRTHDAY, :keyword) > 0)) OR TOTAL_EXPENSE >= :totalExpense) ",
           nativeQuery = true)
    Page<Map<String, Object>> issueCouponList(@Param("keyword") String keyword, @Param("totalExpense") Long totalExpense, Pageable pageable);

    // =============================================
    // backList：後台 DataTables 分頁 - 會員
    // 排序由 Pageable 的 Sort 動態產生，SQL 內不寫 ORDER BY
    // =============================================
    @Query(value =
               "SELECT * FROM ( " +
               "  SELECT MEM_ID, MEM_NAME, MEM_ACCOUNT, MEM_GENDER, MEM_PHONE, MEM_EMAIL, MEM_ADDRESS, " +
               "         DATE_FORMAT(MEM_BIRTHDAY, '%Y-%m-%d') MEM_BIRTHDAY, MEM_STATUS, MEM_VERIFICATION_STATUS, " +
               "         CASE MEM_GENDER WHEN 'm' THEN '男' WHEN 'f' THEN '女' END AS GENDER " +
               "  FROM holiday_dessert.member " +
               ") AS subquery " +
               "WHERE (:keyword = '' OR INSTR(MEM_NAME, :keyword) > 0 OR INSTR(MEM_ACCOUNT, :keyword) > 0 OR INSTR(MEM_PHONE, :keyword) > 0 OR INSTR(MEM_EMAIL, :keyword) > 0 OR INSTR(MEM_ADDRESS, :keyword) > 0 OR INSTR(MEM_BIRTHDAY, :keyword) > 0 OR INSTR(GENDER, :keyword) > 0) ",
           countQuery =
               "SELECT COUNT(*) FROM ( " +
               "  SELECT MEM_ID, MEM_NAME, MEM_ACCOUNT, MEM_GENDER, MEM_PHONE, MEM_EMAIL, MEM_ADDRESS, " +
               "         DATE_FORMAT(MEM_BIRTHDAY, '%Y-%m-%d') MEM_BIRTHDAY, MEM_STATUS, MEM_VERIFICATION_STATUS, " +
               "         CASE MEM_GENDER WHEN 'm' THEN '男' WHEN 'f' THEN '女' END AS GENDER " +
               "  FROM holiday_dessert.member " +
               ") AS subquery " +
               "WHERE (:keyword = '' OR INSTR(MEM_NAME, :keyword) > 0 OR INSTR(MEM_ACCOUNT, :keyword) > 0 OR INSTR(MEM_PHONE, :keyword) > 0 OR INSTR(MEM_EMAIL, :keyword) > 0 OR INSTR(MEM_ADDRESS, :keyword) > 0 OR INSTR(MEM_BIRTHDAY, :keyword) > 0 OR INSTR(GENDER, :keyword) > 0) ",
           nativeQuery = true)
    Page<Map<String, Object>> backList(@Param("keyword") String keyword, Pageable pageable);
	
	// CRUDController Use
	List<Member> findByMemName(String name);
	List<Member> findByMemNameAndMemEmail(String name, String email);
    List<Member> findByMemEmailContaining(String memEmail); // 模糊搜尋

    // 搶票測試用：取得測試會員 ID（帳號前綴相同）
    @Query("SELECT m.memId FROM Member m WHERE m.memAccount LIKE CONCAT(:prefix, '%') ORDER BY m.memId")
    List<Integer> findIdsByAccountPrefix(@Param("prefix") String prefix);
	
    // =============================================
    // register → save() 內建，不需要額外方法
    // =============================================

    // =============================================
    // edit：更新會員資料
    // =============================================
    @Modifying
    @Transactional
    @Query(value = "UPDATE holiday_dessert.member " +
                   "SET MEM_NAME = :memName, MEM_ACCOUNT = :memAccount, MEM_PASSWORD = :memPassword, " +
                   "    MEM_GENDER = :memGender, MEM_PHONE = :memPhone, MEM_EMAIL = :memEmail, " +
                   "    MEM_ADDRESS = :memAddress, MEM_BIRTHDAY = :memBirthday, " +
                   "    MEM_PICTURE = :memPicture, MEM_IMAGE = :memImage " +
                   "WHERE MEM_ID = :memId",
           nativeQuery = true)
    void edit(@Param("memId")      Integer memId,
              @Param("memName")    String memName,
              @Param("memAccount") String memAccount,
              @Param("memPassword")String memPassword,
              @Param("memGender")  String memGender,
              @Param("memPhone")   String memPhone,
              @Param("memEmail")   String memEmail,
              @Param("memAddress") String memAddress,
              @Param("memBirthday")String memBirthday,
              @Param("memPicture") String memPicture,
              @Param("memImage")   String memImage);

    // =============================================
    // verificationEmail：驗證信箱，更新狀態
    // =============================================
    @Modifying
    @Transactional
    @Query(value = "UPDATE holiday_dessert.member " +
                   "SET MEM_VERIFICATION_STATUS = '1', MEM_STATUS = '1' " +
                   "WHERE MEM_ID = :memId",
           nativeQuery = true)
    void verificationEmail(@Param("memId") Integer memId);

    // =============================================
    // getCheckMemberEmail：依 Email 查詢會員
    // Spring Data JPA 命名規則自動產生，不需要 @Query
    // =============================================
    Optional<Member> findByMemEmail(String memEmail);

    // =============================================
    // updateVerification：更新驗證碼
    // =============================================
    @Modifying
    @Transactional
    @Query(value = "UPDATE holiday_dessert.member " +
                   "SET MEM_VERIFICATION_CODE = :memVerificationCode " +
                   "WHERE MEM_ID = :memId",
           nativeQuery = true)
    void updateVerification(@Param("memId")                Integer memId,
                             @Param("memVerificationCode") String memVerificationCode);

    // =============================================
    // updatePassword：更新密碼
    // =============================================
    @Modifying
    @Transactional
    @Query(value = "UPDATE holiday_dessert.member " +
                   "SET MEM_PASSWORD = :memPassword " +
                   "WHERE MEM_ID = :memId",
           nativeQuery = true)
    void updatePassword(@Param("memId")       Integer memId,
                         @Param("memPassword") String memPassword);

    // =============================================
    // login：依 Email + Password 查詢
    // Spring Data JPA 命名規則自動產生，不需要 @Query
    // =============================================
    Optional<Member> findByMemEmailAndMemPassword(String memEmail, String memPassword);

    // =============================================
    // getDataByGoogleUid：依 Google UID 查詢
    // Spring Data JPA 命名規則自動產生，不需要 @Query
    // =============================================
    Optional<Member> findByMemGoogleUid(String memGoogleUid);
	
}
