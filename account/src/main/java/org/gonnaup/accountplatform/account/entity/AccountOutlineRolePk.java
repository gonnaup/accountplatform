package org.gonnaup.accountplatform.account.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author gonnaup
 * @version created at 2023/7/4 下午10:27
 */
@Embeddable
public class AccountOutlineRolePk implements Serializable {

    @Serial
    private static final long serialVersionUID = 166052900495193208L;

    @Column(name = "accountoutline_id")
    private Long accountOutlineId;

    @Column(name = "role_id")
    private Integer roleId;

    public AccountOutlineRolePk() {
    }

    public static AccountOutlineRolePk of(Long accountOutlineId, Integer roleId) {
        AccountOutlineRolePk pk = new AccountOutlineRolePk();
        pk.accountOutlineId = accountOutlineId;
        pk.roleId = roleId;
        return pk;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", org.gonnaup.accountplatform.account.entity.AccountOutlineRolePk.class.getSimpleName() + "[", "]")
                .add("accountOutlineId=" + accountOutlineId)
                .add("roleId=" + roleId)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        org.gonnaup.accountplatform.account.entity.AccountOutlineRolePk that = (org.gonnaup.accountplatform.account.entity.AccountOutlineRolePk) o;
        return Objects.equals(accountOutlineId, that.accountOutlineId) && Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountOutlineId, roleId);
    }

    public Long getAccountOutlineId() {
        return accountOutlineId;
    }

    public void setAccountOutlineId(Long accountOutlineId) {
        this.accountOutlineId = accountOutlineId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
