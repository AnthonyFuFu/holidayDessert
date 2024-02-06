package com.holidaydessert.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NonNull
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "member_coupon")
public class MemberCoupon extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FUNC_ID")
	private String memCpId;            // 會員優惠券ID

    @Column(name = "MEM_ID")
	private String memId;              // 會員ID

    @Column(name = "CP_ID")
	private String cpId;               // 優惠券種類ID

    @Column(name = "MEM_CP_START")
	private String memCpStart;         // 起始時間

    @Column(name = "MEM_CP_END")
	private String memCpEnd;           // 截止時間

    @Column(name = "MEM_CP_STATUS")
	private String memCpStatus;        // 使用狀態(0:不可用 1:可用)

    @Column(name = "MEM_CP_RECORD")
	private String memCpRecord;	       // 使用紀錄

    @ManyToOne
    @JoinColumn(name = "MEM_ID", insertable = false, updatable = false)
	private Member member;             // 會員

    @ManyToOne
    @JoinColumn(name = "CP_ID", insertable = false, updatable = false)
	private Coupon coupon;             // 優惠券種類

}
