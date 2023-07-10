package org.gonnaup.accountplatform.account.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.StringJoiner;

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

    @Column(name = "role_name", length = 100, nullable = false, unique = true)
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

    @Override
    public String toString() {
        return new StringJoiner(", ", Role.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("roleName='" + roleName + "'")
                .add("roleLocalName='" + roleLocalName + "'")
                .add("permissionCode='" + permissionCode + "'")
                .add("description='" + description + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id) && Objects.equals(roleName, role.roleName) && Objects.equals(permissionCode, role.permissionCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roleName, permissionCode);
    }

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
