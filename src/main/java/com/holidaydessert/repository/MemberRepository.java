package com.holidaydessert.repository;

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
public interface MemberRepository extends JpaRepository<Member, String> {
	
	// CRUDController Use
	List<Member> findByMemName(String name);
	List<Member> findByMemNameAndMemEmail(String name, String email);
    List<Member> findByMemEmailContaining(String memEmail); // 模糊搜尋
	
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
    void edit(@Param("memId")      String memId,
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
    void verificationEmail(@Param("memId") String memId);

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
    void updateVerification(@Param("memId")                String memId,
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
    void updatePassword(@Param("memId")       String memId,
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
