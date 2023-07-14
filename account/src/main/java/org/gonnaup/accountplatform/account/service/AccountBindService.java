package org.gonnaup.accountplatform.account.service;

import org.gonnaup.accountplatform.account.constant.BindType;
import org.gonnaup.accountplatform.account.entity.AccountBind;

import java.util.List;

/**
 * 帐号绑定凭证服务接口
 *
 * @author gonnaup
 * @version created at 2023/7/14 下午10:27
 */
public interface AccountBindService {

    /**
     * 添加帐号绑定对象
     *
     * @param accountBind 绑定对象
     * @return 新增的绑定对象
     */
    AccountBind addAccountBind(AccountBind accountBind);

    /**
     * 更新帐号绑定对象，只更新<code>principal,token</code>两个字段，必须提供ID
     *
     * @param accountBind 要更新的绑定对象
     * @return 更新后的帐号绑定对象
     */
    AccountBind updateAccountBind(AccountBind accountBind);

    /**
     * 删除对象绑定对象
     *
     * @param bindId 绑定对象Id
     * @return 删除的绑定对象
     */
    AccountBind deleteAccountBindById(Long bindId);

    /**
     * 查询帐号所有绑定的凭证
     *
     * @param accountId 帐号Id
     * @return 帐号绑定列表
     */
    List<AccountBind> findAccountBindByAccountId(Long accountId);

    /**
     * 根据绑定类型和凭证主体查询绑定对象
     *
     * @param bindType  绑定类型
     * @param principal 凭证主体
     * @return 绑定对象，如果查询不到则返回null
     */
    AccountBind findAccountBindByBindTypeAndPrincipal(BindType bindType, String principal);

}
