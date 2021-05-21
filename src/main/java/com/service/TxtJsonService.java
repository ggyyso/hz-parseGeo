package com.service;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.List;

/**
 * description: TxtJsonService <br>
 * date: 2021/2/23 18:33 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
public interface TxtJsonService {
    /**
     * 解析txt为GeoJson
     *
     * @param txtFilePath
     * @return String
     */
    String txt2GeoJson(String txtFilePath,String epsg);

    /**
     * 验证上传文件是否包含txt文件
     *
     * @param filePath
     * @param destiPath 必须以路径分隔符结尾
     * @return String
     */
    String validateUploadZip(String filePath, String destiPath);

    /**
     * dwg文件解析
     * @param filePath
     * @param dwgPath
     * @param dxfPath
     * @param epsg 数据空间参考wkid 如4326
     * @return
     */
    String parseDwg(String filePath,String dwgPath,String dxfPath,String epsg) throws FactoryException, TransformException, IOException;
}