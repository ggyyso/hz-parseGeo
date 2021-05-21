package com.common.enums;

import lombok.Getter;

/**
 * @Description 返回枚举封装
 * @Author JinQuan--WT1321
 * @Date 2018/11/20
 * @Version 0.0.1
 */
@Getter
public enum ResultEnum {
    /**
     * 成功
     */
    SUCCESS(200, "成功", 200),
    /**
     * 服务器错误
     */
    INTER_ERROR(500, "服务器", 200),

    PARAM_ERROR(3002, "参数异常", 3002);

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息体
     */
    private String msg;

    /**
     * 数量
     */
    private Integer count;


    ResultEnum(Integer code, String msg, Integer count) {
        this.code = code;
        this.msg = msg;
        this.count = count;
    }
}
