package com.common.exception;

/**
 * description: VerifyCodeException <br>
 * date: 2019/9/12 16:54 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
public class VerifyCodeException extends BaseException {
    public VerifyCodeException(String message) {
        super(message);
    }

    public VerifyCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
