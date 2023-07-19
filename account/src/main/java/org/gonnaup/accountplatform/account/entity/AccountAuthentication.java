package org.gonnaup.accountplatform.account.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * 帐号认证信息，存储账号密码
 *
 * @author gonnaup
 * @version created at 2023/6/28 下午11:12
 */
@Entity
@Table(name = "t_account_authentication")
public class AccountAuthentication extends TimedEntity {

    /**
     * ID为对应账号ID
     */
    @Id
    @Column(name = "account_id")
    private Long accountId;

    /**
     * 凭证密码
     */
    @Column(length = 200)
    private String credential;

    public AccountAuthentication() {
    }

    /**
     * 静态创建对象方法
     *
     * @param accountId  账号ID，不能为null
     * @param credential 凭证，不能为null
     * @return {@link AccountAuthentication}
     * @throws NullPointerException 如果存在null值参数
     */
    public static AccountAuthentication of(Long accountId, String credential) {
        Objects.requireNonNull(accountId);
        Objects.requireNonNull(credential);
        AccountAuthentication accountAuthentication = new AccountAuthentication();
        accountAuthentication.accountId = accountId;
        accountAuthentication.credential = credential;
        return accountAuthentication;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AccountAuthentication.class.getSimpleName() + "[", "]")
                .add("id=" + accountId)
                .add("credential=xxxx")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountAuthentication that = (AccountAuthentication) o;
        return Objects.equals(accountId, that.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }
}
