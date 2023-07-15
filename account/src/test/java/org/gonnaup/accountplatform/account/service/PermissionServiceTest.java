package org.gonnaup.accountplatform.account.service;

import org.gonnaup.accountplatform.account.domain.GenericPage;
import org.gonnaup.accountplatform.account.entity.Permission;
import org.gonnaup.accountplatform.account.repository.PermissionRepository;
import org.gonnaup.accountplatform.account.util.AuthUtil;
import org.gonnaup.common.util.RandomUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gonnaup
 * @version created at 2023/7/15 19:40
 */
@SpringBootTest
public class PermissionServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(PermissionServiceTest.class);

    final PermissionService permissionService;

    final PermissionRepository permissionRepository;

    List<Permission> permissionList = new ArrayList<>(3);

    Permission p1;

    Permission p2;

    Permission p3;

    @Autowired
    public PermissionServiceTest(PermissionService permissionService, PermissionRepository permissionRepository) {
        this.permissionService = permissionService;
        this.permissionRepository = permissionRepository;
    }

    @BeforeEach
    void initPermissionList() {
        logger.info("初始化权限对象。。。");
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
        logger.info("初始化权限对象成功。。。");
    }


    @Test
    @Transactional
    void testPermissionService() {
        logger.info("========== 开始测试 PermissionService 接口 ==========");
        permissionRepository.deleteAll();
        //gen
        assertEquals(1, permissionService.generateNextPermissionLocation());
        // add
        permissionService.addPermission(p1);
        assertEquals(2, permissionService.generateNextPermissionLocation());
        assertNotNull(permissionService.findPermissionById(p1.getId()));
        permissionService.addPermission(p2);
        permissionService.addPermission(p3);

        //update
        String NEW_NAME = "TEST_P1_NAME_5839";
        Permission update = new Permission();
        update.setId(p1.getId());
        update.setPermissionName(NEW_NAME);
        permissionService.updatePermission(update);
        Permission updated = permissionService.findPermissionById(update.getId());
        assertEquals(NEW_NAME, updated.getPermissionName());
        assertEquals(p1.getPermissionLocation(), updated.getPermissionLocation());

        //delete
        permissionService.deletePermission(p1.getId());

        //find
        assertNull(permissionService.findPermissionById(p1.getId()));

        assertEquals(2, permissionService.findPermissionsByIdList(List.of(p2.getId(), p3.getId())).size());

        assertEquals(2, permissionService.findAll().size());

        assertEquals(AuthUtil.generatePermissionChain(p2.getPermissionLocation()), permissionService.findPermissionCode(p2.getId()));

        PageRequest pageable = PageRequest.of(0, 1);

        Permission example = new Permission();
        example.setResources("api");
        GenericPage<Permission> permissionPaged = permissionService.findPermissionPaged(example, pageable);
        assertEquals(2, permissionPaged.getTotalPages());
        assertEquals(1, permissionPaged.getRecords().size());
        assertEquals(2, permissionPaged.getTotalElements());

    }
}
