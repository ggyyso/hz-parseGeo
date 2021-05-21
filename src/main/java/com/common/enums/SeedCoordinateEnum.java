package com.common.enums;

import lombok.Getter;

/**
 * @Description 切片坐标系枚举
 * @Author JinQuan--WT1321
 * @Date 2018/12/22
 * @Version Copyright (c) 2018,安徽皖通科技股份有限公司 All rights reserved.
 */
@Getter
public enum SeedCoordinateEnum {

    /**
     * 切片坐标系
     */
    EPSG900913(1, "EPSG:900913"),
    EPSG4326(2, "EPSG：4326");

    private Integer code;

    private String message;

    SeedCoordinateEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
