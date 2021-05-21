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
 * @ClassName: UploadResultData
 * @Description: 服务类型统计
 * @author zhangzhe
 * @date 2017年5月11日
 */
@Getter
@Setter
public class UploadResultData {
    private String fileName = "";
    private String datas = "";

    public UploadResultData(String fileName, String datas) {
        this.fileName = fileName;
        this.datas = datas;
    }
}
