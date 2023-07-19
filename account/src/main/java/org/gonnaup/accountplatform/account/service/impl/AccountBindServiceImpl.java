package org.gonnaup.accountplatform.account.service.impl;

import org.gonnaup.accountplatform.account.constant.BindType;
import org.gonnaup.accountplatform.account.entity.AccountBind;
import org.gonnaup.accountplatform.account.entity.AccountOutline;
import org.gonnaup.accountplatform.account.exception.RecordNotExistException;
import org.gonnaup.accountplatform.account.repository.AccountBindRepository;
import org.gonnaup.accountplatform.account.service.AccountBindService;
import org.gonnaup.accountplatform.account.service.AccountOutlineService;
import org.gonnaup.accountplatform.account.service.IdentifyGenerateService;
import org.gonnaup.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 帐号绑定对象服务接口实现类
 *
 * @author gonnaup
 * @version created at 2023/7/14 下午11:35
 */
@Service
public class AccountBindServiceImpl implements AccountBindService {

    private static final Logger logger = LoggerFactory.getLogger(AccountBindServiceImpl.class);

    private final AccountOutlineService accountOutlineService;

    private final IdentifyGenerateService identifyGenerateService;

    private final AccountBindRepository accountBindRepository;

    @Autowired
    public AccountBindServiceImpl(AccountOutlineService accountOutlineService, IdentifyGenerateService identifyGenerateService, AccountBindRepository accountBindRepository) {
        this.accountOutlineService = accountOutlineService;
        this.identifyGenerateService = identifyGenerateService;
        this.accountBindRepository = accountBindRepository;
    }

    /**
     * 添加帐号绑定对象
     *
     * @param accountBind 绑定对象
     * @return 新增的绑定对象
     */
    @Override
    @Transactional
    public AccountBind addAccountBind(AccountBind accountBind) {
        if (StringUtil.isNullOrBlank(accountBind.getPrincipal())) {
            logger.error("添加帐号绑定对象时，参数绑定主体不能为空");
            throw new IllegalArgumentException("error.accountBind.add.isEmpty.principal");
        }
        Long accountId = accountBind.getAccountId();
        //validate the bindType
        BindType.fromValue(accountBind.getBindType());
        AccountOutline account = accountOutlineService.findAccountOutlineByAccountId(accountId);
        if (account == null) {
            logger.error("给帐号添加绑定对象时，对象ID={}不存在", accountId);
            throw new RecordNotExistException("error.accountBind.add.notexist.account." + accountId);
        }
        accountBind.setId(identifyGenerateService.generateAccountId());
        accountBind.setBothTimeToNow();
        accountBind = accountBindRepository.save(accountBind);
        logger.info("给帐号ID={}添加绑定对象 {}", accountId, accountBind);
        return accountBind;
    }

    /**
     * 更新帐号绑定对象，只更新<code>principal,token</code>两个字段，必须提供ID
     *
     * @param accountBind 要更新的绑定对象
     * @return 更新后的帐号绑定对象
     */
    @Override
    @Transactional
    public AccountBind updateAccountBind(AccountBind accountBind) {
        if (StringUtil.isNullOrBlank(accountBind.getPrincipal())) {
            logger.error("更新帐号绑定对象时，参数绑定主体不能为空");
            throw new IllegalArgumentException("error.accountBind.update.isEmpty.principal");
        }
        Long id = accountBind.getId();
        Optional<AccountBind> oab = accountBindRepository.findById(id);
        if (oab.isEmpty()) {
            logger.error("更新帐号绑定对象时，绑定对象ID={}不存在", id);
            throw new RecordNotExistException("error.accountBind.update.notexist.accountBind." + id);
        }
        AccountBind bind = oab.get();
        bind.setUpdateTimeToNow();
        bind.setPrincipal(accountBind.getPrincipal());
        bind.setToken(accountBind.getToken());
        bind = accountBindRepository.save(bind);
        logger.info("更新帐号绑定对象ID={}成功", id);
        return bind;
    }

    /**
     * 删除帐号绑定对象
     *
     * @param bindId 绑定对象Id
     * @return 删除的绑定对象
     */
    @Override
    @Transactional
    public AccountBind deleteAccountBindById(Long bindId) {
        Optional<AccountBind> oab = accountBindRepository.findById(bindId);
        if (oab.isEmpty()) {
            logger.error("删除帐号绑定对象时，绑定对象ID={}不存在", bindId);
            throw new RecordNotExistException("error.accountBind.del.notexist.accountBind." + bindId);
        }
        accountBindRepository.deleteById(bindId);
        AccountBind bind = oab.get();
        logger.info("删除帐号绑定对象 {}", bind);
        return bind;
    }

    /**
     * 根据Id查询对象
     *
     * @param bindId id
     * @return AccountBind对象
     */
    @Override
    public AccountBind findAccountBindById(Long bindId) {
        return accountBindRepository.findById(bindId).orElse(null);
    }

    /**
     * 查询帐号所有绑定的凭证
     *
     * @param accountId 帐号Id
     * @return 帐号绑定列表
     */
    @Override
    public List<AccountBind> findAccountBindByAccountId(Long accountId) {
        return accountBindRepository.findByAccountId(accountId);
    }

    /**
     * 根据绑定类型和凭证主体查询绑定对象
     *
     * @param bindType  绑定类型
     * @param principal 凭证主体
     * @return 绑定对象，如果查询不到则返回null
     */
    @Override
    public AccountBind findAccountBindByBindTypeAndPrincipal(BindType bindType, String principal) {
        return accountBindRepository.findByBindTypeAndPrincipal(bindType.value, principal);
    }
}
