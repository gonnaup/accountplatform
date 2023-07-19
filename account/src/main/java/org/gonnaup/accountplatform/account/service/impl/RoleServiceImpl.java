package org.gonnaup.accountplatform.account.service.impl;

import org.gonnaup.accountplatform.account.domain.GenericPage;
import org.gonnaup.accountplatform.account.entity.Role;
import org.gonnaup.accountplatform.account.exception.RecordNotExistException;
import org.gonnaup.accountplatform.account.repository.RoleRepository;
import org.gonnaup.accountplatform.account.service.IdentifyGenerateService;
import org.gonnaup.accountplatform.account.service.RoleService;
import org.gonnaup.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 角色服务接口实现类
 *
 * @author gonnaup
 * @version created at 2023/7/5 下午8:37
 */
@Service
public class RoleServiceImpl implements RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final IdentifyGenerateService identifyGenerateService;

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(IdentifyGenerateService identifyGenerateService, RoleRepository roleRepository) {
        this.identifyGenerateService = identifyGenerateService;
        this.roleRepository = roleRepository;
    }

    /**
     * 添加角色对象
     *
     * @param role
     * @return 成功添加的角色对象
     */
    @Override
    @Transactional
    public Role addRole(Role role) {
        Integer id = identifyGenerateService.generateAuthId();
        role.setId(id);
        role.setPermissionCode("0");
        role.setBothTimeToNow();
        Role saved = roleRepository.save(role);
        logger.info("成功添加角色 {}", saved);
        return saved;
    }

    /**
     * 更新角色，不更新权限码字段
     *
     * @param role 角色对象
     * @return 更新条数
     */
    @Override
    @Transactional
    public Role updateRoleExceptPermissionCode(Role role) {
        Integer roleId = role.getId();
        Optional<Role> or = roleRepository.findById(roleId);
        if (or.isEmpty()) {
            logger.error("要更新的角色 {} 不存在", roleId);
            throw new RecordNotExistException("error.role.notexist." + roleId);
        }
        Role r = or.get();
        StringUtil.acceptWhenNotBlank(r::setRoleName, role::getRoleName);
        StringUtil.acceptWhenNotBlank(r::setRoleLocalName, role::getRoleLocalName);
        r.setDescription(role.getDescription());

        r.setUpdateTimeToNow();
        roleRepository.save(r);
        logger.info("更新角色 {} 成功", roleId);
        return r;
    }

    /**
     * 更新角色的权限码
     *
     * @param roleId         角色Id
     * @param permissionCode 权限码
     * @return 更新数据条数
     */
    @Override
    @Transactional
    public int updateRolePermissionCode(Integer roleId, String permissionCode) {
        int count = roleRepository.updatePermissionCodeOfRole(roleId, permissionCode, LocalDateTime.now());
        if (count > 0) {
            logger.info("更新角色 {} 的权限码为 {} 成功", roleId, permissionCode);
        } else {
            logger.info("更新角色 {} 的权限码为 {} 失败, 请查看角色是否存在", roleId, permissionCode);
        }
        return count;
    }

    /**
     * 删除角色
     *
     * @param roleId
     * @return
     */
    @Override
    @Transactional
    public Role deleteRole(Integer roleId) {
        Optional<Role> or = roleRepository.findById(roleId);
        if (or.isEmpty()) {
            logger.error("要删除的角色 {} 不存在", roleId);
            throw new RecordNotExistException("error.role.notexist." + roleId);
        }
        roleRepository.deleteById(roleId);
        return or.get();
    }

    /**
     * 分页查询
     *
     * @param example  查询条件
     * @param pageable 分页条件
     * @return 分页数据
     */
    @Override
    public GenericPage<Role> findRoleListPaged(Role example, Pageable pageable) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("roleName", matcher -> matcher.contains())
                .withMatcher("roleLocalName", matcher -> matcher.contains())
                .withMatcher("description", matcher -> matcher.contains())
                .withIgnorePaths("permissionCode");
        Page<Role> page = roleRepository.findAll(Example.of(example, exampleMatcher), pageable);
        return GenericPage.fromPage(page);
    }

    /**
     * 根据Id查询角色
     *
     * @param roleId 角色Id
     * @return 角色对象
     */
    @Override
    public Role findRoleById(Integer roleId) {
        return roleRepository.findById(roleId).orElse(null);
    }

    /**
     * 根据Id列表查询角色列表
     *
     * @param roleIdList
     * @return
     */
    @Override
    public List<Role> findRolesByIdList(List<Integer> roleIdList) {
        return roleRepository.findAllById(roleIdList);
    }

    /**
     * 查询不在id列表中的角色
     *
     * @param roleIdList id列表，为Empty时返回所有角色
     * @return 不在指定id列表中的角色
     */
    @Override
    public List<Role> findRolesByIdNotInList(List<Integer> roleIdList) {
        return roleIdList.isEmpty() ? roleRepository.findAll() : roleRepository.findByIdNotIn(roleIdList);
    }

    /**
     * 查询所有角色
     *
     * @return
     */
    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    /**
     * 获取角色的权限码
     *
     * @param roleId 角色Id
     * @return
     */
    @Override
    public String findRolePermissionCode(Integer roleId) {
        Optional<Role> or = roleRepository.findById(roleId);
        if (or.isEmpty()) {
            logger.error("要查询权限码的角色 {} 不存在", roleId);
            throw new RecordNotExistException("error.role.notexist." + roleId);
        }
        Role role = or.get();
        return role.getPermissionCode();
    }
}
