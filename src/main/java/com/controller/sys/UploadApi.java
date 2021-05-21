package com.controller.sys;

import com.config.UploadConfig;
import com.controller.base.MyBaseApi;
import com.dto.UploadResultData;
import com.service.TxtJsonService;
import com.common.enums.ResultCode;
import com.common.exception.FileOptionException;
import com.common.rest.result.ResultItems;
import com.common.rest.result.ResultRecord;
import com.common.utils.DateUtils;
import com.common.utils.GeojsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * Created by zzge163 on 2019/2/11.
 */
@RestController
@RequestMapping("/api/sys")
@Api(value = "UploadApi", tags = "文件上传解析接口")
public class UploadApi extends MyBaseApi {
    @Autowired
    private UploadConfig uploadConfig;

    @Autowired
    private TxtJsonService txtJsonService;

    @ResponseBody
    @RequestMapping(value = "/uploadfile", method = RequestMethod.POST)
    @ApiOperation(value = "上传单个文件",tags = "空间数据解析，支持四种类型，自然资源文本格式，普通文本格式（每行数据为y,x格式），dwg数据，zip压缩的shp文件")
    public ResultRecord<UploadResultData> postUploadFile(MultipartFile file,
                                                         @RequestParam( name = "wkid",defaultValue = "4490")  int epsg) throws Exception {
        if (file == null) {
            throw new FileOptionException("文件列表不能为空");
        }
        String fileName = DateUtils.formateDate(new Date())
                + "-" + RandomStringUtils.randomAlphabetic(8)
                + "-" + file.getOriginalFilename();
        File dest = new File(fileName);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new FileOptionException("文件上传失败", e);
        }

        // 解压检查是否是txt文件
        String folder = this.uploadConfig.getLocation();

        String filePath = folder + fileName;
        String fileFullName = dest.getName();
        String destiPath = folder + fileFullName.substring(0, fileFullName.lastIndexOf("."));
        String datas = "";
        //shp格式
        if (filePath.toLowerCase().endsWith("zip")) {
            String shpName = this.txtJsonService.validateUploadZip(
                    filePath, destiPath + File.separator);
            datas = GeojsonUtil.parseShpToGeojson(shpName);


        } else if (filePath.toLowerCase().endsWith("txt")) {
            // 将txt文件解析为geojson
            datas = this.txtJsonService.txt2GeoJson(filePath,String.valueOf(epsg));

        } else if (filePath.toLowerCase().endsWith("dwg")) {
            datas = txtJsonService.parseDwg(filePath, destiPath + File.separator + fileName, destiPath + File.separator +
                    "dxf",String.valueOf(epsg));

        } else {
            throw new Exception("读取文件格式错误，请检查文件内容是否正确！");
        }
        File delfile = new File(filePath);
        delfile.delete();
        File delFileFolder = new File(destiPath);
        delFileFolder.delete();
        // 组织返回结果
        UploadResultData result = new UploadResultData(fileName, datas);
        return this.getResult(ResultCode.SUCCESS, result);
    }

    // 不指定请求参数fileName会报错
    @ResponseBody
    @RequestMapping(value = "/uploadfiles", method = RequestMethod.POST)
    @ApiOperation(value = "上传多个文件")
    public ResultItems<Map<String, String>> postUploadFiles(@RequestParam("file") MultipartFile[] files) {
        if (files == null || files.length == 0) {
            throw new FileOptionException("文件列表不能为空");
        }
        List<Map<String, String>> results = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();
                File dest = new File(fileName);
                file.transferTo(dest);
                Map<String, String> map = new HashMap<>(16);
                map.put("contentType", file.getContentType());
                map.put("fileName", file.getOriginalFilename());
                map.put("fileSize", file.getSize() + "Byte");

                results.add(map);
            }
        } catch (IOException e) {
            throw new FileOptionException("文件上传失败", e);
        }

        return this.getResult(ResultCode.SUCCESS, results);
    }

    @RequestMapping(value = "/download/{fileName:.+}", method = RequestMethod.GET)
    @ApiOperation(value = "下载单个文件")
    public void downLoad(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        if (StringUtils.isEmpty(fileName)) {
            throw new FileOptionException("文件名不能为空");
        }

        String filePath = uploadConfig.getLocation();
        File file = new File(filePath + File.separator + fileName);
        if (file.exists()) { //判断文件父目录是否存在
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);

            byte[] buffer = new byte[1024];
            FileInputStream fis = null; //文件输入流
            BufferedInputStream bis = null;

            OutputStream os = null; //输出流
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }

            } catch (IOException e) {
                throw new FileOptionException("文件下载失败", e);
            }
            try {
                bis.close();
                fis.close();
            } catch (IOException e) {
                throw new FileOptionException("文件流关闭失败", e);
            }
        }
    }
}
