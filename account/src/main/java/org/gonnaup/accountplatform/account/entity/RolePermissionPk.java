package org.gonnaup.accountplatform.account.entity;

/**
 * 嵌入的组合PK
 *
 * @author gonnaup
 * @version created at 2023/7/4 下午10:32
 */

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

@Embeddable
public class RolePermissionPk implements Serializable {

    @Serial
    private static final long serialVersionUID = 5291459615909332771L;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "permission_id")
    private Integer permissionId;

    public RolePermissionPk() {
    }

    public static RolePermissionPk of(Integer roleId, Integer permissionId) {
        RolePermissionPk pk = new RolePermissionPk();
        pk.setRoleId(roleId);
        pk.setPermissionId(permissionId);
        return pk;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RolePermissionPk.class.getSimpleName() + "[", "]")
                .add("roleId=" + roleId)
                .add("permissionId=" + permissionId)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RolePermissionPk that = (RolePermissionPk) o;
        return Objects.equals(roleId, that.roleId) && Objects.equals(permissionId, that.permissionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, permissionId);
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }
}