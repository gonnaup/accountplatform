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
     * 添加角色并关联权限列表
     *
     * @param role          角色对象
     * @param permissionIds 要关联的权限ID列表
     * @return 成功关联的数量
     */
    int addRoleAndAttachPermissions(Role role, List<Integer> permissionIds);

    /**
     * 更新角色
     *
     * @param role 角色对象
     * @return 更新条数
     */
    int updateRole(Role role);

    /**
     * 删除角色
     *
     * @param roleId
     * @return
     */
    int deleteRole(Integer roleId);

    /**
     * 分页查询
     *
     * @param example  查询条件
     * @param pageable 分页条件
     * @return 分页数据
     */
    GenericPage<Role> findRoleListPaged(Role example, Pageable pageable);

    /**
     * 获取角色的权限码
     *
     * @param roleId 角色Id
     * @return
     */
    String findRolePermissionCode(Integer roleId);
}
