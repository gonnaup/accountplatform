package org.gonnaup.accountplatform.account.service;

import org.gonnaup.accountplatform.account.domain.GenericPage;
import org.gonnaup.accountplatform.account.entity.AccountOutline;
import org.gonnaup.accountplatform.account.entity.AccountOutlineRolePk;
import org.gonnaup.accountplatform.account.entity.Permission;
import org.gonnaup.accountplatform.account.entity.Role;
import org.gonnaup.accountplatform.account.util.AuthUtil;
import org.gonnaup.common.util.RandomUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 账号-角色关联服务接口测试
 *
 * @author gonnaup
 * @version created at 2023/7/18 18:21
 */
@SpringBootTest
class AccountOutlineRoleServiceTest {

    AccountOutlineRoleService accountOutlineRoleService;

    AccountOutlineService accountOutlineService;

    RoleService roleService;

    PermissionService permissionService;

    RolePermissionService rolePermissionService;

    Permission p1;

    Permission p2;

    Permission p3;

    Role r1;

    Role r2;

    Role r3;

    AccountOutline a1;

    AccountOutline a2;

    @Autowired
    public AccountOutlineRoleServiceTest(AccountOutlineRoleService accountOutlineRoleService, AccountOutlineService accountOutlineService,
                                         RoleService roleService, PermissionService permissionService, RolePermissionService rolePermissionService) {
        this.accountOutlineRoleService = accountOutlineRoleService;
        this.accountOutlineService = accountOutlineService;
        this.roleService = roleService;
        this.permissionService = permissionService;
        this.rolePermissionService = rolePermissionService;
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

        a1 = new AccountOutline();
        a1.setAccountName(RandomUtil.randomAlphabet(8));

        a2 = new AccountOutline();
        a2.setAccountName(RandomUtil.randomAlphabet(10));
    }

    @Test
    void testAccountOutlineRoleService() {
        permissionService.addPermission(p1);
        permissionService.addPermission(p2);
        permissionService.addPermission(p3);
        a1 = accountOutlineService.addAccountOutline(a1);
        a2 = accountOutlineService.addAccountOutline(a2);
        roleService.addRole(r2);
        roleService.addRole(r3);

        // r1 -> p1
        rolePermissionService.addRoleAndAttachPermissions(r1, List.of(p1.getId()));
        // a1 -> r1
        accountOutlineRoleService.addAccountOutlineRole(AccountOutlineRolePk.of(a1.getId(), r1.getId()));
        //countXXX
        assertEquals(1, accountOutlineRoleService.countRolesByAccountOutlineId(a1.getId()));
        assertEquals(0, accountOutlineRoleService.countRolesByAccountOutlineId(a2.getId()));
        assertTrue(accountOutlineRoleService.countRolesByNotAccountOutlineId(a1.getId()) >= 2);
        assertTrue(accountOutlineRoleService.countRolesByNotAccountOutlineId(a2.getId()) >= 3);
        assertEquals(accountOutlineRoleService.countAccountOutlinesByRoleId(r1.getId()), 1);
        assertEquals(accountOutlineRoleService.countAccountOutlinesByRoleId(r2.getId()), 0);
        assertTrue(accountOutlineRoleService.countAccountOutlinesByNotRoleId(r1.getId()) >= 1);
        assertTrue(accountOutlineRoleService.countAccountOutlinesByNotRoleId(r2.getId()) >= 2);

        //findXXX
        assertEquals(accountOutlineRoleService.findAccountOutlineIdListByRoleId(r1.getId()).size(), 1);
        assertEquals(accountOutlineRoleService.findAccountOutlineIdListByRoleId(r2.getId()).size(), 0);
        assertEquals(accountOutlineRoleService.findRolesByAccountId(a1.getId()).size(), 1);
        assertTrue(accountOutlineRoleService.findRolesNotAttachAccount(a1.getId()).size() >= 2);
        //auth
        assertEquals(p1.getPermissionCode(),
                accountOutlineService.calculatePermissionCode(a1.getId()));
        accountOutlineRoleService.deleteByPrimaryKey(AccountOutlineRolePk.of(a1.getId(), r1.getId()));
        assertTrue(accountOutlineRoleService.findRolesByAccountId(a1.getId()).isEmpty());
        assertEquals("0", accountOutlineService.calculatePermissionCode(a1.getId()));

        // ------------- findAll().size() == 0 -----------------//
        // a1 -> r1, r2
        accountOutlineRoleService.addAccountOutlineRoleList(a1.getId(), List.of(r2.getId(), r1.getId()));
        // a2 -> r1
        accountOutlineRoleService.addRoleAccountOutlineList(r1.getId(), List.of(a2.getId()));
        assertEquals(2, accountOutlineRoleService.countRolesByAccountOutlineId(a1.getId()));
        assertEquals(0, accountOutlineRoleService.countAccountOutlinesByRoleId(r3.getId()));
        assertEquals(2, accountOutlineRoleService.countAccountOutlinesByRoleId(r1.getId()));

        // assert total 2, pagesize = 1, page = 2
        GenericPage<AccountOutline> accountPage1 = accountOutlineRoleService.findAccountOutlinesByRoleIdPaged(r1.getId(), PageRequest.of(1, 1));
        assertEquals(2, accountPage1.getTotalElements());
        assertEquals(2, accountPage1.getTotalPages());
        assertEquals(1, accountPage1.getRecords().size());

        // assert total >= 2, pagesize = 2, page = 1
        GenericPage<AccountOutline> accountPage2 = accountOutlineRoleService.findAccountOutlinesNotAttachRolePaged(r3.getId(), PageRequest.of(0, 2));
        assertTrue(accountPage2.getTotalElements() >= 2);
        assertEquals(2, accountPage2.getRecords().size());
        assertTrue(accountPage2.getTotalPages() >= 1);

        //delete a2 -> r1
        accountOutlineRoleService.deleteByAccountOutlineId(a2.getId());
        assertTrue(accountOutlineRoleService.findRolesByAccountId(a2.getId()).isEmpty());
        String a2Code = accountOutlineService.calculatePermissionCode(a2.getId());
        assertEquals(AuthUtil.ZERO_P_CODE, a2Code);

        //delete a1 -> r1, 剩余 a1 -> r2
        accountOutlineRoleService.deleteAccountOutlineRoleList(a1.getId(), List.of(r1.getId()));
        assertEquals(1, accountOutlineRoleService.findRolesByAccountId(a1.getId()).size());
        assertEquals(r2.getPermissionCode(), accountOutlineService.calculatePermissionCode(a1.getId()));

        //delete a1-> r2
        accountOutlineRoleService.deleteByRoleId(r2.getId());
        assertTrue(accountOutlineRoleService.findAccountOutlineIdListByRoleId(r2.getId()).isEmpty());

        // a1 -> r1, a2 -> r1
        accountOutlineRoleService.addRoleAccountOutlineList(r1.getId(), List.of(a1.getId(), a2.getId()));
        assertEquals(2, accountOutlineRoleService.findAccountOutlineIdListByRoleId(r1.getId()).size());
        //delete r1 -> a1,a2
        accountOutlineRoleService.deleteRoleAccountOutlineList(r1.getId(), List.of(a1.getId(), a2.getId()));
        assertTrue(accountOutlineRoleService.findAccountOutlineIdListByRoleId(r1.getId()).isEmpty());
    }

}