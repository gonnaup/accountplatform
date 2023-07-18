package org.gonnaup.accountplatform.account.constant;

import org.gonnaup.accountplatform.account.domain.Select;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 帐号状态定义
 *
 * @author gonnaup
 * @version created at 2023/7/5 上午10:10
 */
public enum AccountState {
    Normal("N", "正常"),
    Locked("L", "锁定"),
    Disabled("D", "禁用"),
    Removed("R", "删除");
    public final String value;

    public final String label;

    private final static Map<String, AccountState> valueDic;

    static {
        AccountState[] values = values();
        valueDic = new HashMap<>(values.length);
        Arrays.stream(values).forEach(o -> valueDic.put(o.value, o));
    }

    AccountState(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 使用value值获取枚举
     *
     * @param value value值
     * @return {@link AccountState}
     * @throws IllegalArgumentException 枚举中不存在此value值
     */
    public static AccountState fromValue(String value) {
        AccountState state = valueDic.get(value);
        if (state != null) {
            return state;
        }
        throw new IllegalArgumentException(
                "No enum value " + AccountState.class.getCanonicalName() + "." + value);
    }

    public static List<Select> toSelectList() {
        AccountState[] states = values();
        return Arrays.stream(states).map(s -> Select.of(s.value, s.label)).toList();
    }
}
