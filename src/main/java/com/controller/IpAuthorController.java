package com.controller;

import com.common.enums.ResultEnum;
import com.common.utils.ResultVOUtil;
import com.service.IPAuthorService;
import com.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Jason Wong
 * @title: IpAuthorController
 * @projectName hz-parseGeo
 * @description: TODO
 * @date 2021/5/12 15:30
 */

@RestController
@RequestMapping("/ip")
@Api(tags = "ip注册与认证")
public class IpAuthorController {
    @Autowired
    IPAuthorService ipAuthorService;

    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "增加IP认证")
    public ResultVO AddIp(@RequestParam( name = "ip")  String ip) throws Exception {
        ipAuthorService.add(ip);
        return ResultVOUtil.success();
    }
    @ResponseBody
    @RequestMapping(value = "/author", method = RequestMethod.POST)
    @ApiOperation(value = "增加IP认证")
    public ResultVO Author(@RequestParam( name = "ip")  String ip) throws Exception {
       boolean result= ipAuthorService.author(ip);
       if (result){
           return ResultVOUtil.success();
       }else {
           return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
       }

    }
}
