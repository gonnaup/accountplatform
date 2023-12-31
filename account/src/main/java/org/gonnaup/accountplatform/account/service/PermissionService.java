package org.gonnaup.accountplatform.account.service;

import org.gonnaup.accountplatform.account.domain.GenericPage;
import org.gonnaup.accountplatform.account.entity.Permission;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 权限服务接口
 *
 * @author gonnaup
 * @version created at 2023/7/2 下午12:16
 */
public interface PermissionService {

    /**
     * 添加权限
     * <p>
     * 权限位为null时自动填充
     *
     * @param permission
     * @return 添加的权限对象
     */
    Permission addPermission(Permission permission);

    /**
     * 更新权限对象的非权限部分字段
     * <p>
     * permissionName, permissionLocalName, resources, description
     *
     * @param permission 要更新的权限对象
     * @return 更新后的权限对象
     */
    Permission updatePermission(Permission permission);

    /**
     * 删除权限
     *
     * @param permissionId 权限Id
     * @return 被删除的权限，删除失败返回null
     */
    Permission deletePermission(Integer permissionId);

    /**
     * 获取下一个权限位
     *
     * @return 下一个权限位
     */
    int generateNextPermissionLocation();

    /**
     * 根据ID查询权限对象
     *
     * @param permissionId 权限ID
     * @return 权限对象
     */
    Permission findPermissionById(Integer permissionId);

    /**
     * 根据Id列表查询权限列表
     *
     * @param permissionIds 权限Id列表
     * @return 权限列表
     */
    List<Permission> findPermissionsByIdList(List<Integer> permissionIds);

    /**
     * 查询Id不在权限id列表中的权限列表
     *
     * @param permissionIds id list，为Empty时返回所有权限
     * @return 不在permissionIds中的权限列表
     */
    List<Permission> findPermissionsByIdNotInLIst(List<Integer> permissionIds);

    /**
     * 获取所有权限列表
     *
     * @return 所有权限列表
     */
    List<Permission> findAll();

    /**
     * 分页查询
     *
     * @param example  查询条件
     * @param pageable 分页条件
     * @return 查询页
     */
    GenericPage<Permission> findPermissionPaged(Permission example, Pageable pageable);

    /**
     * 获取权限码
     *
     * @param permissionId 权限Id
     * @return 权限码
     */
    String findPermissionCode(Integer permissionId);

}
