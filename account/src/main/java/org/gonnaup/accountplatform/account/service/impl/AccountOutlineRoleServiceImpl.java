package org.gonnaup.accountplatform.account.service.impl;

import org.gonnaup.accountplatform.account.domain.GenericPage;
import org.gonnaup.accountplatform.account.entity.AccountOutline;
import org.gonnaup.accountplatform.account.entity.AccountOutlineRole;
import org.gonnaup.accountplatform.account.entity.AccountOutlineRolePk;
import org.gonnaup.accountplatform.account.entity.Role;
import org.gonnaup.accountplatform.account.exception.RecordNotExistException;
import org.gonnaup.accountplatform.account.repository.AccountOutlineRoleRepository;
import org.gonnaup.accountplatform.account.service.AccountOutlineRoleService;
import org.gonnaup.accountplatform.account.service.AccountOutlineService;
import org.gonnaup.accountplatform.account.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 账号角色关联关系服务接口实现类
 *
 * @author gonnaup
 * @version created at 2023/7/11 21:51
 */
@Service
public class AccountOutlineRoleServiceImpl implements AccountOutlineRoleService {

    private static final Logger logger = LoggerFactory.getLogger(AccountOutlineRoleServiceImpl.class);

    private final AccountOutlineRoleRepository accountOutlineRoleRepository;

    private final AccountOutlineService accountOutlineService;

    private final RoleService roleService;

    @Autowired
    public AccountOutlineRoleServiceImpl(AccountOutlineRoleRepository accountOutlineRoleRepository, AccountOutlineService accountOutlineService, RoleService roleService) {
        this.accountOutlineRoleRepository = accountOutlineRoleRepository;
        this.accountOutlineService = accountOutlineService;
        this.roleService = roleService;
    }

    /**
     * 添加帐号-角色关联关系，并删除相关账号权限码缓存
     *
     * @param accountOutlineRolePk@return 添加的关联对象
     */
    @Override
    @Transactional
    public AccountOutlineRole addAccountOutlineRole(AccountOutlineRolePk accountOutlineRolePk) {
        Long accountId = accountOutlineRolePk.getAccountOutlineId();
        Integer roleId = accountOutlineRolePk.getRoleId();
        logger.info("开始为帐号ID={}关联角色ID={}", accountId, roleId);
        AccountOutline account = accountOutlineService.findAccountOutlineByAccountId(accountId);
        if (account == null) {
            logger.error("为账号添加关联角色时，帐号ID={}不存在", accountId);
            throw new RecordNotExistException("error.accountOutlineRole.add.notexist.accountOutline." + accountId);
        }
        Role role = roleService.findRoleById(roleId);
        if (role == null) {
            logger.error("为账号添加关联角色时，角色ID={}不存在", roleId);
            throw new RecordNotExistException("error.accountOutlineRole.add.notexist.role." + roleId);
        }
        AccountOutlineRole accountOutlineRole = AccountOutlineRole.of(accountOutlineRolePk, account, role);
        AccountOutlineRole saved = accountOutlineRoleRepository.save(accountOutlineRole);
        logger.info("帐号ID={}关联角色ID={}成功", accountId, roleId);

        accountOutlineService.clearPermissionCodeCache(accountId);
        return saved;
    }

    /**
     * 为账号批量添加角色，并删除相关账号权限码缓存
     *
     * @param accountId 账号Id
     * @param roleIds   角色列表
     * @return 批量添加的个数
     */
    @Override
    @Transactional
    public int addAccountOutlineRoleList(Long accountId, List<Integer> roleIds) {
        logger.info("开始为帐号ID={}批量关联角色ID={}", accountId, roleIds);
        if (roleIds.isEmpty()) {
            logger.error("为帐号ID={}批量关联角色时，角色列表为空", accountId);
            throw new RecordNotExistException("error.accountOutlineRole.add.notexist.roleList");
        }
        AccountOutline account = accountOutlineService.findAccountOutlineByAccountId(accountId);
        if (account == null) {
            logger.error("为账号添加关联角色时，帐号ID={}不存在", accountId);
            throw new RecordNotExistException("error.accountOutlineRole.add.notexist.accountOutline." + accountId);
        }

        long count = roleIds.stream().map(roleId -> {
            Role role = roleService.findRoleById(roleId);
            if (role == null) {
                logger.error("为账号添加关联角色时，角色ID={}不存在", roleId);
                throw new RecordNotExistException("error.accountOutlineRole.add.role.notexist." + roleId);
            }
            AccountOutlineRole accountOutlineRole = AccountOutlineRole.of(account, role);
            return accountOutlineRoleRepository.save(accountOutlineRole);
        }).count();
        logger.info("总共为帐号关联{}个角色", count);

        accountOutlineService.clearPermissionCodeCache(accountId);
        return (int) count;
    }

