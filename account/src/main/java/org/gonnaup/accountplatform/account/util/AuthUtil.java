package org.gonnaup.accountplatform.account.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 认证和授权习惯工具类
 * <p>
 * 权限码在位运算设计中由多个长整形拼接而成，此种权限码称为权限链
 * <p>
 * 权限位指在某个bit位有权限
 *
 * @author gonnaup
 * @version created at 2023/7/1 上午9:15
 */
public class AuthUtil {

    private static final Logger logger = LoggerFactory.getLogger(AuthUtil.class);

    /* 权限表示：长整形数字64位，有63位可用于权限位声明，每位数字1代表有权限，0代表无权限。
     * 当系统权限多于63种时，可使用长整形数字列来解决，每个长整形数字用'#'隔开，如'86868$96969'包含
     * 两个64位整数，86868代表1~63权限位，96969代表64~126权限位，以此类推。
     */
    private static final int LONG_MAX_PERMISSION_BIT = 63;

    // 权限链分隔符
    private static final String PERMISSION_CHAIN_SPLITER = "#";

    // 权限码数字进制
    private static final int PERMISSION_CODE_RADIX = 16;

    private AuthUtil() {
        throw new AssertionError("no AuthUtil instance for you!");
    }

    /**
     * 根据单个权限位生成权限链
     *
     * @param permissionLocation 权限链
     * @return
     */
    public static String generatePermissionChain(int permissionLocation) {
        if (permissionLocation < 1) {
            throw new IllegalArgumentException("权限位数字必须大于1");
        }
        if (permissionLocation <= LONG_MAX_PERMISSION_BIT) {
            return Long.toHexString(1L << (permissionLocation - 1));
        } else {
            // 位于第n串
            int chainN = permissionLocation / LONG_MAX_PERMISSION_BIT;
            // 第n串第m位
            int loc = permissionLocation % LONG_MAX_PERMISSION_BIT;

            if (loc == 0) {
                //整除,为前一串最后一位
                chainN--;
                loc = LONG_MAX_PERMISSION_BIT;
            }

            String lastChain = Long.toHexString(1L << (loc - 1));
            String emptyCode = '0' + PERMISSION_CHAIN_SPLITER;
            return emptyCode.repeat(chainN) + lastChain;
        }
    }

    /**
     * 根据权限位列表生成权限链
     *
     * @param permissionLocations
     * @return
     */
    public static String generatePermissionChain(List<Integer> permissionLocations) {
        if (permissionLocations.isEmpty()) {
            logger.error("多个权限位生成权限链时，权限位列表不能为空");
            throw new IllegalArgumentException("error.permissionchain.generate.emptyPermissionLocationList");
        }
        if (permissionLocations.size() == 1) {
            return generatePermissionChain(permissionLocations.get(0));
        }
        return permissionLocations.stream().map(AuthUtil::generatePermissionChain).reduce((p1, p2) -> mergePermissonChain(p1, p2)).get();
    }


    /**
     * 合并权限链列表
     *
     * @param permissionChains
     * @return
     */
    public static String mergePermissionChainList(List<String> permissionChains) {
        if (permissionChains.isEmpty()) {
            logger.error("进行权限链列表合并时，列表不能为空");
            throw new IllegalArgumentException("error.permissionchain.merge.emptyList");
        }
        if (permissionChains.size() == 1) {
            return permissionChains.get(0);
        }
        return permissionChains.stream().reduce((p1, p2) -> mergePermissonChain(p1, p2)).get();
    }

    /**
     * 给已拥有的权限链授权
     *
     * @param ownPermissionChain      已拥有的权限链
     * @param grantedPermissionChains 新授予的权限链
     * @return 授权后的权限链
     */
    public static String grantPermission(String ownPermissionChain, List<String> grantedPermissionChains) {
        if (grantedPermissionChains.isEmpty()) {
            return ownPermissionChain;
        }
        if (grantedPermissionChains.size() == 1) {
            return mergePermissonChain(ownPermissionChain, grantedPermissionChains.get(0));
        }
        return mergePermissonChain(ownPermissionChain, mergePermissionChainList(grantedPermissionChains));
    }

