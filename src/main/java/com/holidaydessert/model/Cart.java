package com.holidaydessert.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

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
@Entity
@Table(name = "cart")
public class Cart extends Base {
	
	@EmbeddedId
    @AttributeOverrides({
    	@AttributeOverride(name = "memId", column = @Column(name = "MEM_ID")),
    	@AttributeOverride(name = "pdId", column = @Column(name = "PD_ID"))
    })
    private CartId id;				   // 複合主鍵ID
	
	@Transient
	private String memId;              // 會員ID
	
	@Transient
	private String pdId;               // 商品ID
	
    @Column(name = "CART_PD_QUANTITY")
	private String cartPdQuantity;     // 商品個數

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Embeddable
	public static class CartId implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
		private String memId;		   // 會員ID
		private String pdId;		   // 商品ID
		
		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof CartId))
				return false;
			CartId cartId = (CartId) o;
			return Objects.equals(getMemId(), cartId.getMemId()) && Objects.equals(getPdId(), cartId.getPdId());
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(getMemId(), getPdId());
		}
		
	}
	
}
