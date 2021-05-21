/**
 * @Title: ServiceContractInfoDto.java
 * @Package com.sxgis.dto.service
 * @Company: 陕西省基础地理信息中心
 * @author zhangzhe
 * @date 2017年5月11日
 * @reviser
 * @date
 * @version V1.0
 */
package com.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wwj
 * @ClassName: AddFeatureeData
 * @Description: 项目红线增加要素
 * @date 2021年5月9日
 */
@Data
@ApiModel(value = "项目红线增加要素")
public class AddFeatureeData {
    @ApiModelProperty(value = "流水号")
    private int lsh ;
    @ApiModelProperty(value = "项目名称")
    private String xmmc;
    @ApiModelProperty(value = "项目代码")
    private String xmdm;
    @ApiModelProperty(value = "项目范围，geojson FeatureCollection")
    private String xmfw;

}
