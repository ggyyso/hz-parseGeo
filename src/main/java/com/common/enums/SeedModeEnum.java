package com.common.enums;

import lombok.Getter;

/**
 * @Description 切片方式枚举
 * @Author JinQuan--WT1321
 * @Date 2018/12/22
 * @Version Copyright (c) 2018,安徽皖通科技股份有限公司 All rights reserved.
 */
@Getter
public enum SeedModeEnum {
    /**
     * 切片方式
     */
    SEED(1, "Seed"),
    RESEED(2, "Reseed"),
    TRUNCATE(3, "Truncate");


    private Integer code;

    private String message;

    SeedModeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
