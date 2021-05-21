package com.common.exception;

/**
 * description: ClientIPException <br>
 * date: 2019/9/11 10:05 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
public class ClientIpException extends BaseException {
    public ClientIpException(String message) {
        super(message);
    }

    public ClientIpException(String message, Throwable cause) {
        super(message, cause);
    }
}
