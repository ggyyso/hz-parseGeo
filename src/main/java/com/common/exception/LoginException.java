package com.common.exception;

/**
 * description: LoginException <br>
 * date: 2019/10/8 16:53 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
public class LoginException extends BaseException {
    public LoginException(String message) {
        super(message);
    }

    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
