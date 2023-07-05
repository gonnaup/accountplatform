package org.gonnaup.accountplatform.account.service;

import org.gonnaup.accountplatform.account.constant.AccountState;
import org.gonnaup.accountplatform.account.domain.GenericPage;
import org.gonnaup.accountplatform.account.entity.AccountOutline;
import org.springframework.data.domain.Pageable;

/**
 * 帐号概要信息服务接口
 *
 * @author gonnaup
 * @version created at 2023/6/30 上午12:04
 */
public interface AccountOutlineService {
    /**
     * 添加帐号，注册帐号时使用
     *
     * @param accountOutline
     * @return
     */
    AccountOutline addAccountOutline(AccountOutline accountOutline);

    /**
     * 更新帐号名
     *
     * @param accountOutline
     * @return 更新帐号数
     */
    int updateAccountName(String accountName);

    /**
     * 更新账户状态
     *
     * @param accountId 帐号Id
     * @param state     新状态
     * @return 更新条数
     */
    int updateState(Long accountId, AccountState state);

    /**
     * 更新除 accountName, state字段外的其他信息
     *
     * @param accountOutline 更新对象
     * @return
     */
    AccountOutline updateAccountOutlineExeptAccountNameAndState(AccountOutline accountOutline);

    /**
     * 禁用帐号
     *
     * @param accountId 帐号Id
     * @return 禁用帐号数
     */
    int disableAccount(Long accountId);

    /**
     * 将帐号置于移除状态
     *
     * @param accountId 帐号Id
     * @return 移除帐号数
     */
    int removeAccount(Long accountId);

    /**
     * 分页查询
     *
     * @param example  查询条件
     * @param pageable 分页条件
     * @return 分页数据
     */
    GenericPage<AccountOutline> findAccountOutlineListPaged(AccountOutline example, Pageable pageable);

    /**
     * 根据Id查询帐号概要信息
     *
     * @param accountId 帐号Id
     * @return 帐号概要信息
     */
    AccountOutline findAccountOutlineByAccountId(Long accountId);

    /**
     * 根据帐号名查询帐号概要信息
     *
     * @param accountName 帐号名
     * @return 帐号概要信息
     */
    AccountOutline findAccountOutlineByAccountName(String accountName);

    /**
     * 计算帐号的权限码，带缓存
     *
     * @param accountId 帐号Id
     * @return 权限码
     */
    String calculatePermissionCode(Long accountId);

    /**
     * 清除账户权限码缓存
     *
     * @param accountId 帐号Id
     */
    void clearPermissionCodeCache(Long accountId);

}
