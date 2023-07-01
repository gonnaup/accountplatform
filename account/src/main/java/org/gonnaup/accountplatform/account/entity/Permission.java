package org.gonnaup.accountplatform.account.entity;

import jakarta.persistence.*;

import java.util.StringJoiner;

/**
 * 具体权限控制
 * 合法的权限码链由单个权限码和'#'按一定顺串成
 * 单个权限码必须是16进制的长整形数字
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
     * 本地名称
     */
    @Column(name = "permission_local_name", length = 200, nullable = false)
    private String permissionLocalName;


    /**
     * 权限所在的资源，可以为空，根据权限实现方式决定
     */
    @Column(name = "resources", length = 500)
    private String resources;


    /**
     * 权限位，位运算法权限验证时使用
     */
    @Column(name = "permission_location")
    private Integer permissionLocation;

    /**
     * 位运算鉴权
     * <p>
     * 权限表示：长整形数字64位，有63位可用于权限位声明，每位数字1代表有权限，0代表无权限。
     * 当系统权限多于63种时，可使用长整形数字列来解决，每个长整形数字用'#'隔开，如'86868#96969'包含
     * 两个64位整数，86868代表1~63权限位，96969代表64~126权限位，以此类推。
     * <p>
     * 算法：
     * 1. 鉴权：（own：拥有的权限，need：需要的权限） own & need == need
     * 2. 授权：（own：拥有的权限，grant：授予的权限）own |= grant
     * 3. 删除授权：（own：拥有的权限，del：要删除的权限）own &= ~del
     * <p>
     * 使用16进制数表示
     */
    @Column(name = "permission_code", length = 500)
    private String permissionCode;


    @Column(length = 500)
    private String description;


    @Override
    public String toString() {
        return new StringJoiner(", ", Permission.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("permissionName='" + permissionName + "'")
                .add("permissionCode='" + permissionCode + "'")
                .add("resources='" + resources + "'")
                .add("permissionLocation=" + permissionLocation)
                .add("description='" + description + "'")
                .toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    public Integer getPermissionLocation() {
        return permissionLocation;
    }

    public void setPermissionLocation(Integer permissionLocation) {
        this.permissionLocation = permissionLocation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPermissionLocalName() {
        return permissionLocalName;
    }

    public void setPermissionLocalName(String permissionLocalName) {
        this.permissionLocalName = permissionLocalName;
    }
}
