package org.gonnaup.accountplatform.account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * 帐号概述和角色关联关系
 *
 * @author gonnaup
 * @version created at 2023/7/4 下午9:49
 */
@Entity
@Table(name = "t_accountoutline_role")
public class AccountOutlineRole {

    @EmbeddedId
    private AccountOutlineRolePk id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("accountOutlineId")
    @JoinColumn(name = "accountoutline_id", foreignKey = @ForeignKey(name = "fk_accountoutline_role_accountoutline"))
    @JsonIgnore
    private AccountOutline accountOutline;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "fk_accountoutline_role_role"))
    @JsonIgnore
    private Role role;

    public AccountOutlineRole() {
    }

    public static AccountOutlineRole of(AccountOutlineRolePk pk, AccountOutline accountOutline, Role role) {
        AccountOutlineRole ar = new AccountOutlineRole();
        ar.setId(pk);
        ar.setAccountOutline(accountOutline);
        ar.setRole(role);
        return ar;
    }

    public static AccountOutlineRole of(AccountOutline accountOutline, Role role) {
        AccountOutlineRolePk pk = AccountOutlineRolePk.of(accountOutline.getId(), role.getId());
        AccountOutlineRole ar = new AccountOutlineRole();
        ar.setId(pk);
        ar.setAccountOutline(accountOutline);
        ar.setRole(role);
        return ar;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AccountOutlineRole.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountOutlineRole that = (AccountOutlineRole) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public AccountOutlineRolePk getId() {
        return id;
    }

    public void setId(AccountOutlineRolePk id) {
        this.id = id;
    }

    public AccountOutline getAccountOutline() {
        return accountOutline;
    }

    public void setAccountOutline(AccountOutline accountOutline) {
        this.accountOutline = accountOutline;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
