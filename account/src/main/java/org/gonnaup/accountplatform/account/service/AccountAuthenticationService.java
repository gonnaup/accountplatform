package org.gonnaup.accountplatform.account.service;

import org.gonnaup.accountplatform.account.entity.AccountAuthentication;

/**
 * 账户认证服务接口
 *
 * @author gonnaup
 * @version created at 2023/7/5 下午5:23
 */

public interface AccountAuthenticationService {

    /**
     * 为账户添加认证密码，帐号必须已存在
     *
     * @param accountAuthentication 新的认证方式
     * @return 添加的认证对象
     */
    AccountAuthentication addAccountAuthentication(AccountAuthentication accountAuthentication);

    /**
     * 更新账号的认证密码
     *
     * @param accountAuthentication
     * @return 更新后的认证对象
     */
    AccountAuthentication updateAccountAuthentication(AccountAuthentication accountAuthentication);

    /**
     * 根据Id删除认证信息
     *
     * @param accountId 认证id
     * @return 删除数据数
     */
    int deleteAccountAuthenticationById(Long accountId);


    /**
     * 查询某个帐号的所有认证信息
     *
     * @param accountId 帐号Id
     * @return 认证列表
     */
    AccountAuthentication findByAccountId(Long accountId);

}
