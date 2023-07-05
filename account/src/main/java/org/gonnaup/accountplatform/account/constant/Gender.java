package org.gonnaup.accountplatform.account.constant;

import org.gonnaup.accountplatform.account.domain.Select;

import java.util.Arrays;
import java.util.List;

/**
 * 性别
 *
 * @author gonnaup
 * @version created at 2023/7/5 上午11:50
 */
public enum Gender {
    Male("M", "男"),
    Female("F", "女");
    public final String value;

    public final String label;

    Gender(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static List<Select> toSelectList() {
        Gender[] states = values();
        return Arrays.stream(states).map(s -> Select.of(s.value, s.label)).toList();
    }
}
