package org.gonnaup.accountplatform.account.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 角色
 *
 * @author gonnaup
 * @version created at 2023/6/28 下午11:16
 */
@Entity
@Table(name = "t_role")
public class Role extends TimedEntity {
    @Id
    private Integer id;

    @Column(name = "role_name", length = 100, nullable = false)
    private String roleName;

    @Column(name = "role_name", length = 200, nullable = false)
    private String roleLocalName;

    /**
     * 权限编码，需根据<code>permissions</code>计算得出
     *
     * @see Permission#permissionCode
     */
    @Column(name = "permission_code", length = 500)
    private String permissionCode;

    @Column(length = 500)
    private String description;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoleLocalName() {
        return roleLocalName;
    }

    public void setRoleLocalName(String roleLocalName) {
        this.roleLocalName = roleLocalName;
    }
}
