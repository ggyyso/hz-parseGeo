package com.common.enums;

import lombok.Getter;

/**
 * @Description 切片类型枚举
 * @Author JinQuan--WT1321
 * @Date 2018/12/22
 * @Version Copyright (c) 2018,安徽皖通科技股份有限公司 All rights reserved.
 */
@Getter
public enum SeedTypeEnum {

    /**
     * 切片类型
     */
    PBF(1, "PBF"),
    PNG(2, "PNG");

    private Integer code;

    private String message;

    SeedTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
