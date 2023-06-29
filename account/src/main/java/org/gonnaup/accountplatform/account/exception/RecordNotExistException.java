package org.gonnaup.accountplatform.account.exception;

import java.io.Serial;

/**
 * 记录不存在异常
 *
 * @author gonnaup
 * @version created at 2023/6/29 下午3:12
 */
public class RecordNotExistException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -4291367831508535204L;

    public RecordNotExistException(String message) {
        super(message);
    }
}
