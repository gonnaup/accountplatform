package org.gonnaup.accountplatform.account.service;

import org.gonnaup.accountplatform.account.domain.GenericPage;
import org.gonnaup.accountplatform.account.entity.AccountOutline;
import org.gonnaup.accountplatform.account.entity.AccountOutlineRole;
import org.gonnaup.accountplatform.account.entity.AccountOutlineRolePk;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 帐号概述实体与角色关联关系服务接口，主要用来控制用户权限
 *
 * @author gonnaup
 * @version created at 2023/7/4 下午10:08
 */
public interface AccountOutlineRoleService {

    /**
     * 添加帐号-角色关联关系，如果有权限缓存，需删除关联缓存
     *
     * @param accountOutlineId 帐号id
     * @param roleId           角色id
     * @return 添加的关联对象
     */
    AccountOutlineRole addAccountOutlineRole(AccountOutlineRolePk accountOutlineRolePk);

    /**
     * 批量添加帐号-角色关联关系，如果有权限缓存，需删除关联缓存
     *
     * @param accountOutlineRolePkList 帐号id-角色id列表
     * @return 批量添加的个数
     */
    int addAccountOutlineRoleList(List<AccountOutlineRolePk> accountOutlineRolePkList);

    /**
     * 根据主键删除账户的一个关联角色，如果有权限缓存，需删除关联缓存
     *
     * @param accountOutlineRolePk primary key
     * @return 删除元素的个数
     */
    int deleteByPrimaryKey(AccountOutlineRolePk accountOutlineRolePk);

    /**
     * 批量删除帐号的关联角色，如果有权限缓存，需删除关联缓存
     *
     * @param accountOutlineRolePkList
     * @return 删除元素个数
     */
    int deleteByPrimaryKeyList(List<AccountOutlineRolePk> accountOutlineRolePkList);

    /**
     * 解除帐号的所有角色，如果有权限缓存，需删除关联缓存
     *
     * @param accountOutlineId 帐号Id
     * @return 删除角色个数
     */
    int deleteByAccountOutlineId(Long accountOutlineId);

    /**
     * 删除某角色对应的所有帐号关联关系，如果有权限缓存，需删除关联缓存
     *
     * @param roleId 角色Id
     * @return 删除关联关系个数
     */
    int deleteByRoleId(Integer roleId);

    /**
     * 查询帐号关联的角色个数
     *
     * @param accountOutlineId
     * @return
     */
    int countByAccountOutlineId(Long accountOutlineId);

    /**
     * 查询角色关联的帐号个数
     *
     * @param roleId
     * @return
     */
    int countByRoleId(Integer roleId);


    /**
     * 分页查询某角色关联的帐号列表，用作权限的批量操作
     *
     * @param roleId   角色Id
     * @param pageable 分页参数
     * @return 分页数据
     */
    GenericPage<AccountOutline> findAccountOutlinesByRoleIdPaged(Integer roleId, Pageable pageable);


}
