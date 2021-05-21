package com.common.exception;

/**
 * description: EsDaoException <br>
 * date: 2019/2/13 16:40 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
public class StateNotFountException extends BaseException {
    public StateNotFountException(String message) {
        super(message);
    }

    public StateNotFountException(String message, Throwable cause) {
        super(message, cause);
    }
}

