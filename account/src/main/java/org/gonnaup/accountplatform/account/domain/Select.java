package org.gonnaup.accountplatform.account.domain;

import java.util.List;

/**
 * 下拉框数据模型
 *
 * @param value 选项值
 * @param label 选项标签
 * @author gonnaup
 * @version created at 2023/7/5 上午10:25
 */
public record Select(String value, String label) {
    public static Select of(String value, String label) {
        return new Select(value, label);
    }
}
