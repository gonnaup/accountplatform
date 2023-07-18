package org.gonnaup.accountplatform.account.service;

import org.gonnaup.accountplatform.account.entity.Permission;
import org.gonnaup.accountplatform.account.entity.Role;
import org.gonnaup.accountplatform.account.entity.RolePermission;
import org.gonnaup.accountplatform.account.entity.RolePermissionPk;

import java.util.List;

/**
 * 角色和权限关联关系服务类
 *
 * @author gonnaup
 * @version created at 2023/7/2 下午6:50
 */
public interface RolePermissionService {

    /**
     * 添加角色并关联权限列表
     *
     * @param role          角色对象
     * @param permissionIds 要关联的权限ID列表
     * @return 成功关联的数量
     */
    int addRoleAndAttachPermissions(Role role, List<Integer> permissionIds);

    /**
     * 添加权限并与角色列表关联
     *
     * @param permission 权限对象
     * @param roleIds    角色列表
     * @return 成功关联角色的数量
     */
    int addPermissionAndAttachRoles(Permission permission, List<Integer> roleIds);

    /**
     * 添加关联对象，如果有权限缓存，需删除关联缓存
     *
     * @param roleId       角色ID
     * @param permissionId 权限ID
     * @return 添加的对象，已存在返回null
     */
    RolePermission addRolePermission(RolePermissionPk rolePermissionPk);

    /**
     * 为某角色批量添加角色权限关联对象，如果有权限缓存，需删除关联缓存
     *
     * @param roleId        角色Id
     * @param permissionIds 权限Id列表，必须是没有和角色关联的权限
     * @return 添加的数量
     */
    int roleAttachPermissions(Integer roleId, List<Integer> permissionIds);

    /**
     * 为某权限批量添加角色权限关联对象，如果有权限缓存，需删除关联缓存
     *
     * @param permissionId 权限Id
     * @param roleIds      角色Id列表，必须是没有和权限关联的角色
     * @return 添加的数量
     */
    int permissionAttachRoles(Integer permissionId, List<Integer> roleIds);

    /**
     * 为角色重新关联权限列表，先删除已有的关联关系，再重新关联列表
     *
     * @param roleId       角色Id
     * @param permissionId 权限Id列表
     * @return 重新关联的个数
     */
    int updateRoleAttachPermissions(Integer roleId, List<Integer> permissionIds);

    /**
     * 为权限重新关联角色列表，先删除原有关联关系， 再重新关联
     *
     * @param permissionId 权限Id
     * @param roleIds      角色Id列表
     * @return 重新关联的个数
     */
    int updatePermissionAttachRoles(Integer permissionId, List<Integer> roleIds);

    /**
     * 根据联合主键删除一个关联对象，如果有权限缓存，需删除关联缓存
     *
     * @param rolePermissionPk 主键
     * @return 删除对象个数
     */
    int deleteByPrimaryKey(RolePermissionPk rolePermissionPk);

    /**
     * 批量删除某角色的关联对象，如果有权限缓存，需删除关联缓存
     *
     * @param roleId        角色Id
     * @param permissionIds 关联的权限Id列表
     * @return 删除的数量
     */
    int deleteRoleRef(Integer roleId, List<Integer> permissionIds);

    /**
     * 批量删除某权限的关联对象，如果有权限缓存，需删除关联缓存
     *
     * @param permissionId 权限Id
     * @param roleIds      角色Id列表
     * @return 删除的数量
     */
    int deletePermissionRef(Integer permissionId, List<Integer> roleIds);

    /**
     * 根据角色ID删除关联关系，如果有权限缓存，需删除关联缓存
     *
     * @param roleId 角色ID
     * @return 删除对象的个数
     */
    int deleteByRoleId(Integer roleId);

    /**
     * 删除和某权限的所有角色关联关系，如果有权限缓存，需删除关联缓存
     *
     * @param permissionId
     * @return 删除对象个数
     */
    int deleteByPermissionId(Integer permissionId);

    /**
     * 查询某角色关联了多少权限
     *
     * @param roleId
     * @return 关联权限的个数
     */
    int countByRoleId(Integer roleId);

    /**
     * 查询和某个角色关联的权限列表
     *
     * @param roleId
     * @return
     */
    List<Permission> findPermissionsByRoleId(Integer roleId);

    /**
     * 查询没有和某个角色关联的权限列表
     *
     * @param roleId 角色Id
     * @return 权限列表
     */
    List<Permission> findPermissionsNotAttachRole(Integer roleId);

    /**
     * 查询某个权限被多少角色关联
     *
     * @param permissionId
     * @return 被关联的数量
     */
    int countByPermissionId(Integer permissionId);

    /**
     * 查询关联了某个权限的角色列表
     *
     * @param permissionId
     * @return 关联了此权限的角色列表
     */
    List<Role> findRolesByPermissionId(Integer permissionId);

    /**
     * 查询没有和某权限关联的角色
     *
     * @param permissionId 权限Id
     * @return 角色列表
     */
    List<Role> findRolesNotAttachPermission(Integer permissionId);

}
