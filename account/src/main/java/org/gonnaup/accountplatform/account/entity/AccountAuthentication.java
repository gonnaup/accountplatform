package org.gonnaup.accountplatform.account.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 帐号认证信息
 *
 * @author gonnaup
 * @version created at 2023/6/28 下午11:12
 */
@Entity
@Table(name = "t_account_authentication")
public class AccountAuthentication extends TimedEntity {
    @Id
    private Long id;
}
