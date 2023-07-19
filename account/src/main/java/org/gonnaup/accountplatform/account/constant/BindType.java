package org.gonnaup.accountplatform.account.constant;

import org.gonnaup.accountplatform.account.domain.Select;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账号绑定类型
 *
 * @author gonnaup
 * @version created at 2023/7/5 下午12:01
 */
public enum BindType {

    Email("E", "邮箱"),
    Phone("P", "手机号码"),
    WeChat("W", "微信"),
    QQ("Q", "QQ"),
    Github("G", "Github"),
    AliPay("A", "支付宝");

    public final String value;

    public final String label;

    private final static Map<String, BindType> valueDic;

    static {
        BindType[] values = values();
        valueDic = new HashMap<>(values.length);
        Arrays.stream(values).forEach(o -> valueDic.put(o.value, o));
    }

    BindType(String value, String label) {
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
    public static BindType fromValue(String value) {
        BindType bindType = valueDic.get(value);
        if (bindType != null) {
            return bindType;
        }
        throw new IllegalArgumentException(
                "No enum value " + BindType.class.getCanonicalName() + "." + value);

    }

    public static List<Select> toSelectList() {
        BindType[] states = values();
        return Arrays.stream(states).map(s -> Select.of(s.value, s.label)).toList();
    }
}
