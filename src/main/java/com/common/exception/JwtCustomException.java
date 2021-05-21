package com.common.exception;

/**
 * description: LoginException <br>
 * date: 2019/10/8 16:53 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
public class JwtCustomException extends BaseException {
    public JwtCustomException(String message) {
        super(message);
    }

    public JwtCustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
