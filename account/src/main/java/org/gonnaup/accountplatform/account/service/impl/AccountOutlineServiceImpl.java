package org.gonnaup.accountplatform.account.service.impl;

import org.gonnaup.accountplatform.account.config.AccountProperties;
import org.gonnaup.accountplatform.account.constant.AccountState;
import org.gonnaup.accountplatform.account.constant.Gender;
import org.gonnaup.accountplatform.account.domain.GenericPage;
import org.gonnaup.accountplatform.account.entity.AccountOutline;
import org.gonnaup.accountplatform.account.entity.Role;
import org.gonnaup.accountplatform.account.exception.RecordNotExistException;
import org.gonnaup.accountplatform.account.repository.AccountOutlineRepository;
import org.gonnaup.accountplatform.account.repository.AccountOutlineRoleRepository;
import org.gonnaup.accountplatform.account.service.AccountOutlineService;
import org.gonnaup.accountplatform.account.service.IdentifyGenerateService;
import org.gonnaup.accountplatform.account.service.RoleService;
import org.gonnaup.accountplatform.account.util.AuthUtil;
import org.gonnaup.common.util.RandomUtil;
import org.gonnaup.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author gonnaup
 * @version created at 2023/6/30 上午12:05
 */
@Service
@CacheConfig(cacheNames = {"cache::accountOutline"})
public class AccountOutlineServiceImpl implements AccountOutlineService {

    private static final Logger logger = LoggerFactory.getLogger(AccountOutlineServiceImpl.class);

    private final AccountProperties accountProperties;

    private final IdentifyGenerateService identifyGenerateService;

    private final AccountOutlineRepository accountOutlineRepository;

    //防止循环依赖引入repository
    private final AccountOutlineRoleRepository accountOutlineRoleRepository;

    private final RoleService roleService;

    @Autowired
    public AccountOutlineServiceImpl(AccountProperties accountProperties, IdentifyGenerateService identifyGenerateService, AccountOutlineRepository accountOutlineRepository,
                                     AccountOutlineRoleRepository accountOutlineRoleRepository, RoleService roleService) {
        this.accountProperties = accountProperties;
        this.identifyGenerateService = identifyGenerateService;
        this.accountOutlineRepository = accountOutlineRepository;
        this.accountOutlineRoleRepository = accountOutlineRoleRepository;
        this.roleService = roleService;
    }

    /**
     * 添加帐号，注册帐号时使用，需提前设置好非空字段的默认值
     *
     * @param accountOutline
     * @return
     */
    @Override
    @Transactional
    public AccountOutline addAccountOutline(AccountOutline accountOutline) {
        accountOutline.setId(identifyGenerateService.generateAccountId());
        fillRequiredFiled(accountOutline);
        accountOutline = accountOutlineRepository.save(accountOutline);
        logger.info("添加帐号概要信息 {} 成功", accountOutline);
        return accountOutline;
    }

    /**
     * 更新帐号名
     *
     * @param accountOutlineId 帐号Id
     * @param accountName      新账户名
     * @return 更新帐号数
     */
    @Override
    @Transactional
    public int updateAccountName(Long id, String accountName) {
        if (StringUtil.isNullOrBlank(accountName)) {
            logger.error("更新账户ID={}的账户名时，参数账户名为空", id);
            throw new IllegalArgumentException("更新账户名时账户名不能为空");
        }
        logger.info("更新账户ID={}的账户名为{}", id, accountName);
        return accountOutlineRepository.updateAccountName(id, accountName, LocalDateTime.now());
    }

    /**
     * 更新账户状态
     *
     * @param id    帐号Id
     * @param state 新状态
     * @return 更新条数
     */
    @Override
    @Transactional
    public int updateState(Long id, AccountState state) {
        logger.info("更新帐号ID={}的状态={}", id, state.label);
        return accountOutlineRepository.updateAccountState(id, state.value, LocalDateTime.now());
    }

    /**
     * 更新账户头像url
     *
     * @param id  账户id
     * @param url 头像url
     * @return 更新条数
     */
    @Override
    @Transactional
    public int updateAvatarUrl(Long id, String url) {
        logger.info("更新帐号ID={}的头像url={}", id, url);
        return accountOutlineRepository.updateAvatarUrl(id, url, LocalDateTime.now());
    }

