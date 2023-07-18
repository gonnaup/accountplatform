package org.gonnaup.accountplatform.account.service;

import org.gonnaup.accountplatform.account.entity.Permission;
import org.gonnaup.accountplatform.account.entity.Role;
import org.gonnaup.accountplatform.account.entity.RolePermissionPk;
import org.gonnaup.accountplatform.account.repository.PermissionRepository;
import org.gonnaup.accountplatform.account.repository.RolePermissionRepository;
import org.gonnaup.accountplatform.account.repository.RoleRepository;
import org.gonnaup.accountplatform.account.util.AuthUtil;
import org.gonnaup.common.util.RandomUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gonnaup
 * @version created at 2023/7/17 20:09
 */
@SpringBootTest
class RolePermissionServiceTest {

    RolePermissionService rolePermissionService;

    RolePermissionRepository rolePermissionRepository;

    RoleService roleService;

    PermissionService permissionService;

    PermissionRepository permissionRepository;

    RoleRepository roleRepository;

    Permission p1;

    Permission p2;

    Permission p3;

    Role r1;

    Role r2;

    Role r3;

    @Autowired
    public RolePermissionServiceTest(RolePermissionService rolePermissionService, RolePermissionRepository rolePermissionRepository,
                                     RoleService roleService, PermissionService permissionService, PermissionRepository permissionRepository, RoleRepository roleRepository) {
        this.rolePermissionService = rolePermissionService;
        this.rolePermissionRepository = rolePermissionRepository;
        this.roleService = roleService;
        this.permissionService = permissionService;
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
    }

    @BeforeEach
    void setUp() {
        p1 = new Permission();
        p1.setPermissionName(RandomUtil.randomString(5));
        p1.setPermissionLocalName(p1.getPermissionName());
        p1.setResources("/api/admin/**");

        p2 = new Permission();
        p2.setPermissionName(RandomUtil.randomString(5));
        p2.setPermissionLocalName(p2.getPermissionName());
        p2.setResources("/api/user/**");

        p3 = new Permission();
        p3.setPermissionName(RandomUtil.randomString(5));
        p3.setPermissionLocalName(p3.getPermissionName());
        p3.setResources("/api/guest/**");

        r1 = new Role();
        r1.setRoleName(RandomUtil.randomAlphabetWithPrefix(5, "Role_"));
        r1.setRoleLocalName(r1.getRoleName());
        r1.setDescription(RandomUtil.randomString(100));

        r2 = new Role();
        r2.setRoleName(RandomUtil.randomAlphabetWithPrefix(5, "Role_"));
        r2.setRoleLocalName(r2.getRoleName());
        r2.setDescription(RandomUtil.randomString(100));

        r3 = new Role();
        r3.setRoleName(RandomUtil.randomAlphabetWithPrefix(5, "Role_"));
        r3.setRoleLocalName(r3.getRoleName());
        r3.setDescription(RandomUtil.randomString(100));

    }

    @Test
    @Transactional
    void testRolePermissionService() {
        rolePermissionRepository.deleteAll();
        permissionRepository.deleteAll();
        roleRepository.deleteAll();

        //add
        permissionService.addPermission(p1);
        permissionService.addPermission(p2);
        permissionService.addPermission(p3);
        int count = rolePermissionService.addRoleAndAttachPermissions(r1, List.of(p1.getId()));
        roleService.addRole(r2);
        roleService.addRole(r3);
        assertEquals(1, count);
        assertEquals(1, rolePermissionService.countByRoleId(r1.getId()));
        assertEquals(1, rolePermissionService.findPermissionsByRoleId(r1.getId()).size());
        assertEquals(p1.getPermissionName(), rolePermissionService.findPermissionsByRoleId(r1.getId()).get(0).getPermissionName());
        assertEquals(2, rolePermissionService.findPermissionsNotAttachRole(r1.getId()).size());
        assertEquals(1, rolePermissionService.countByPermissionId(p1.getId()));
        assertEquals(1, rolePermissionService.findRolesByPermissionId(p1.getId()).size());
        assertEquals(2, rolePermissionService.findRolesNotAttachPermission(p1.getId()).size());

        rolePermissionService.deleteByRoleId(r1.getId());
        assertTrue(rolePermissionService.findPermissionsByRoleId(r1.getId()).isEmpty());
        assertTrue(rolePermissionService.findRolesByPermissionId(p1.getId()).isEmpty());

        permissionService.deletePermission(p1.getId());

        rolePermissionService.addPermissionAndAttachRoles(p1, List.of(r1.getId(), r2.getId(), r3.getId()));
        r1 = roleService.findRoleById(r1.getId());
        assertEquals(r1.getPermissionCode(), p1.getPermissionCode());
        assertEquals(3, rolePermissionService.findRolesByPermissionId(p1.getId()).size());
        assertEquals(0, rolePermissionService.findRolesNotAttachPermission(p1.getId()).size());
        assertEquals(3, rolePermissionService.findRolesNotAttachPermission(p2.getId()).size());

        RolePermissionPk pk = RolePermissionPk.of(r1.getId(), p2.getId());
        rolePermissionService.addRolePermission(pk);
        r1 = roleService.findRoleById(r1.getId());
        assertEquals(2, rolePermissionService.findPermissionsByRoleId(r1.getId()).size());
        assertEquals(r1.getPermissionCode(), AuthUtil.mergePermissionChainList(List.of(p1.getPermissionCode(), p2.getPermissionCode())));

        rolePermissionService.roleAttachPermissions(r3.getId(), List.of(p1.getId(), p3.getId()));
        assertEquals(2, rolePermissionService.findPermissionsByRoleId(r3.getId()).size());

        rolePermissionService.permissionAttachRoles(p3.getId(), List.of(r1.getId(), r3.getId()));
        assertEquals(2, rolePermissionService.findRolesByPermissionId(p3.getId()).size());

        assertEquals(3, rolePermissionService.updateRoleAttachPermissions(r2.getId(), List.of(p1.getId(), p2.getId(), p3.getId())));
        r2 = roleService.findRoleById(r2.getId());
        assertEquals(r2.getPermissionCode(), AuthUtil.mergePermissionChainList(List.of(p1.getPermissionCode(), p2.getPermissionCode(), p3.getPermissionCode())));

        assertEquals(1, rolePermissionService.updatePermissionAttachRoles(p1.getId(), List.of(r3.getId())));
        assertEquals(1, rolePermissionService.findRolesByPermissionId(p1.getId()).size());

        rolePermissionService.deleteByPermissionId(p1.getId());
        assertEquals(0, rolePermissionService.findRolesByPermissionId(p1.getId()).size());

        rolePermissionService.deleteByPrimaryKey(pk);
        assertTrue(rolePermissionRepository.findById(pk).isEmpty());

        rolePermissionRepository.deleteAll();

        rolePermissionService.updateRoleAttachPermissions(r1.getId(), List.of(p1.getId(), p2.getId(), p3.getId()));
        assertEquals(3, rolePermissionService.findPermissionsByRoleId(r1.getId()).size());
        rolePermissionService.deleteRoleRef(r1.getId(), List.of(p2.getId(), p3.getId()));
        assertEquals(1, rolePermissionService.findPermissionsByRoleId(r1.getId()).size());
        assertEquals(p1.getId(), rolePermissionService.findPermissionsByRoleId(r1.getId()).get(0).getId());

        rolePermissionService.deletePermissionRef(p1.getId(), List.of(r1.getId(), r2.getId()));
        assertTrue(rolePermissionService.findRolesByPermissionId(p1.getId()).isEmpty());

    }

}