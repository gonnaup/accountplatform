package org.gonnaup.accountplatform.account.entity;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

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

    @OneToMany
    @MapsId("roleId")
    //外键列名以此JoinColumn优先，如果未声明，则取MapsId对应属性的列名
    @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "fk_role_permission_role"))
    private Role role;

    @OneToMany
    @MapsId("permissionId")
    @JoinColumn(name = "permission_id", foreignKey = @ForeignKey(name = "fk_role_permission_permission"))
    private Permission permission;

    public RolePermission() {
    }

    private RolePermission(RolePermissionPk id, Role role, Permission permission) {
        this.id = id;
        this.role = role;
        this.permission = permission;
    }

    public static RolePermission of(Role role, Permission permission) {
        RolePermissionPk pk = new RolePermissionPk(role.getId(), permission.getId());
        return new RolePermission(pk, role, permission);
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

    /**
     * 嵌入的组合PK
     */
    @Embeddable
    public static class RolePermissionPk implements Serializable {

        @Serial
        private static final long serialVersionUID = 5291459615909332771L;

        @Column(name = "role_id")
        private Integer roleId;

        @Column(name = "permission_id")
        private Integer permissionId;

        public RolePermissionPk() {
        }

        public RolePermissionPk(Integer roleId, Integer permissionId) {
            this.roleId = roleId;
            this.permissionId = permissionId;
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

}
