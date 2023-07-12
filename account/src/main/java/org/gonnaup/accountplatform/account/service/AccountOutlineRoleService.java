package org.gonnaup.accountplatform.account.service;

import org.gonnaup.accountplatform.account.domain.GenericPage;
import org.gonnaup.accountplatform.account.entity.AccountOutline;
import org.gonnaup.accountplatform.account.entity.AccountOutlineRole;
import org.gonnaup.accountplatform.account.entity.AccountOutlineRolePk;
import org.gonnaup.accountplatform.account.entity.Role;
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
     * 添加帐号-角色关联关系，并删除相关账号权限码缓存
     *
     * @param accountOutlineId 帐号id
     * @param roleId           角色id
     * @return 添加的关联对象
     */
    AccountOutlineRole addAccountOutlineRole(AccountOutlineRolePk accountOutlineRolePk);

    /**
     * 为账号批量添加角色，并删除相关账号权限码缓存
     *
     * @param accountId 账号Id
     * @param roleIds   角色列表
     * @return 批量添加的个数
     */
    int addAccountOutlineRoleList(Long accountId, List<Integer> roleIds);

    /**
     * 为角色批量关联账号，并删除相关账号权限码缓存
     *
     * @param roleId     角色Id
     * @param accountIds 账号列表
     * @return 批量添加的个数
     */
    int addRoleAccountOutlineList(Integer roleId, List<Long> accountIds);

    /**
     * 根据主键删除账户的一个关联角色，并删除相关账号权限码缓存
     *
     * @param accountOutlineRolePk primary key
     * @return 删除元素的个数
     */
    int deleteByPrimaryKey(AccountOutlineRolePk accountOutlineRolePk);

    /**
     * 批量删除帐号的关联角色列表，并删除相关账号权限码缓存
     *
     * @param accountId 帐号ID
     * @param roleIds   角色列表
     * @return 删除元素个数
     */
    int deleteAccountOutlineRoleList(Long accountId, List<Integer> roleIds);

    /**
     * 解除帐号的所有角色，并删除相关账号权限码缓存
     *
     * @param accountOutlineId 帐号Id
     * @return 删除角色个数
     */
    int deleteByAccountOutlineId(Long accountOutlineId);

    /**
     * 解除角色关联的帐号列表， 并删除相关账号权限码缓存
     *
     * @param roleId     角色Id
     * @param accountIds 帐号列表
     * @return 删除关联帐号个数
     */
    int deleteRoleAccountOutlineList(Integer roleId, List<Long> accountIds);

    /**
     * 删除某角色对应的所有帐号关联关系，并删除相关账号权限码缓存
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
    int countRolesByAccountOutlineId(Long accountOutlineId);

    /**
     * 查询帐号未关联的角色个数
     *
     * @param accountOutlineId
     * @return
     */
    int countRolesByNotAccountOutlineId(Long accountOutlineId);

    /**
     * 查询角色关联的帐号个数
     *
     * @param roleId 角色Id
     * @return
     */
    int countAccountOutlinesByRoleId(Integer roleId);

    /**
     * 查询角色未关联的账号个数
     *
     * @param roleId 角色Id
     * @return
     */
    int countAccountOutlinesByNotRoleId(Integer roleId);


    /**
     * 分页查询某角色关联的帐号列表，用作权限的批量操作
     *
     * @param roleId   角色Id
     * @param pageable 分页参数
     * @return 分页数据
     */
    GenericPage<AccountOutline> findAccountOutlinesByRoleIdPaged(Integer roleId, Pageable pageable);

    /**
     * 分页查询某角色未关联的帐号列表，用作权限的批量操作
     *
     * @param roleId   角色Id
     * @param pageable 分页参数
     * @return 分页数据
     */
    GenericPage<AccountOutline> findAccountOutlinesNotAttachRolePaged(Integer roleId, Pageable pageable);


    /**
     * 查询拥有某角色的帐号ID列表
     *
     * @param roleId 角色Id
     * @return
     */
    List<Long> findAccountOutlineIdListByRoleId(Integer roleId);

    /**
     * 查询帐号的所有角色列表
     *
     * @param accountId 帐号Id
     * @return 角色列表
     */
    List<Role> findRolesByAccountId(Long accountId);

    /**
     * 查询账号未拥有的角色列表
     *
     * @param accountId
     * @return
     */
    List<Role> findRolesNotAttachAccount(Long accountId);

}
