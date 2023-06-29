package org.gonnaup.accountplatform.account.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 具体权限控制
 *
 * @author gonnaup
 * @version created at 2023/6/28 下午11:26
 */
@Entity
@Table(name = "t_permission")
public class Permission {

    @Id
    private Integer id;

    /**
     * 权限名称
     */
    @Column(name = "permission_name", length = 100, nullable = false)
    private String permissionName;

    /**
     * 权限所在的资源，可以为空，根据权限实现方式决定
     */
    @Column(name = "resources", length = 3000)
    private String resources;

    /**
     * 权限位，位运算法权限验证时使用
     */
    @Column(name = "permission_location")
    private Integer permissionLocation;


    @Column(length = 500)
    private String description;

}
