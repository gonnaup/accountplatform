package org.gonnaup.accountplatform.account.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gonnaup
 * @version created at 2023/7/2 上午10:04
 */
@Deprecated
class AuthUtilTest {

    private static final Logger logger = LoggerFactory.getLogger(AuthUtilTest.class);

    List<Integer> permissionLocationLists = new ArrayList<>();

    @BeforeEach
    void init() {
        SecureRandom random = new SecureRandom();
        int locNum = random.nextInt(100, 300);//权限位个数
        Set<Integer> locSet = new HashSet<>();
        while (locSet.size() < locNum) {
            locSet.add(random.nextInt(2, 1000));
        }
        permissionLocationLists.clear();
        permissionLocationLists.addAll(locSet);
        logger.info("随机生成 {} 个不重复权限位", permissionLocationLists.size());
    }


    @RepeatedTest(10)
    void hasPermissionTest() {
        //生成 权限链
        String permissionChain = AuthUtil.generatePermissionChain(permissionLocationLists);

        // 对每个权限位进行验证
        assertTrue(permissionLocationLists.stream().map(AuthUtil::generatePermissionChain)
                .allMatch(c -> AuthUtil.hasPermission(permissionChain, c)));

        // 对每个不再权限位中的位置进行无权限验证
        Set<Integer> locSet = new HashSet<>(permissionLocationLists);

        assertTrue(IntStream.range(1, 1000).filter(l -> !locSet.contains(l))
                .mapToObj(AuthUtil::generatePermissionChain).noneMatch(c -> AuthUtil.hasPermission(permissionChain, c)));

        //对权限位列表进行验证
        assertTrue(AuthUtil.hasPermission(permissionChain, permissionLocationLists.stream().map(AuthUtil::generatePermissionChain).toList()));
        assertTrue(IntStream.range(1, 1000).filter(l -> !locSet.contains(l))
                .mapToObj(l -> {
                    List<Integer> loc0 = new ArrayList<>(permissionLocationLists);
                    //将不含权限的权限位添加至含权限的权限位列表
                    loc0.add(l);
                    return loc0;
                })
                .noneMatch(c -> AuthUtil.hasPermission(permissionChain, c.stream().map(AuthUtil::generatePermissionChain).toList())));
    }


    @RepeatedTest(5)
    void generatePermissionChainTestSingleArg() {
        permissionLocationLists.forEach(location -> {
            String permissionChain = AuthUtil.generatePermissionChain(location);
            List<Integer> locations = AuthUtil.parsePermissionChainToPermissionLocationList(permissionChain);
            //单个权限位生成权限时，生成的权限链只含一个权限位且权限位位置和生成权限链的参数值相等
            assertEquals(1, locations.size());
            assertEquals(location, locations.get(0));
        });
    }

    @RepeatedTest(5)
    void generatePermissionChainTestListArg() {
        String permissionChain = AuthUtil.generatePermissionChain(permissionLocationLists);
        List<Integer> locs = AuthUtil.parsePermissionChainToPermissionLocationList(permissionChain);
        Collections.sort(permissionLocationLists);
        Collections.sort(locs);
        //生成的权限链的权限位和参数权限位一一对应相等
        assertEquals(permissionLocationLists, locs);

    }

    @RepeatedTest(5)
    void mergePermissionChainListTest() {
        //生成随机权限链列表
        List<String> chainList = permissionLocationLists.stream().map(AuthUtil::generatePermissionChain).toList();
        //合并
        String merged = AuthUtil.mergePermissionChainList(chainList);

        // 列表中的全部有权限
        assertTrue(chainList.stream().allMatch(c -> AuthUtil.hasPermission(merged, c)));

        //列表外的全无权限
        Set<Integer> locSet = new HashSet<>(permissionLocationLists);
        List<String> noP = IntStream.range(1, 1000).filter(l -> !locSet.contains(l)).mapToObj(AuthUtil::generatePermissionChain).toList();
        assertTrue(noP.stream().noneMatch(c -> AuthUtil.hasPermission(merged, c)));
    }

    @RepeatedTest(5)
    void grantPermissionTest() {
        assertTrue(permissionLocationLists.size() >= 2);
        Integer l = permissionLocationLists.get(0);
        String ownPermission = AuthUtil.generatePermissionChain(l);
        permissionLocationLists = permissionLocationLists.subList(1, permissionLocationLists.size());
        Integer r = permissionLocationLists.get(0);
        String grant1 = AuthUtil.generatePermissionChain(r);
        //单个授权测试
        String granted1 = AuthUtil.grantPermission(ownPermission, grant1);
        //已授权的
        assertTrue(AuthUtil.hasPermission(granted1, grant1));
        //未授权的
        assertTrue(permissionLocationLists.stream().filter(l0 -> !Objects.equals(l0, r))
                .map(AuthUtil::generatePermissionChain).noneMatch(g -> AuthUtil.hasPermission(granted1, g)));

        //列表授权测试
        List<String> plist = permissionLocationLists.stream().map(AuthUtil::generatePermissionChain).toList();
        //授权
        String granted = AuthUtil.grantPermission(ownPermission, plist);
        //含权限列表断言
        assertTrue(plist.stream().allMatch(s -> AuthUtil.hasPermission(granted, s)));
        assertTrue(AuthUtil.hasPermission(granted, plist));
        //不含权限列表断言
        Set<Integer> locSet = new HashSet<>(permissionLocationLists);
        locSet.add(l);//自身权限位
        List<String> revList = IntStream.range(1, 1000).filter(l0 -> !locSet.contains(l0)).mapToObj(AuthUtil::generatePermissionChain).toList();
        assertTrue(revList.stream().noneMatch(s -> AuthUtil.hasPermission(granted, s)));
        assertFalse(AuthUtil.hasPermission(granted, revList));


    }


}