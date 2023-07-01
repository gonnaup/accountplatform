package org.gonnaup.accountplatform.account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

/**
 * 帐号概要信息
 *
 * @author gonnaup
 * @version created at 2023/6/28 下午11:10
 */
@Entity
@Table(name = "t_account_outline")
public class AccountOutline {
    @Id
    @GeneratedValue(generator = "")
    private Long id;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "t_accountoutline_role", joinColumns = @JoinColumn(name = "accountoutline_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnore
    private List<Role> roles;


}
