package org.gonnaup.accountplatform.account.constant;

import org.gonnaup.accountplatform.account.domain.Select;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 性别
 *
 * @author gonnaup
 * @version created at 2023/7/5 上午11:50
 */
public enum Gender {
    Male("M", "男"),
    Female("F", "女"),
    Privacy("P", "保密");
    public final String value;

    public final String label;

    private final static Map<String, Gender> valueDic;

    static {
        Gender[] values = values();
        valueDic = new HashMap<>(values.length);
        Arrays.stream(values).forEach(o -> valueDic.put(o.value, o));
    }

    Gender(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 使用value值获取枚举
     *
     * @param value value值
     * @return {@link Gender}
     * @throws IllegalArgumentException 枚举中不存在此value值
     */
    public static Gender fromValue(String value) {
        Gender gender = valueDic.get(value);
        if (gender != null) {
            return gender;
        }
        throw new IllegalArgumentException(
                "No enum value " + Gender.class.getCanonicalName() + "." + value);

    }

    public static List<Select> toSelectList() {
        Gender[] states = values();
        return Arrays.stream(states).map(s -> Select.of(s.value, s.label)).toList();
    }
}
