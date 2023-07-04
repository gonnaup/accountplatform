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
     * 添加关联对象，如果有权限缓存，需删除关联缓存
     *
     * @param roleId       角色ID
     * @param permissionId 权限ID
     * @return 添加的对象
     */
    RolePermission addRolePermission(RolePermissionPk rolePermissionPk);

    /**
     * 批量添加角色权限关联对象，如果有权限缓存，需删除关联缓存
     *
     * @param rolePermissionPkList roleId-permissionId列表
     * @return 添加的数量
     */
    int addRolePermissionList(List<RolePermissionPk> rolePermissionPkList);

    /**
     * 根据联合主键删除一个关联对象，如果有权限缓存，需删除关联缓存
     *
     * @param rolePermissionPk 主键
     * @return 删除对象个数
     */
    int deleteByPrimaryKey(RolePermissionPk rolePermissionPk);

    /**
     * 批量删除关联对象，如果有权限缓存，需删除关联缓存
     *
     * @param rolePermissionPkList 关联对象主键列表
     * @return 删除的数量
     */
    int deleteByPrimaryKeyList(List<RolePermissionPk> rolePermissionPkList);

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
     * 查询角色所拥有的权限码列表
     *
     * @param roleId 角色Id
     * @return 权限码列表
     */
    List<String> findPermissionCodeByRoleId(Integer roleId);

}
