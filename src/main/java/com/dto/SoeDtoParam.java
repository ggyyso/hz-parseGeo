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

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangzhe
 * @ClassName: SoeDtoParam
 * @Description: 服务请求参数
 * @date 2021年2月26日
 */
@Getter
@Setter
public class SoeDtoParam {
    private String projectInfos = "";
    private String checkFeatures = "";
    private String layers = "";
    private String servicesfields = "";
    private String sr = "";
    private String f = "";
}
