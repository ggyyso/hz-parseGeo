package com.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户表
 */
@Data
public class TUser implements Serializable {
    private Integer id;

    private String name;

    private String namecn;

    private String pwd;

    private String phone;

    private String tel;

    private Integer deleted;
}