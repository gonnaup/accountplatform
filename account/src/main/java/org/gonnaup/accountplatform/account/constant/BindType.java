package org.gonnaup.accountplatform.account.constant;

import org.gonnaup.accountplatform.account.domain.Select;

import java.util.Arrays;
import java.util.List;

/**
 * 账号绑定类型
 *
 * @author gonnaup
 * @version created at 2023/7/5 下午12:01
 */
public enum BindType {

    Email("E", "邮箱");
    //    Phone("P", "手机号码"),
//    WeChat("W", "微信"),
//    QQ("Q", "QQ"),
//    Github("G", "Github"),
//    AliPay("A", "支付宝");
    public final String value;

    public final String label;

    BindType(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static List<Select> toSelectList() {
        BindType[] states = values();
        return Arrays.stream(states).map(s -> Select.of(s.value, s.label)).toList();
    }
}
