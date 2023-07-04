package org.gonnaup.accountplatform.account.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 帐号概要信息
 *
 * @author gonnaup
 * @version created at 2023/6/28 下午11:10
 */
@Entity
@Table(name = "t_account_outline")
public class AccountOutline extends TimedEntity {
    @Id
    private Long id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
