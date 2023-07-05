package org.gonnaup.accountplatform.account.constant;

import org.gonnaup.accountplatform.account.domain.Select;

import java.util.Arrays;
import java.util.List;

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

    AccountState(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static List<Select> toSelectList() {
        AccountState[] states = values();
        return Arrays.stream(states).map(s -> Select.of(s.value, s.label)).toList();
    }
}
