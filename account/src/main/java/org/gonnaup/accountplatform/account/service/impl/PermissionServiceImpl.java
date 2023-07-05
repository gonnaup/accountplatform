package org.gonnaup.accountplatform.account.service.impl;

import org.gonnaup.accountplatform.account.domain.GenericPage;
import org.gonnaup.accountplatform.account.entity.Permission;
import org.gonnaup.accountplatform.account.entity.RolePermissionPk;
import org.gonnaup.accountplatform.account.exception.RecordNotExistException;
import org.gonnaup.accountplatform.account.repository.PermissionRepository;
import org.gonnaup.accountplatform.account.service.IdentifyGenerateService;
import org.gonnaup.accountplatform.account.service.PermissionService;
import org.gonnaup.accountplatform.account.service.RolePermissionService;
import org.gonnaup.accountplatform.account.util.AuthUtil;
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
 * 权限接口实现类
 *
 * @author gonnaup
 * @version created at 2023/7/5 下午6:18
 * @see PermissionService
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    private static final Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);

    private final PermissionRepository permissionRepository;

    private final IdentifyGenerateService identifyGenerateService;

    private final RolePermissionService rolePermissionService;

    @Autowired
    public PermissionServiceImpl(PermissionRepository permissionRepository, IdentifyGenerateService identifyGenerateService,
                                 RolePermissionService rolePermissionService) {
        this.permissionRepository = permissionRepository;
        this.identifyGenerateService = identifyGenerateService;
        this.rolePermissionService = rolePermissionService;
    }

    /**
     * 添加权限
     * <p>
     * 权限位必须系统生成，防止权限位不连续造成权限位浪费
     *
     * @param permission
     * @return 添加的权限对象
     * @see #generateNextPermissionLocation()
     */
    @Override
    @Transactional
    public Permission addPermission(Permission permission) {
        Integer id = identifyGenerateService.generateAuthId();
        permission.setId(id);// set Id
        LocalDateTime time = LocalDateTime.now();
        permission.setCreateTime(time);
        permission.setUpdateTime(time);
        int nextPermissionLocation = generateNextPermissionLocation();
        //权限位检查
        if (nextPermissionLocation != permission.getPermissionLocation()) {
            logger.error("添加权限时权限位不正确，需要{}, 实际 {}", nextPermissionLocation, permission.getPermissionLocation());
            throw new IllegalArgumentException(String.format("权限位参数错误，需要 %d，实际 %d", nextPermissionLocation, permission.getPermissionLocation()));
        }
        //生成权限码
        permission.setPermissionCode(AuthUtil.generatePermissionChain(permission.getPermissionLocation()));
        Permission saved = permissionRepository.save(permission);
        logger.info("添加权限 {} 成功", saved);
        return saved;
    }

    /**
     * 添加权限并与角色列表关联
     *
     * @param permission 权限对象
     * @param roleIds    角色列表
     * @return 成功关联角色的数量
     */
    @Override
    @Transactional
    public int addPermissionAndAttachRoles(Permission permission, List<Integer> roleIds) {
        final Permission p = addPermission(permission);
        if (roleIds.size() > 0) {
            final Integer permissionId = p.getId();
            List<RolePermissionPk> rolePermissionPks = roleIds.stream().map(roleId -> RolePermissionPk.of(roleId, permissionId)).toList();
            int count = rolePermissionService.addRolePermissionList(rolePermissionPks);
            if (logger.isDebugEnabled()) {
                logger.debug("需要与权限 {} 关联的角色 {} 共 {} 个", permissionId, roleIds, roleIds.size());
            }
            logger.info("{}个角色和权限 {} 成功关联", count, permissionId);
            return count;
        }
        logger.warn("需要和权限 {} 关联的角色个数为0", p.getId());
        return 0;
    }

    /**
     * 更新权限对象的非权限部分字段
     * <p>
     * permissionName, permissionLocalName, resources, description
     *
     * @param permission 要更新的权限对象
     * @return 更新后的权限对象
     */
    @Override
    @Transactional
    public Permission updatePermission(Permission permission) {
        Integer id = permission.getId();
        Optional<Permission> op = permissionRepository.findById(id);
        if (op.isEmpty()) {
            return null;
        }
        Permission p = op.get();
        p.setPermissionName(permission.getPermissionName());
        p.setPermissionLocalName(permission.getPermissionLocalName());
        p.setResources(permission.getResources());
        p.setDescription(permission.getDescription());
        return permissionRepository.save(p);
    }

    /**
     * 删除权限
     *
     * @param permissionId 权限Id
     * @return 被删除的权限，删除失败返回null
     */
    @Override
    @Transactional
    public Permission deletePermission(Integer permissionId) {
        Permission permission = findByPermissionId(permissionId);
        if (permission == null) {
            logger.error("要删除的权限 {} 不存在", permissionId);
            throw new RecordNotExistException("error.permission.notexist." + permissionId);
        }
        permissionRepository.deleteById(permissionId);
        logger.info("删除权限 {}", permission);
        return permission;
    }

    /**
     * 获取下一个权限位
     *
     * @return 下一个权限位
     */
    @Override
    @Transactional
    public int generateNextPermissionLocation() {
        return permissionRepository.findNextPermissionLocation();
    }

    /**
     * 根据ID查询权限对象
     *
     * @param permissionId 权限ID
     * @return 权限对象
     */
    @Override
    public Permission findByPermissionId(Integer permissionId) {
        return permissionRepository.findById(permissionId).orElse(null);
    }

    /**
     * 获取所有权限列表
     *
     * @return 所有权限列表
     */
    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    /**
     * 分页查询
     *
     * @param example  查询条件
     * @param pageable 分页条件
     * @return 查询页
     */
    @Override
    public GenericPage<Permission> findPermissionPaged(Permission example, Pageable pageable) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("permissionName", matcher -> matcher.contains())
                .withMatcher("permissionLocalName", matcher -> matcher.contains())
                .withMatcher("resources", matcher -> matcher.contains())
                .withMatcher("description", matcher -> matcher.contains());
        Page<Permission> page = permissionRepository.findAll(Example.of(example, exampleMatcher), pageable);
        return GenericPage.fromPage(page);
    }

    /**
     * 获取权限码
     *
     * @param permissionId 权限Id
     * @return 权限码
     */
    @Override
    public String findPermissionCode(Integer permissionId) {
        Optional<Permission> op = permissionRepository.findById(permissionId);
        if (op.isEmpty()) {
            throw new RecordNotExistException(String.format("error.permission.notexist.%d", permissionId));
        }
        return op.get().getPermissionCode();
    }
}