    /**
     * 给<code>ownPermissionChain</code>授予<code>grantedPermissionChain</code>权限
     *
     * @param ownPermissionChain     拥有的权限链
     * @param grantedPermissionChain 新授予权限链
     * @return 新权限链
     */
    public static String grantPermission(String ownPermissionChain, String grantedPermissionChain) {
        return grantPermission(ownPermissionChain, List.of(grantedPermissionChain));
    }

    /**
     * 移除权限中的某些权限
     *
     * @param ownPermissionChain     拥有的权限
     * @param removedPermissionChain 移除的权限
     * @return 移除权限后剩余的权限
     */
    public static String removePermission(String ownPermissionChain, String removedPermissionChain) {
        return removePermissionChain(ownPermissionChain, removedPermissionChain);
    }

    /**
     * 移除权限中的某列权限
     *
     * @param ownPermissionChain         拥有的权限
     * @param removedPermissionChainList 移除的权限列表
     * @return 移除权限列表后剩余的权限
     */
    public static String removePermission(String ownPermissionChain, List<String> removedPermissionChainList) {
        return removedPermissionChainList.stream().reduce(ownPermissionChain, AuthUtil::removePermissionChain);
    }

    /**
     * 判断权限链是否有目标权限链中的所有权限
     *
     * @param ownPermissionChain    拥有的权限链
     * @param targetPermissionChain 目标权限链
     * @return 拥有的权限链拥有目标权限链的所有权限返回true, 否则返回false
     */
    public static boolean hasPermission(String ownPermissionChain, String targetPermissionChain) {
        String[] owns = ownPermissionChain.split(PERMISSION_CHAIN_SPLITER);
        int ownsLength = owns.length;

        String[] targets = targetPermissionChain.split(PERMISSION_CHAIN_SPLITER);
        int targetsLength = targets.length;

        //目标权限链长于拥有权限链
        if (targetsLength > ownsLength) {
            for (int i = ownsLength; i < targetsLength; i++) {
                //超过部分含有至少一位权限
                if (parsePermissionCodeToLong(targets[i]) > 0) {
                    return false;
                }
            }
        }

        for (int i = 0; i < ownsLength; i++) {
            if (i < targetsLength && !hasPermissionSinglePermissionCode(owns[i], targets[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断权限链是否有权限链列表中所有权限
     *
     * @param ownPermissionChain
     * @param targetPermissionChainList
     * @return
     */
    public static boolean hasPermission(String ownPermissionChain, List<String> targetPermissionChainList) {
        return hasPermission(ownPermissionChain, mergePermissionChainList(targetPermissionChainList));
    }


    /******************** 验证工具方法 ***************/

    /**
     * 将基于16进制整数的权限链解析成基于二进制数的权限链
     * <p>
     * 用于进行可视化展示或测试验证
     * </p>
     *
     * @param permissionChain
     * @return
     */
    public static String parsePermissionChainToBinaryChain(String permissionChain) {
        String[] pm = permissionChain.split(PERMISSION_CHAIN_SPLITER);
        return Arrays.stream(pm).map(AuthUtil::parsePermissionCodeToLong).map(Long::toBinaryString).reduce((p1, p2) -> String.join(PERMISSION_CHAIN_SPLITER, p1, p2)).orElse("0");
    }

    /**
     * 根据权限链生成权限链所有权限位
     *
     * @param permissionChain
     * @return
     */
    public static List<Integer> parsePermissionChainToPermissionLocationList(String permissionChain) {
        String[] pm = permissionChain.split(PERMISSION_CHAIN_SPLITER);
        int len = pm.length;
        List<Integer> locList = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            long code = parsePermissionCodeToLong(pm[i]);
            for (int j = 1; j <= LONG_MAX_PERMISSION_BIT; j++) {
                long c = 1L << (j - 1);
                if ((code & c) == c) {
                    locList.add(j + i * LONG_MAX_PERMISSION_BIT);
                }
            }
        }
        return locList;
    }


    /************************************ private method  ***************************************/
    /************************************ private method  ***************************************/
    /************************************ private method  ***************************************/

    /**
     * 合并两个权限链
     *
     * @param ownPermissionChain     拥有的权限链
     * @param grantedPermissionChain 授权的权限链
     * @return 授权后的权限链
     */
    private static String mergePermissonChain(String ownPermissionChain, String grantedPermissionChain) {
        String[] owns = ownPermissionChain.split(PERMISSION_CHAIN_SPLITER);
        int ownsLength = owns.length;

        String[] granteds = grantedPermissionChain.split(PERMISSION_CHAIN_SPLITER);
        int grantedsLength = granteds.length;

        String[] merged = new String[Math.max(ownsLength, grantedsLength)];

        for (int i = 0; ; i++) {
            if (i < ownsLength && i < grantedsLength) {
                merged[i] = mergeSinglePermissionCode(owns[i], granteds[i]);
            }

            // own链较长
            if (i < ownsLength && i >= grantedsLength) {
                merged[i] = owns[i];
            }

            // granted链较长
            if (i >= ownsLength && i < grantedsLength) {
                merged[i] = granteds[i];
            }

            if (i >= ownsLength && i >= grantedsLength) {
                break;
            }
        }
        return String.join(PERMISSION_CHAIN_SPLITER, merged);
    }

    /**
     * 移除权限链中的权限
     *
     * @param ownPermissionChain
     * @param removedPermissionChain
     * @return
     */
    private static String removePermissionChain(String ownPermissionChain, String removedPermissionChain) {
        String[] owns = ownPermissionChain.split(PERMISSION_CHAIN_SPLITER);
        String[] removeds = removedPermissionChain.split(PERMISSION_CHAIN_SPLITER);
        int ownsLength = owns.length;
        int removedsLength = removeds.length;
        for (int i = 0; i < ownsLength; i++) {
            if (i < removedsLength) {
                owns[i] = removeSinglePermissionCode(owns[i], removeds[i]);
            } else {
                break;
            }
        }
        return String.join(PERMISSION_CHAIN_SPLITER, owns);
    }

    /**
     * 合并单个权限码，own | granted
     *
     * @param ownPermissionCode     已有权限码
     * @param grantedPermissionCode 待授予的权限码
     * @return 合并后的权限码
     */
    private static String mergeSinglePermissionCode(String ownPermissionCode, String grantedPermissionCode) {
        long own = parsePermissionCodeToLong(ownPermissionCode);
        long granted = parsePermissionCodeToLong(grantedPermissionCode);
        return Long.toHexString(own | granted);
    }

    /**
     * 移除权限码中的权限，own &= ~removed
     *
     * @param ownPermissionCode     权限码
     * @param removedPermissionCode 要移除的权限
     * @return 清除权限后的权限码
     */
    private static String removeSinglePermissionCode(String ownPermissionCode, String removedPermissionCode) {
        long own = parsePermissionCodeToLong(ownPermissionCode);
        long removed = parsePermissionCodeToLong(removedPermissionCode);
        return Long.toHexString(own & (~removed));
    }


    /**
     * 单个权限码情况下判断是否有权限，own & needed == needed
     *
     * @param ownPermissionCode    拥有的权限码
     * @param neededPermissionCode 需要包含的权限
     * @return 有权限则返回 true, 没有权限返回 false
     */
    private static boolean hasPermissionSinglePermissionCode(String ownPermissionCode, String neededPermissionCode) {
        long own = parsePermissionCodeToLong(ownPermissionCode);
        long needed = parsePermissionCodeToLong(neededPermissionCode);
        return (own & needed) == needed;
    }

    /**
     * 将16进制权限码转换成长整形数字
     *
     * @param permissionCode 权限码
     * @return 长整形权限码
     */
    private static long parsePermissionCodeToLong(String permissionCode) {
        return Long.parseLong(permissionCode, PERMISSION_CODE_RADIX);
    }


}