    /**
     * 更新简要信息，nickName, gender, region, personalSignature
     *
     * @param accountOutline 更新对象
     * @return
     */
    @Override
    @Transactional
    public AccountOutline updateAccountOutlineBrief(AccountOutline accountOutline) {
        Long id = accountOutline.getId();
        Optional<AccountOutline> oa = accountOutlineRepository.findById(id);
        if (oa.isEmpty()) {
            logger.error("更新账户简要信息时账户ID={}不存在", id);
            throw new RecordNotExistException("error.accountOutline.update.notexist." + id);
        }
        logger.info("开始更新账户Id={}的简要信息", id);
        AccountOutline aol = oa.get();
        if (logger.isDebugEnabled()) {
            logger.debug("更新前账户对象 {}", aol);
            logger.debug("更新参数对象 {}", accountOutline);
        }
        StringUtil.acceptWhenNotBlank(aol::setNickName, accountOutline::getNickName);
        StringUtil.acceptWhenNotBlank(s -> aol.setGender(Gender.fromValue(accountOutline.getGender()).value), accountOutline::getGender);
        StringUtil.acceptWhenNotBlank(aol::setRegion, accountOutline::getRegion);
        StringUtil.acceptWhenNotBlank(aol::setPersonalSignature, accountOutline::getPersonalSignature);
        aol.setUpdateTimeToNow();
        accountOutlineRepository.save(aol);
        if (logger.isDebugEnabled()) {
            logger.debug("更新后账户对象 {}", aol);
        }
        return aol;
    }

    /**
     * 禁用帐号
     *
     * @param id 帐号Id
     * @return 禁用帐号数
     */
    @Override
    @Transactional
    public int disableAccount(Long id) {
        logger.info("禁用帐号ID={}", id);
        return accountOutlineRepository.updateAccountState(id, AccountState.Disabled.value, LocalDateTime.now());
    }

    /**
     * 将帐号置于移除状态
     *
     * @param id 帐号Id
     * @return 移除帐号数
     */
    @Override
    @Transactional
    public int removeAccount(Long id) {
        logger.info("移除账户ID={}", id);
        return accountOutlineRepository.updateAccountState(id, AccountState.Removed.value, LocalDateTime.now());
    }

