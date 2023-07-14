package org.gonnaup.accountplatform.account.service.impl;

import org.gonnaup.accountplatform.account.entity.AccountAuthentication;
import org.gonnaup.accountplatform.account.entity.AccountOutline;
import org.gonnaup.accountplatform.account.exception.RecordNotExistException;
import org.gonnaup.accountplatform.account.repository.AccountAuthenticationRepository;
import org.gonnaup.accountplatform.account.service.AccountAuthenticationService;
import org.gonnaup.accountplatform.account.service.AccountOutlineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 账号认证服务接口实现类
 *
 * @author gonnaup
 * @version created at 2023/7/14 13:36
 */
@Service
public class AccountAuthenticationSerivceImpl implements AccountAuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AccountAuthenticationSerivceImpl.class);

    private final AccountAuthenticationRepository accountAuthenticationRepository;

    private final AccountOutlineService accountOutlineService;

    public AccountAuthenticationSerivceImpl(AccountAuthenticationRepository accountAuthenticationRepository, AccountOutlineService accountOutlineService) {
        this.accountAuthenticationRepository = accountAuthenticationRepository;
        this.accountOutlineService = accountOutlineService;
    }

    /**
     * 为账户添加认证密码，帐号必须已存在
     *
     * @param accountAuthentication 新的认证方式
     * @return 添加的认证对象
     */
    @Override
    @Transactional
    public AccountAuthentication addAccountAuthentication(AccountAuthentication accountAuthentication) {
        Long accountId = accountAuthentication.getAccountId();
        logger.info("为账号ID={}添加认证密码", accountId);
        AccountOutline account = accountOutlineService.findAccountOutlineByAccountId(accountId);
        if (account == null) {
            logger.error("为账号添加认证密码时，账号ID={}不存在!", accountId);
            throw new RecordNotExistException("error.accountAuthentication.add.notexist.account." + accountId);
        }
        LocalDateTime now = LocalDateTime.now();
        accountAuthentication.setCreateTime(now);
        accountAuthentication.setUpdateTime(now);
        return accountAuthenticationRepository.save(accountAuthentication);
    }

    /**
     * 更新账号的认证密码
     *
     * @param accountAuthentication
     * @return 更新后的认证对象
     */
    @Override
    @Transactional
    public AccountAuthentication updateAccountAuthentication(AccountAuthentication accountAuthentication) {
        Long accountId = accountAuthentication.getAccountId();
        logger.info("为账号ID={}更新认证密码", accountId);
        AccountOutline account = accountOutlineService.findAccountOutlineByAccountId(accountId);
        if (account == null) {
            logger.error("为账号添加认证密码时，账号ID={}不存在!", accountId);
            throw new RecordNotExistException("error.accountAuthentication.add.notexist.account." + accountId);
        }
        LocalDateTime now = LocalDateTime.now();
        accountAuthentication.setUpdateTime(now);
        return accountAuthenticationRepository.save(accountAuthentication);
    }

    /**
     * 根据Id删除认证信息
     *
     * @param id 认证id
     * @return 删除数据数
     */
    @Override
    @Transactional
    public int deleteAccountAuthenticationById(Long accountId) {
        accountAuthenticationRepository.deleteById(accountId);
        return 1;
    }

    /**
     * 查询某个帐号的认证密码
     *
     * @param accountId 帐号Id
     * @return 认证密码对象，如果不存在返回null
     */
    @Override
    public AccountAuthentication findByAccountId(Long accountId) {
        return accountAuthenticationRepository.findById(accountId).orElse(null);
    }
}
