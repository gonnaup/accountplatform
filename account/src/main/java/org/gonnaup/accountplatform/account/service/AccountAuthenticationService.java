package org.gonnaup.accountplatform.account.service;

import org.gonnaup.accountplatform.account.constant.AuthenticationType;
import org.gonnaup.accountplatform.account.entity.AccountAuthentication;
import org.gonnaup.accountplatform.account.entity.AccountOutline;

import java.util.List;

/**
 * 账户认真服务接口
 *
 * @author gonnaup
 * @version created at 2023/7/5 下午5:23
 */

public interface AccountAuthenticationService {

    /**
     * 为账户添加一种认证方式，如绑定邮箱，微信等，帐号必须已存在
     *
     * @param accountAuthentication 新的认证方式
     * @return 添加的认证对象
     */
    AccountAuthentication addAccountAuthentication(AccountAuthentication accountAuthentication);

    /**
     * 注册新的用户并添加用户的认证方式
     *
     * @param accountAuthentication 认证对象
     * @return 生成的帐号概要信息
     */
    AccountOutline registeAccountAuthentication(AccountAuthentication accountAuthentication);

    /**
     * 更新认证信息
     *
     * @param accountAuthentication 更新的认证信息
     * @return 更新数量
     */
    int updateAccountAuthentication(AccountAuthentication accountAuthentication);

    /**
     * 根据Id删除认证信息
     *
     * @param id 认证id
     * @return 删除数据数
     */
    int deleteAccountAuthenticationById(Long id);

    /**
     * 判断某类型的认证主体是否已经存在
     *
     * @param type      认证类型
     * @param principal 主体名
     * @return 如果存在返回true, 不存在返回false
     */
    boolean principalExist(AuthenticationType type, String principal);

    /**
     * 根据认证类型和认证主体查询认证信息
     *
     * @param type      认证类型
     * @param principal 主体
     * @return 认证信息
     */
    AccountAuthentication findByPrincipal(AuthenticationType type, String principal);

    /**
     * 查询某个帐号的所有认证信息
     *
     * @param accountId 帐号Id
     * @return 认证列表
     */
    List<AccountAuthentication> findByAccountId(Long accountId);

    /**
     * 查询某个帐号特定认证方式的认证信息
     *
     * @param accountId 帐号Id
     * @param type      认证类型
     * @return 认证列表
     */
    List<AccountAuthentication> findByAccountIdAndAuthenticationType(Long accountId, AuthenticationType type);

}
