package org.gonnaup.accountplatform.account.service.impl;

import org.gonnaup.accountplatform.account.domain.GenericPage;
import org.gonnaup.accountplatform.account.entity.AccountOutline;
import org.gonnaup.accountplatform.account.entity.AccountOutlineRole;
import org.gonnaup.accountplatform.account.entity.AccountOutlineRolePk;
import org.gonnaup.accountplatform.account.entity.Role;
import org.gonnaup.accountplatform.account.service.AccountOutlineRoleService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 账号角色关联关系服务接口实现类
 *
 * @author gonnaup
 * @version created at 2023/7/11 21:51
 */
@Service
public class AccountOutlineRoleServiceImpl implements AccountOutlineRoleService {


    /**
     * 添加帐号-角色关联关系，并删除相关账号权限码缓存
     *
     * @param accountOutlineRolePk@return 添加的关联对象
     */
    @Override
    public AccountOutlineRole addAccountOutlineRole(AccountOutlineRolePk accountOutlineRolePk) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 为账号批量添加角色，并删除相关账号权限码缓存
     *
     * @param accountId 账号Id
     * @param roleIds   角色列表
     * @return 批量添加的个数
     */
    @Override
    public int addAccountOutlineRoleList(Long accountId, List<Integer> roleIds) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * 为角色批量关联账号，并删除相关账号权限码缓存
     *
     * @param roleId     角色Id
     * @param accountIds 账号列表
     * @return 批量添加的个数
     */
    @Override
    public int addRoleAccountOutlineList(Integer roleId, List<Long> accountIds) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * 根据主键删除账户的一个关联角色，并删除相关账号权限码缓存
     *
     * @param accountOutlineRolePk primary key
     * @return 删除元素的个数
     */
    @Override
    public int deleteByPrimaryKey(AccountOutlineRolePk accountOutlineRolePk) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * 批量删除帐号的关联角色，并删除相关账号权限码缓存
     *
     * @param accountOutlineRolePkList
     * @return 删除元素个数
     */
    @Override
    public int deleteByPrimaryKeyList(List<AccountOutlineRolePk> accountOutlineRolePkList) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * 解除帐号的所有角色，并删除相关账号权限码缓存
     *
     * @param accountOutlineId 帐号Id
     * @return 删除角色个数
     */
    @Override
    public int deleteByAccountOutlineId(Long accountOutlineId) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * 删除某角色对应的所有帐号关联关系，并删除相关账号权限码缓存
     *
     * @param roleId 角色Id
     * @return 删除关联关系个数
     */
    @Override
    public int deleteByRoleId(Integer roleId) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * 查询帐号关联的角色个数
     *
     * @param accountOutlineId
     * @return
     */
    @Override
    public int countByAccountOutlineId(Long accountOutlineId) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * 查询角色关联的帐号个数
     *
     * @param roleId 角色Id
     * @return
     */
    @Override
    public int countByRoleId(Integer roleId) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * 查询角色未关联的账号个数
     *
     * @param roleId 角色Id
     * @return
     */
    @Override
    public int countByNotRoleId(Integer roleId) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * 分页查询某角色关联的帐号列表，用作权限的批量操作
     *
     * @param roleId   角色Id
     * @param pageable 分页参数
     * @return 分页数据
     */
    @Override
    public GenericPage<AccountOutline> findAccountOutlinesByRoleIdPaged(Integer roleId, Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 分页查询某角色未关联的帐号列表，用作权限的批量操作
     *
     * @param roleId   角色Id
     * @param pageable 分页参数
     * @return 分页数据
     */
    @Override
    public GenericPage<AccountOutline> findAccountOutlinesNotAttachRolePaged(Integer roleId, Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 查询某角色与帐号的所有关联关系
     *
     * @param roleId 角色Id
     * @return 角色-帐号关联关系列表
     */
    @Override
    public List<AccountOutlineRole> findByRoleId(Integer roleId) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 查询帐号的所有角色列表
     *
     * @param accountId 帐号Id
     * @return 角色列表
     */
    @Override
    public List<Role> findRolesByAccountId(Long accountId) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 查询账号未拥有的角色列表
     *
     * @param accountId
     * @return
     */
    @Override
    public List<Role> findRolesNotAttachAccount(Long accountId) {
        // TODO Auto-generated method stub
        return null;
    }
}
