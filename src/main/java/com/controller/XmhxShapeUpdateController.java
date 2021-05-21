package com.controller;

import com.dto.AddFeatureeData;
import com.service.XmhxShpUpdateService;
import com.common.utils.ResultVOUtil;
import com.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/qypg/ydhx")
@Api(tags = "区域评估用地红线Shapefile增加要素")
public class XmhxShapeUpdateController {
    @Autowired
    protected HttpServletRequest request;
    @Autowired
    XmhxShpUpdateService xmhxShpUpdateService;


    /**
     * 区域评估YouMapServer shapefile增加要素
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addFeature", method = RequestMethod.POST)
    @ApiOperation(value = "增加要素", notes = "{\"lsh\":\"ccc\",\"xmmc\":\"aaa\",\"xmdm\":\"xx-xxx\"," +
            "\"xmfw\":\"geojson FeatureCollection\"}")
    public ResultVO addFeature(@RequestBody AddFeatureeData info) throws Exception {
        xmhxShpUpdateService.addFeature(info);
        return ResultVOUtil.success();
    }


    /**
     *
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/dijiCheck", method = RequestMethod.POST)
    @ApiOperation(value = "地籍信息查询", notes = "{\"xmfw\":\"xmfwString\"}")
    public ResultVO dijiInfoCheck(@RequestBody Object info) throws Exception {
       String dijiInfo= xmhxShpUpdateService.dijiInfoCheck(info);
        return ResultVOUtil.success(dijiInfo,1);
    }

}
