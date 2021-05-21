package com.common.exception;

/**
 * description: NetException <br>
 * date: 2020/11/23 9:40 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
public class NetException extends BaseException {
    public NetException(String message) {
        super(message);
    }

    public NetException(String message, Throwable cause) {
        super(message, cause);
    }
}