    /**
     * 为角色批量关联账号，并删除相关账号权限码缓存
     *
     * @param roleId     角色Id
     * @param accountIds 账号列表
     * @return 批量添加的个数
     */
    @Override
    @Transactional
    public int addRoleAccountOutlineList(Integer roleId, List<Long> accountIds) {
        logger.info("开始为角色ID={}批量关联帐号{}", roleId, accountIds);
        if (roleService.findRoleById(roleId) == null) {
            logger.error("为角色批量关联帐号时，角色不存在");
            throw new RecordNotExistException("error.accountOutlineRole.del.notexist.role." + roleId);
        }
        if (accountIds.isEmpty()) {
            logger.error("为角色ID={}批量关联帐号时，帐号列表为空", roleId);
            throw new RecordNotExistException("error.accountOutlineRole.del.notexist.accountList");
        }
        long count = accountIds.stream().map(accountId -> {
            AccountOutlineRolePk pk = AccountOutlineRolePk.of(accountId, roleId);
            return addAccountOutlineRole(pk);
        }).count();
        logger.info("总共为角色ID={}关联{}个帐号", roleId, count);
        return (int) count;
    }

    /**
     * 根据主键删除账户的一个关联角色，并删除相关账号权限码缓存
     *
     * @param accountOutlineRolePk primary key
     * @return 删除元素的个数
     */
    @Override
    @Transactional
    public int deleteByPrimaryKey(AccountOutlineRolePk accountOutlineRolePk) {
        Optional<AccountOutlineRole> oar = accountOutlineRoleRepository.findById(accountOutlineRolePk);
        if (oar.isEmpty()) {
            logger.error("要删除的帐号角色关联对象 {} 不存在", accountOutlineRolePk);
            throw new RecordNotExistException("error.accountOutlineRole.del.notexist.pk");
        }
        Long accountId = accountOutlineRolePk.getAccountOutlineId();
        Integer roleId = accountOutlineRolePk.getRoleId();
        logger.info("开始删除帐号ID={}的关联角色ID={}", accountId, roleId);
        accountOutlineRoleRepository.deleteById(accountOutlineRolePk);

        accountOutlineService.clearPermissionCodeCache(accountId);
        return 1;
    }

    /**
     * 批量删除帐号的关联角色列表，并删除相关账号权限码缓存
     *
     * @param accountId 帐号ID
     * @param roleIds   角色列表
     * @return 删除元素个数
     */
    @Override
    @Transactional
    public int deleteAccountOutlineRoleList(Long accountId, List<Integer> roleIds) {
        logger.info("开始删除帐号ID={}的关联角色ID={}", accountId, roleIds);
        if (roleIds.isEmpty()) {
            logger.warn("删除帐号ID={}关联角色时，角色列表为空", accountId);
            return 0;
        }
        AccountOutline account = accountOutlineService.findAccountOutlineByAccountId(accountId);
        if (account == null) {
            logger.error("为账号删除关联角色时，帐号ID={}不存在", accountId);
            throw new RecordNotExistException("error.accountOutlineRole.del.notexist.accountOutline." + accountId);
        }

        long count = roleIds.stream().map(roleId -> {
            Role role = roleService.findRoleById(roleId);
            if (role == null) {
                logger.error("为账号删除关联角色时，角色ID={}不存在", roleId);
                throw new RecordNotExistException("error.accountOutlineRole.del.notexist.role." + roleId);
            }
            AccountOutlineRolePk pk = AccountOutlineRolePk.of(accountId, roleId);
            accountOutlineRoleRepository.deleteById(pk);
            return pk;
        }).count();
        logger.info("总共为帐号删除{}个角色", count);

        accountOutlineService.clearPermissionCodeCache(accountId);
        return (int) count;
    }

    /**
     * 解除帐号的所有角色，并删除相关账号权限码缓存
     *
     * @param accountOutlineId 帐号Id
     * @return 删除角色个数
     */
    @Override
    @Transactional
    public int deleteByAccountOutlineId(Long accountOutlineId) {
        logger.info("删除帐号ID={}的所有关联角色", accountOutlineId);
        if (accountOutlineService.findAccountOutlineByAccountId(accountOutlineId) == null) {
            logger.error("删除帐号ID={}所有关联角色时，帐号不存在", accountOutlineId);
            throw new RecordNotExistException("error.accountOutlineRole.del.notexist.accountOutline." + accountOutlineId);
        }
        int count = accountOutlineRoleRepository.deleteByAccountOutlineId(accountOutlineId);

        accountOutlineService.clearPermissionCodeCache(accountOutlineId);
        return count;
    }

