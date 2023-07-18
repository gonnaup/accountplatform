package org.gonnaup.accountplatform.account.service.impl;

import org.gonnaup.accountplatform.account.entity.Permission;
import org.gonnaup.accountplatform.account.entity.Role;
import org.gonnaup.accountplatform.account.entity.RolePermission;
import org.gonnaup.accountplatform.account.entity.RolePermissionPk;
import org.gonnaup.accountplatform.account.exception.RecordNotExistException;
import org.gonnaup.accountplatform.account.repository.RolePermissionRepository;
import org.gonnaup.accountplatform.account.service.*;
import org.gonnaup.accountplatform.account.util.AuthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 角色权限关联关系服务接口实现类
 *
 * @author gonnaup
 * @version created at 2023/7/5 下午11:07
 */
@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    private static final Logger logger = LoggerFactory.getLogger(RolePermissionServiceImpl.class);

    private RolePermissionRepository rolePermissionRepository;

    private PermissionService permissionService;

    private RoleService roleService;

    private AccountOutlineRoleService accountOutlineRoleService;

    private AccountOutlineService accountOutlineService;

    /**
     * 添加角色并关联权限列表
     *
     * @param role          角色对象
     * @param permissionIds 要关联的权限ID列表
     * @return 成功关联的数量
     */
    @Override
    @Transactional
    public int addRoleAndAttachPermissions(Role role, List<Integer> permissionIds) {
        Role r = roleService.addRole(role);
        if (permissionIds.size() > 0) {
            final Integer roleId = r.getId();
            int count = roleAttachPermissions(roleId, permissionIds);
            if (logger.isDebugEnabled()) {
                logger.debug("需要与角色 {} 关联的权限 {} 共 {} 个", roleId, permissionIds, permissionIds.size());
            }
            logger.info("{}个权限和角色 {} 成功关联", count, roleId);
            return count;
        }
        logger.warn("需要和角色 {} 关联的权限个数为0", r.getId());
        return 0;
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
        final Permission p = permissionService.addPermission(permission);
        if (roleIds.size() > 0) {
            final Integer permissionId = p.getId();
            int count = permissionAttachRoles(permissionId, roleIds);
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
     * 添加关联对象，如果有权限缓存，需删除关联缓存
     *
     * @param rolePermissionPk@return 添加的对象
     * @return 成功添加返回添加的实体类，已存在返回null
     */
    @Override
    @Transactional
    public RolePermission addRolePermission(RolePermissionPk rolePermissionPk) {
        Integer roleId = rolePermissionPk.getRoleId();
        Integer permissionId = rolePermissionPk.getPermissionId();
        if (rolePermissionRepository.findById(rolePermissionPk).isPresent()) {
            logger.warn("角色-权限关联关系 {} 已存在，不再重复添加", rolePermissionPk);
            return null;
        }
        logger.info("开始添加角色[{}]-权限[{}]关联关系...", roleId, permissionId);
        Role role = roleService.findRoleById(roleId);
        Permission permission = permissionService.findPermissionById(permissionId);
        if (role == null || permission == null) {
            logger.error("添加角色-权限关联关系时，角色[{}]或权限[{}]不存在", roleId, permissionId);
            throw new RecordNotExistException("error.rolepermission.roleorpermission.notexist");
        }
        RolePermission rp = RolePermission.of(rolePermissionPk, role, permission);
        rp = rolePermissionRepository.save(rp);
        logger.info("添加角色-权限关联关系 {}", rp);
        /**
         * 开始更新角色权限和帐号权限
         */
        // 1.角色权限更新
        String pcode = permission.getPermissionCode();
        String rcode = role.getPermissionCode();
        //授权
        String rcodeMergePcode = AuthUtil.grantPermission(rcode, pcode);
        //更新角色权限
        role.setPermissionCode(rcodeMergePcode);
        roleService.updateRolePermissionCode(roleId, rcodeMergePcode);
        logger.info("角色对象权限码{}，权限对象权限码{}，更新角色对象权限码为{}", rcode, pcode, rcodeMergePcode);

        // 2.与角色关联的账户权限缓存删除
        clearRelateRoleAccountPermissionCodeCache(roleId);
        logger.info("成功添加角色[{}]-权限[{}]关联关系...", roleId, permissionId);
        return rp;
    }

    /**
     * 为某角色批量添加角色权限关联对象，如果有权限缓存，需删除关联缓存
     *
     * @param roleId        角色Id
     * @param permissionIds 权限Id列表，必须是没有和角色关联的权限
     * @return 添加的数量
     */
    @Override
    @Transactional
    public int roleAttachPermissions(Integer roleId, List<Integer> permissionIds) {
        //角色
        final Role role = roleService.findRoleById(roleId);
        if (role == null) {
            logger.error("为角色批量添加权限关联对象时，角色ID={}不存在", roleId);
            throw new RecordNotExistException("error.rolepermission.role.notexist." + roleId);
        }
        //添加关联关系并返回权限码集合，如果权限已关联此角色，则保持关联关系
        List<String> pCodeList = permissionIds.stream().map(pId -> {
            Permission p = permissionService.findPermissionById(pId);
            if (p == null) {
                logger.error("为角色批量添加权限关联对象时，权限ID={}不存在", pId);
                throw new RecordNotExistException("error.rolepermission.permission.notexist." + pId);
            }
            RolePermission rolePermission = RolePermission.of(role, p);
            RolePermission rp = rolePermissionRepository.save(rolePermission);
            logger.info("添加角色-权限关联关系 {}", rp);
            return p.getPermissionCode();
        }).toList();

        String rc = role.getPermissionCode();
        String merged = AuthUtil.grantPermission(rc, pCodeList);
        role.setPermissionCode(merged);
        roleService.updateRolePermissionCode(roleId, merged);
        logger.info("角色对象权限码{}，权限对象权限码列表{}，更新角色对象权限码为{}", rc, pCodeList, merged);

        clearRelateRoleAccountPermissionCodeCache(roleId);
        logger.info("成功添加角色[{}]-权限列表[{}]关联关系", roleId, permissionIds);

        return permissionIds.size();
    }

    /**
     * 为某权限批量添加角色权限关联对象，如果有权限缓存，需删除关联缓存
     *
     * @param permissionId 权限Id
     * @param roleIds      角色Id列表，必须是没有和权限关联的角色
     * @return 添加的数量
     */
    @Override
    @Transactional
    public int permissionAttachRoles(Integer permissionId, List<Integer> roleIds) {
        logger.info("开始为权限ID[{}]绑定角色列表ID[{}]...", permissionId, roleIds);
        roleIds.forEach(roleId -> {
            RolePermissionPk pk = RolePermissionPk.of(roleId, permissionId);
            addRolePermission(pk);
        });
        int count = roleIds.size();
        logger.info("为权限ID[{}]成功绑定{}个角色对象", permissionId, count);
        return count;
    }

    /**
     * 根据联合主键删除一个关联对象，如果有权限缓存，需删除关联缓存
     *
     * @param rolePermissionPk 主键
     * @return 删除对象个数
     */
    @Override
    @Transactional
    public int deleteByPrimaryKey(RolePermissionPk rolePermissionPk) {
        Optional<RolePermission> orp = rolePermissionRepository.findById(rolePermissionPk);
        if (orp.isEmpty()) {
            logger.warn("要删除的关联关系 {} 不存在", rolePermissionPk);
            return 0;
        }
        Integer roleId = rolePermissionPk.getRoleId();
        Integer permissionId = rolePermissionPk.getPermissionId();
        logger.info("开始删除角色[{}]-权限[{}]关联关系", roleId, permissionId);
        rolePermissionRepository.deleteById(rolePermissionPk);

        String pCode = permissionService.findPermissionCode(permissionId);
        String rCode = roleService.findRolePermissionCode(roleId);
        if (logger.isDebugEnabled()) {
            logger.debug("移除角色权限{} 中的 {}权限", rCode, pCode);
        }
        String removedPCode = AuthUtil.removePermission(rCode, pCode);
        roleService.updateRolePermissionCode(roleId, removedPCode);

        clearRelateRoleAccountPermissionCodeCache(roleId);
        return 1;
    }

    /**
     * 批量删除某角色的关联对象，如果有权限缓存，需删除关联缓存
     *
     * @param roleId        角色Id
     * @param permissionIds 关联的权限Id列表
     * @return 删除的数量
     */
    @Override
    public int deleteRoleRef(Integer roleId, List<Integer> permissionIds) {
        logger.info("开始删除角色[{}]关联的多个权限列表[{}]...", roleId, permissionIds);
        Role role = roleService.findRoleById(roleId);
        if (role == null) {
            logger.error("删除角色关联权限列表时，角色[{}]不存在", roleId);
            throw new RecordNotExistException("error.rolepermission.role.notexist." + roleId);
        }
        List<String> permissionsCodeList = permissionIds.stream().map(permissionId -> {
            RolePermissionPk pk = RolePermissionPk.of(roleId, permissionId);
            rolePermissionRepository.deleteById(pk);
            rolePermissionRepository.flush();//TODO
            return permissionService.findPermissionCode(permissionId);
        }).filter(Objects::nonNull).toList();

        //更新角色权限码
        String rCode = role.getPermissionCode();
        String removedPCode = AuthUtil.removePermission(rCode, permissionsCodeList);
        if (logger.isDebugEnabled()) {
            logger.debug("移除角色权限{} 中的 {} 权限", rCode, permissionsCodeList);
        }
        roleService.updateRolePermissionCode(roleId, removedPCode);

        clearRelateRoleAccountPermissionCodeCache(roleId);

        return permissionsCodeList.size();
    }

    /**
     * 批量删除某权限的关联对象，如果有权限缓存，需删除关联缓存
     *
     * @param permissionId 权限Id
     * @param roleIds      角色Id列表
     * @return 删除的数量
     */
    @Override
    @Transactional
    public int deletePermissionRef(Integer permissionId, List<Integer> roleIds) {
        logger.info("开始删除权限[{}]关联的角色[{}]...", permissionId, roleIds);
        long count = roleIds.stream().map(roleId -> RolePermissionPk.of(roleId, permissionId))
                .filter(pk -> {
                    if (rolePermissionRepository.findById(pk).isEmpty()) {
                        logger.warn("角色-权限关联对象 {} 不存在，跳过删除步骤", pk);
                        return false;
                    }
                    return true;
                }).map(pk -> deleteByPrimaryKey(pk)).toList().size();//使用.count()方法后map方法不会执行！！！
        logger.info("共删除权限[{}]关联的角色 {} 个", permissionId, count);
        return (int) count;
    }

    /**
     * 根据角色ID删除关联关系，如果有权限缓存，需删除关联缓存
     *
     * @param roleId 角色ID
     * @return 删除对象的个数
     */
    @Override
    @Transactional
    public int deleteByRoleId(Integer roleId) {
        logger.info("开始删除角色[{}]所有关联权限...", roleId);
        List<RolePermission> rp = rolePermissionRepository.findByRoleId(roleId);
        if (rp.isEmpty()) {
            logger.warn("查询到角色[{}]没有关联权限，无需执行删除操作", roleId);
            return 0;
        }
        logger.info("查询到角色[{}]关联权限共 {} 个，开始删除关联关系", roleId, rp.size());
        List<RolePermissionPk> pkList = rp.stream().map(RolePermission::getId).toList();
        rolePermissionRepository.flush();
        rolePermissionRepository.deleteAllByIdInBatch(pkList);

        String zeroPermissionCode = "0";
        roleService.updateRolePermissionCode(roleId, zeroPermissionCode);
        logger.info("更新角色[{}]的权限码为 {}", roleId, zeroPermissionCode);

        clearRelateRoleAccountPermissionCodeCache(roleId);
        return rp.size();
    }

    /**
     * 删除和某权限的所有角色关联关系，如果有权限缓存，需删除关联缓存
     *
     * @param permissionId
     * @return 删除对象个数
     */
    @Override
    @Transactional
    public int deleteByPermissionId(Integer permissionId) {
        logger.info("开始删除权限[{}]所有关联的角色...", permissionId);
        List<RolePermission> rp = rolePermissionRepository.findByPermissionId(permissionId);
        List<Integer> roleIdList = rp.stream().map(rolePermission -> rolePermission.getId().getRoleId()).toList();
        if (roleIdList.isEmpty()) {
            logger.warn("查询到权限[{}]没有关联角色，无需执行删除操作", permissionId);
            return 0;
        }
        logger.info("查询到权限[{}]关联角色共 {} 个，开始删除关联关系", permissionId, roleIdList.size());
        return deletePermissionRef(permissionId, roleIdList);
    }

    /**
     * 为角色重新关联权限列表，先删除已有的关联关系，再重新关联列表
     *
     * @param roleId        角色Id
     * @param permissionIds
     * @return 重新关联的个数
     */
    @Override
    @Transactional
    public int updateRoleAttachPermissions(Integer roleId, List<Integer> permissionIds) {
        logger.info("开始重新配置角色[{}]关联的权限列表 {}", roleId, permissionIds);
        logger.info("开始删除角色[{}]所有关联权限...", roleId);
        List<RolePermission> rp = rolePermissionRepository.findByRoleId(roleId);
        if (!rp.isEmpty()) {
            logger.info("查询到角色[{}]关联权限共 {} 个，开始删除关联关系", roleId, rp.size());
            List<RolePermissionPk> pkList = rp.stream().map(RolePermission::getId).toList();
            rolePermissionRepository.flush();
            rolePermissionRepository.deleteAllByIdInBatch(pkList);
        }
        return roleAttachPermissions(roleId, permissionIds);
    }

    /**
     * 为权限重新关联角色列表，先删除原有关联关系， 再重新关联
     *
     * @param permissionId 权限Id
     * @param roleIds      角色Id列表
     * @return 重新关联的个数
     */
    @Override
    @Transactional
    public int updatePermissionAttachRoles(Integer permissionId, List<Integer> roleIds) {
        logger.info("开始重新配置权限[{}]关联的角色列表 {}", permissionId, roleIds);
        deleteByPermissionId(permissionId);
        return permissionAttachRoles(permissionId, roleIds);
    }

    /**
     * 查询某角色关联了多少权限
     *
     * @param roleId
     * @return 关联权限的个数
     */
    @Override
    public int countByRoleId(Integer roleId) {
        return rolePermissionRepository.countByRoleId(roleId);
    }

    /**
     * 查询和某个角色关联的权限列表
     *
     * @param roleId
     * @return
     */
    @Override
    public List<Permission> findPermissionsByRoleId(Integer roleId) {
        return permissionService.findPermissionsByIdList(rolePermissionRepository.findByRoleId(roleId).stream()
                .map(rolePermission -> rolePermission.getId().getPermissionId()).toList());
    }

    /**
     * 查询没有和某个角色关联的权限列表
     *
     * @param roleId 角色Id
     * @return 权限列表
     */
    @Override
    public List<Permission> findPermissionsNotAttachRole(Integer roleId) {
        return permissionService.findPermissionsByIdNotInLIst(rolePermissionRepository.findByRoleId(roleId).stream()
                .map(rolePermission -> rolePermission.getId().getPermissionId()).toList());
    }

    /**
     * 查询某个权限被多少角色关联
     *
     * @param permissionId
     * @return 被关联的数量
     */
    @Override
    public int countByPermissionId(Integer permissionId) {
        return rolePermissionRepository.countByPermissionId(permissionId);
    }

    /**
     * 查询关联了某个权限的角色列表
     *
     * @param permissionId
     * @return 关联了此权限的角色列表
     */
    @Override
    public List<Role> findRolesByPermissionId(Integer permissionId) {
        return roleService.findRolesByIdList(rolePermissionRepository.findByPermissionId(permissionId).stream()
                .map(rolePermission -> rolePermission.getId().getRoleId()).toList());
    }

    /**
     * 查询没有和某权限关联的角色
     *
     * @param permissionId 权限Id
     * @return 角色列表
     */
    @Override
    public List<Role> findRolesNotAttachPermission(Integer permissionId) {
        return roleService.findRolesByIdNotInList(rolePermissionRepository.findByPermissionId(permissionId).stream()
                .map(rolePermission -> rolePermission.getId().getRoleId()).toList());
    }

    /**
     * 删除和角色有关联的帐号的权限码缓存
     *
     * @param roleId 角色Id
     */
    private void clearRelateRoleAccountPermissionCodeCache(Integer roleId) {
        List<Long> accountOutlineIdList = accountOutlineRoleService.findAccountOutlineIdListByRoleId(roleId);
        accountOutlineIdList.forEach(accountOutlineId -> accountOutlineService.clearPermissionCodeCache(accountOutlineId));
        logger.info("总共清除 {} 个帐号的权限码缓存", accountOutlineIdList.size());
    }

    @Autowired
    public void setRolePermissionRepository(RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Autowired
    public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setAccountOutlineRoleService(AccountOutlineRoleService accountOutlineRoleService) {
        this.accountOutlineRoleService = accountOutlineRoleService;
    }

    @Autowired
    public void setAccountOutlineService(AccountOutlineService accountOutlineService) {
        this.accountOutlineService = accountOutlineService;
    }
}
