package org.gonnaup.accountplatform.account.constant;

import org.gonnaup.accountplatform.account.domain.Select;

import java.util.Arrays;
import java.util.List;

/**
 * 认证类型
 *
 * @author gonnaup
 * @version created at 2023/7/5 下午12:01
 */
public enum AuthenticationType {

    Name("N", "用户名密码"),
    Email("E", "邮箱");
    public final String value;

    public final String label;

    AuthenticationType(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static List<Select> toSelectList() {
        AuthenticationType[] states = values();
        return Arrays.stream(states).map(s -> Select.of(s.value, s.label)).toList();
    }
}