    /**
     * 解除角色关联的帐号列表， 并删除相关账号权限码缓存
     *
     * @param roleId     角色Id
     * @param accountIds 帐号列表
     * @return 删除关联帐号个数
     */
    @Override
    @Transactional
    public int deleteRoleAccountOutlineList(Integer roleId, List<Long> accountIds) {
        logger.info("开始删除角色ID={}的关联帐号列表{}", roleId, accountIds);
        if (accountIds.isEmpty() || roleService.findRoleById(roleId) == null) {
            logger.error("删除角色的关联帐号列表时，角色或帐号列表为空");
            throw new RecordNotExistException("error.accountOutlineRole.del.notexist");
        }
        long count = accountIds.stream().map(accountId -> {
            AccountOutlineRolePk pk = AccountOutlineRolePk.of(accountId, roleId);
            return deleteByPrimaryKey(pk);
        }).count();
        logger.info("共删除角色的{}个关联帐号列表", count);
        return (int) count;
    }

    /**
     * 删除某角色对应的所有帐号关联关系，并删除相关账号权限码缓存
     *
     * @param roleId 角色Id
     * @return 删除关联关系个数
     */
    @Override
    @Transactional
    public int deleteByRoleId(Integer roleId) {
        if (roleService.findRoleById(roleId) == null) {
            logger.error("删除角色ID={}的所有关联帐号时，角色不存在", roleId);
            throw new RecordNotExistException("error.accountOutlineRole.del.notexist.role." + roleId);
        }
        List<Long> accountIdList = accountOutlineRoleRepository.findAccountOutlineIdListByRoleId(roleId);

        int count = accountOutlineRoleRepository.deleteByRoleId(roleId);

        accountIdList.forEach(accountId ->
                accountOutlineService.clearPermissionCodeCache(accountId));

        logger.info("共删除角色-帐号关联关系{}对", count);
        return count;
    }

    /**
     * 查询帐号关联的角色个数
     *
     * @param accountOutlineId
     * @return
     */
    @Override
    public int countRolesByAccountOutlineId(Long accountOutlineId) {
        return accountOutlineRoleRepository.countRolesByAccountOutlineId(accountOutlineId);
    }

    /**
     * 查询帐号未关联的角色个数
     *
     * @param accountOutlineId
     * @return
     */
    @Override
    public int countRolesByNotAccountOutlineId(Long accountOutlineId) {
        return accountOutlineRoleRepository.countRoleByNotAccountOutlineId(accountOutlineId);
    }

    /**
     * 查询角色关联的帐号个数
     *
     * @param roleId 角色Id
     * @return
     */
    @Override
    public int countAccountOutlinesByRoleId(Integer roleId) {
        return accountOutlineRoleRepository.countAccountOutlinesByRoleId(roleId);
    }

    /**
     * 查询角色未关联的账号个数
     *
     * @param roleId 角色Id
     * @return
     */
    @Override
    public int countAccountOutlinesByNotRoleId(Integer roleId) {
        return accountOutlineRoleRepository.countAccountOutlinesByNotRoleId(roleId);
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
     * 查询拥有某角色的帐号ID列表
     *
     * @param roleId 角色Id
     * @return
     */
    @Override
    public List<Long> findAccountOutlineIdListByRoleId(Integer roleId) {
        return accountOutlineRoleRepository.findAccountOutlineIdListByRoleId(roleId);
    }

    /**
     * 查询帐号的所有角色列表
     *
     * @param accountId 帐号Id
     * @return 角色列表
     */
    @Override
    public List<Role> findRolesByAccountId(Long accountId) {
        List<Integer> roleIdList = accountOutlineRoleRepository.findRoleIdListByAccountOutlineId(accountId);
        return roleService.findRolesByIdList(roleIdList);
    }

    /**
     * 查询账号未拥有的角色列表
     *
     * @param accountId
     * @return
     */
    @Override
    public List<Role> findRolesNotAttachAccount(Long accountId) {
        List<Integer> roleIdList = accountOutlineRoleRepository.findRoleIdListByAccountOutlineIdIsNot(accountId);
        return roleService.findRolesByIdList(roleIdList);
    }
}
