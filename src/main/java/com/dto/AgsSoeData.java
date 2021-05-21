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
 * @ClassName: AgsSoeData
 * @Description: 异步服务返回参数
 * @date 2021年2月26日
 */
@Getter
@Setter
public class AgsSoeData {
    private String json = "";
    private String doc = "";

    public AgsSoeData(String json, String doc) {
        this.json = json;
        this.doc = doc;
    }
}
