package org.gonnaup.accountplatform.account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

/**
 * Role/Permission关联关系实体类
 *
 * @author gonnaup
 * @version created at 2023/7/2 下午6:19
 */
@Entity
@Table(name = "t_role_permission")
public class RolePermission {

    @EmbeddedId
    private RolePermissionPk id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    //外键列名以此JoinColumn优先，如果未声明，则取MapsId对应属性的列名
    @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "fk_role_permission_role"))
    @JsonIgnore
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("permissionId")
    @JoinColumn(name = "permission_id", foreignKey = @ForeignKey(name = "fk_role_permission_permission"))
    @JsonIgnore
    private Permission permission;

    public RolePermission() {
    }

    private RolePermission(RolePermissionPk id, Role role, Permission permission) {
        this.id = id;
        this.role = role;
        this.permission = permission;
    }

    public static RolePermission of(RolePermissionPk pk, Role role, Permission permission) {
        return new RolePermission(pk, role, permission);
    }

    public static RolePermission of(Role role, Permission permission) {
        return new RolePermission(RolePermissionPk.of(role.getId(), permission.getId()), role, permission);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RolePermission that = (RolePermission) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public RolePermissionPk getId() {
        return id;
    }

    public void setId(RolePermissionPk id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

}
