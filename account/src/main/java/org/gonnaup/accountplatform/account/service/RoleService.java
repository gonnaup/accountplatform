package org.gonnaup.accountplatform.account.service;

import org.gonnaup.accountplatform.account.domain.GenericPage;
import org.gonnaup.accountplatform.account.entity.Role;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 角色服务接口
 *
 * @author gonnaup
 * @version created at 2023/7/2 下午7:37
 */
public interface RoleService {

    /**
     * 添加角色对象
     *
     * @param role
     * @return 成功添加的角色对象
     */
    Role addRole(Role role);

    /**
     * 更新角色，不更新权限码字段
     *
     * @param role 角色对象
     * @return 更新条数
     */
    Role updateRoleExceptPermissionCode(Role role);

    /**
     * 更新角色的权限码
     *
     * @param roleId         角色Id
     * @param permissionCode 权限码
     * @return 更新数据条数
     */
    int updateRolePermissionCode(Integer roleId, String permissionCode);

    /**
     * 删除角色
     *
     * @param roleId 角色Id
     * @return 删除的角色对象
     */
    Role deleteRole(Integer roleId);

    /**
     * 分页查询
     *
     * @param example  查询条件
     * @param pageable 分页条件
     * @return 分页数据
     */
    GenericPage<Role> findRoleListPaged(Role example, Pageable pageable);

    /**
     * 根据Id查询角色
     *
     * @param roleId 角色Id
     * @return 角色对象
     */
    Role findRoleById(Integer roleId);

    /**
     * 根据Id列表查询角色列表
     *
     * @param roleIdList
     * @return
     */
    List<Role> findRolesByIdList(List<Integer> roleIdList);

    /**
     * 查询不在id列表中的角色
     *
     * @param roleIdList id列表，为Empty时返回所有角色
     * @return 不在指定id列表中的角色
     */
    List<Role> findRolesByIdNotInList(List<Integer> roleIdList);

    /**
     * 查询所有角色
     *
     * @return
     */
    List<Role> findAll();

    /**
     * 获取角色的权限码
     *
     * @param roleId 角色Id
     * @return
     */
    String findRolePermissionCode(Integer roleId);
}
