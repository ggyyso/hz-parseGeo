package com.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description 统一返回结果封装
 * @Author JinQuan--WT1321
 * @Date 2018/11/20
 * @Version 0.0.1
 */
@ApiModel(value = "统一返回结果封装")
@Data
public class ResultVO {


    /**
     * 状态码
     */
    @ApiModelProperty(value = "状态码")
    private Integer code;

    /**
     * 消息体
     */
    @ApiModelProperty(value = "消息体")
    private String msg;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private Integer count;
    /**
     * 数据
     */
    @ApiModelProperty(value = "数据")
    private Object data;
}
