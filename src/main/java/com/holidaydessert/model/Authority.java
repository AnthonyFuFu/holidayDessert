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
@Table(name = "authority")
public class Authority extends Base {
	
	@EmbeddedId
    @AttributeOverrides({
    	@AttributeOverride(name = "empId", column = @Column(name = "EMP_ID")),
    	@AttributeOverride(name = "funcId", column = @Column(name = "FUNC_ID"))
    })
    private AuthorityId id;			   // 複合主鍵ID

	@Transient
	private String empId;              // 管理員ID

	@Transient
	private String funcId;             // 功能ID
	
    @Column(name = "AUTH_STATUS")
	private String authStatus;         // 權限狀態
	
}

@Getter
@Setter
@Embeddable
class AuthorityId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    private String empId;              // 管理員ID
    private String funcId;             // 功能ID

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthorityId)) return false;
        AuthorityId authorityId = (AuthorityId) o;
        return Objects.equals(getEmpId(), authorityId.getEmpId()) &&
               Objects.equals(getFuncId(), authorityId.getFuncId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmpId(), getFuncId());
    }
    
}
