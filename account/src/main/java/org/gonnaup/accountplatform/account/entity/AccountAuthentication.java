package org.gonnaup.accountplatform.account.entity;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * 帐号认证信息，每个帐号可以有多个相同类型的认证方式，比如可以绑定多个邮箱，但每个邮箱只能绑定一次
 *
 * @author gonnaup
 * @version created at 2023/6/28 下午11:12
 */
@Entity
@Table(name = "t_account_authentication", indexes = {@Index(name = "idx_account_authentication_authenticationtype_principal",
        columnList = "authenticationType, principal", unique = true)})
public class AccountAuthentication extends TimedEntity {
    @Id
    private Long id;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "authentication_type", length = 1, nullable = false)
    private String authenticationType;

    /**
     * 主体，根据认证类型不同而不同
     */
    @Column(length = 200, nullable = false)
    private String principal;

    /**
     * 凭证，一般为密码，可以为空，比如邮箱登录
     */
    @Column(length = 200)
    private String credential;

    @Override
    public String toString() {
        return new StringJoiner(", ", AccountAuthentication.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("accountId=" + accountId)
                .add("authenticationType='" + authenticationType + "'")
                .add("principal='" + principal + "'")
                .add("credential=xxxx")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountAuthentication that = (AccountAuthentication) o;
        return Objects.equals(id, that.id) && Objects.equals(accountId, that.accountId) && Objects.equals(authenticationType, that.authenticationType) && Objects.equals(principal, that.principal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, authenticationType, principal);
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

    public String getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(String authenticationType) {
        this.authenticationType = authenticationType;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }
}
