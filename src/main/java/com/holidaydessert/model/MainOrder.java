package com.holidaydessert.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Builder
@NonNull
@NoArgsConstructor
@AllArgsConstructor
public class MainOrder extends Base {

	private String ordId;              // 訂單ID
	private String memId;              // 會員ID
	private String memCpId;            // 會員優惠券ID
	private String ordSubtotal;        // 小計(未扣cupon折扣)
	private String ordTotal;           // 總計(扣除cupon折扣)
	private String ordStatus;          // 訂單狀態(0:未完成 1:已完成 2:配送中 3:取消)(預設 0)
	private String ordCreate;          // 訂單成立時間
	private String ordRecipient;       // 收件人
	private String ordRecipientPhone;  // 收件人連絡電話
	private String ordPayment;         // 付款方式(0:貨到付款 1:信用卡)
	private String ordDelivery;        // 送貨方式(0:超商取貨 1:宅配)
	private String ordAddress;         // 收件地址
	private String ordNote;            // 訂單備註
	private String ordDeliveryFee;     // 運費

}
