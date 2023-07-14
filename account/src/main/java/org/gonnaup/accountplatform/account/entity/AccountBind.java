package org.gonnaup.accountplatform.account.entity;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * 账号绑定的第三方凭证，同种类型的凭证每个帐号
 *
 * @author gonnaup
 * @version created at 2023/7/14 19:01
 */
@Entity
@Table(name = "t_account_bind",
        indexes = {@Index(name = "idx_account_bind_bindtype_principal", columnList = "bind_type, principal", unique = true),
                @Index(name = "idx_account_bind_accountid", columnList = "account_id")})
public class AccountBind extends TimedEntity {
    @Id
    private Long id;

    /**
     * 账号Id
     */
    @Column(name = "account_id", nullable = false)
    private Long accountId;

    /**
     * 绑定账号类型
     *
     * @see org.gonnaup.accountplatform.account.constant.BindType
     */
    @Column(name = "bind_type", length = 1, nullable = false)
    private String bindType;

    /**
     * 主体，根据认证类型不同而不同
     */
    @Column(length = 200, nullable = false)
    private String principal;

    /**
     * 绑定第三方账号可能产生的token，可能为空
     */
    @Column(length = 500)
    private String token;

    @Override
    public String toString() {
        return new StringJoiner(", ", AccountBind.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("accountId=" + accountId)
                .add("bindType='" + bindType + "'")
                .add("principal='" + principal + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountBind that = (AccountBind) o;
        return Objects.equals(id, that.id) && Objects.equals(accountId, that.accountId) && Objects.equals(bindType, that.bindType) && Objects.equals(principal, that.principal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, bindType, principal);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getBindType() {
        return bindType;
    }

    public void setBindType(String bindType) {
        this.bindType = bindType;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
