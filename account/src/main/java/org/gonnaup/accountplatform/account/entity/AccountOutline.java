package org.gonnaup.accountplatform.account.entity;

import jakarta.persistence.*;

/**
 * 帐号概要信息
 *
 * @author gonnaup
 * @version created at 2023/6/28 下午11:10
 */
@Entity
@Table(name = "t_account_outline", indexes = {@Index(name = "idx_accountoutline_name", columnList = "account_name", unique = true)})
public class AccountOutline extends TimedEntity {
    @Id
    private Long id;

    /**
     * 用户名,ascii码不超过100
     */
    @Column(name = "account_name", length = 100, nullable = false)
    private String accountName;

    /**
     * 昵称
     */
    @Column(name = "nick_name", length = 200, nullable = false)
    private String nickName;

    /**
     * 性别
     *
     * @see org.gonnaup.accountplatform.account.constant.Gender
     */
    @Column(length = 1, nullable = false)
    private String gender;

    /**
     * 所在地区
     */
    @Column(length = 20)
    private String region;

    /**
     * 头像url
     */
    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    /**
     * 个性签名
     */
    @Column(name = "personal_signature", length = 100)
    private String personalSignature;

    /**
     * 用户状态
     *
     * @see org.gonnaup.accountplatform.account.constant.AccountState
     */
    @Column(length = 1, nullable = false)
    private String state;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getPersonalSignature() {
        return personalSignature;
    }

    public void setPersonalSignature(String personalSignature) {
        this.personalSignature = personalSignature;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