    /**
     * 分页查询
     *
     * @param example  查询条件
     * @param pageable 分页条件
     * @return 分页数据
     */
    @Override
    public GenericPage<AccountOutline> findAccountOutlineListPaged(AccountOutline example, Pageable pageable) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("accountName", matcher -> matcher.contains())
                .withMatcher("nickName", matcher -> matcher.contains())
                .withIgnorePaths("avatarUrl", "personalSignature");
        return GenericPage.fromPage(accountOutlineRepository.findAll(Example.of(example, exampleMatcher), pageable));
    }

    /**
     * 根据Id查询帐号概要信息
     *
     * @param id 帐号Id
     * @return 帐号概要信息
     */
    @Override
    public AccountOutline findAccountOutlineByAccountId(Long id) {
        return accountOutlineRepository.findById(id).orElseThrow(() -> {
            logger.error("帐号ID={} 不存在", id);
            return new RecordNotExistException("error.accountOutline.search.notexist.id." + id);
        });
    }

    /**
     * 根据帐号名查询帐号概要信息
     *
     * @param accountName 帐号名
     * @return 帐号概要信息
     */
    @Override
    public AccountOutline findAccountOutlineByAccountName(String accountName) {
        return accountOutlineRepository.findByAccountName(accountName).orElseThrow(() -> {
            logger.error("帐号Name={} 不存在", accountName);
            return new RecordNotExistException("error.accountOutline.search.notexist.accountName." + accountName);
        });
    }

    /**
     * 查询账户名是否已经存在
     *
     * @param accountName 账户名
     * @return 如果已经存在返回true，不存在返回false
     */
    @Override
    public boolean accountNameExist(String accountName) {
        return accountOutlineRepository.countByAccountName(accountName) > 0;
    }

    /**
     * 统计Id不在accountIdList中的账号个数
     *
     * @param accountIdList
     * @return 账号个数
     */
    @Override
    public int countByIdNotIn(List<Long> accountIdList) {
        return accountIdList.isEmpty() ? (int) accountOutlineRepository.count() : accountOutlineRepository.countByIdNotIn(accountIdList);
    }

    /**
     * 分页查询账号Id不在accountIdList中的账号列表
     *
     * @param accountIdList
     * @param pageable
     * @return
     */
    @Override
    public GenericPage<AccountOutline> findByIdNotIn(List<Long> accountIdList, Pageable pageable) {
        Page<AccountOutline> page = null;
        if (accountIdList.isEmpty()) {
            page = accountOutlineRepository.findAll(pageable);
        } else {
            page = accountOutlineRepository.findByIdNotIn(accountIdList, pageable);
        }
        return GenericPage.fromPage(page);
    }

    /**
     * 分页查询账号Id在accountIdList中的账号列表
     *
     * @param accountIdList
     * @param pageable
     * @return
     */
    @Override
    public GenericPage<AccountOutline> findByIdIn(List<Long> accountIdList, Pageable pageable) {
        return GenericPage.fromPage(accountOutlineRepository.findByIdIn(accountIdList, pageable));
    }

    /**
     * 计算帐号的权限码，带缓存
     *
     * @param id 帐号Id
     * @return 权限码
     */
    @Override
    @Cacheable(key = "'permissionCode::'+#id", unless = "#result == null")
    public String calculatePermissionCode(Long id) {
        logger.info("开始计算帐号ID={} 的权限码", id);
        if (accountOutlineRepository.findById(id).isEmpty()) {
            logger.error("要计算权限码的帐号ID={} 不存在", id);
            throw new RecordNotExistException("error.accountOutline.search.notexist.id." + id);
        }
//        List<Role> roles = accountOutlineRoleService.findRolesByAccountId(id);
        List<Integer> roleIdList = accountOutlineRoleRepository.findRoleIdListByAccountOutlineId(id);
        List<Role> roles = roleService.findRolesByIdList(roleIdList);
        if (logger.isDebugEnabled()) {
            logger.debug("查询到帐号ID={} 共有{}个角色，分别为{}", id, roles.size(), roles);
        }
        if (roles.isEmpty()) {
            logger.info("帐号ID={} 没有分配角色，返回权限码\"{}\"", id, AuthUtil.ZERO_P_CODE);
            return AuthUtil.ZERO_P_CODE;
        }
        List<String> permissionCodeList = roles.stream().map(Role::getPermissionCode).toList();
        String code = AuthUtil.mergePermissionChainList(permissionCodeList);
        logger.info("计算账号ID={}的权限码为{}", id, code);
        return code;
    }

    /**
     * 清除账户权限码缓存
     *
     * @param id 帐号Id
     */
    @Override
    @CacheEvict(key = "'permissionCode::'+#id")
    public void clearPermissionCodeCache(Long id) {
        logger.info("成功清除帐号ID={}的权限码缓存", id);
    }


    private void fillRequiredFiled(AccountOutline accountOutline) {
        accountOutline.setBothTimeToNow();
        if (StringUtil.isNullOrBlank(accountOutline.getAccountName())) {
            String accountName = null;
            while (accountOutlineRepository.findByAccountName((accountName =
                            RandomUtil.randomStringWithPrefix(accountProperties.getAccountName().getLength(), accountProperties.getAccountName().getPrefix())))
                    .isPresent()) {
                logger.info("生成的默认账户名 {} 已存在，重新生成账户名", accountName);
            }
            logger.info("生成默认账户名 {}", accountName);
            accountOutline.setAccountName(accountName);
        }
        StringUtil.acceptWhenNullOrBlank(accountOutline::setNickName, accountOutline.getAccountName(), accountOutline::getNickName);
        StringUtil.acceptWhenNullOrBlank(accountOutline::setGender, Gender.Privacy.value, accountOutline::getGender);
        StringUtil.acceptWhenNullOrBlank(accountOutline::setAvatarUrl, accountProperties.getAvatar().defaultAvatarUrl(), accountOutline::getAvatarUrl);
        StringUtil.acceptWhenNullOrBlank(accountOutline::setState, AccountState.Normal.value, accountOutline::getState);
    }

}
