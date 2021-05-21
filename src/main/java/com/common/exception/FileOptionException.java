package com.common.exception;

/**
 * description: FileOptionException <br>
 * date: 2019/9/12 16:44 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
public class FileOptionException extends BaseException {
    public FileOptionException(String message) {
        super(message);
    }

    public FileOptionException(String message, Throwable cause) {
        super(message, cause);
    }
}

