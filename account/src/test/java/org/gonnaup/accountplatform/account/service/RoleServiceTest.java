package org.gonnaup.accountplatform.account.service;

import org.gonnaup.accountplatform.account.domain.GenericPage;
import org.gonnaup.accountplatform.account.entity.Role;
import org.gonnaup.accountplatform.account.util.AuthUtil;
import org.gonnaup.common.util.RandomUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gonnaup
 * @version created at 2023/7/17 18:19
 */
@SpringBootTest
class RoleServiceTest {

    RoleService roleService;

    Role r1;

    Role r2;

    Role r3;

    @Autowired
    public RoleServiceTest(RoleService roleService) {
        this.roleService = roleService;
    }

    @BeforeEach
    void setUp() {
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
    void testRoleService() {
        //add
        roleService.addRole(r1);
        assertNotNull(roleService.findRoleById(r1.getId()));

        //update
        Role role = new Role();
        role.setId(r1.getId());
        role.setRoleName(RandomUtil.randomString(7));
        role.setRoleLocalName(RandomUtil.randomString(8));
        roleService.updateRoleExceptPermissionCode(role);
        assertEquals(roleService.findAll().size() - 1, roleService.findRolesByIdNotInList(List.of(role.getId())).size());
        Role roleById = roleService.findRoleById(r1.getId());
        assertEquals(role.getRoleName(), roleById.getRoleName());
        assertEquals(role.getRoleLocalName(), roleById.getRoleLocalName());
        assertEquals(role.getDescription(), roleById.getDescription());

        String pCode = AuthUtil.generatePermissionChain(List.of(1, 2, 3));
        roleService.updateRolePermissionCode(r1.getId(), pCode);
        assertEquals(pCode, roleService.findRoleById(r1.getId()).getPermissionCode());
        assertEquals(pCode, roleService.findRolePermissionCode(r1.getId()));

        //delete
        roleService.deleteRole(r1.getId());
        assertNull(roleService.findRoleById(r1.getId()));

        //find
        roleService.addRole(r1);
        roleService.addRole(r2);
        roleService.addRole(r3);

        assertEquals(3, roleService.findRolesByIdList(List.of(r1.getId(), r2.getId(), r3.getId())).size());

        Role example = new Role();
        example.setRoleName("Role_");
        example.setRoleLocalName("Role_");

        GenericPage<Role> rolePage = roleService.findRoleListPaged(example, PageRequest.of(0, 2));
        assertEquals(2, rolePage.getRecords().size());


    }
}