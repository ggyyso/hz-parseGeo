package com.common.utils;


import com.common.enums.ResultEnum;
import com.vo.ResultVO;
import org.springframework.stereotype.Component;

/**
 * @Description 返回结果工具类
 * @Author JinQuan--WT1321
 * @Date 2018/11/20
 * @Version 0.0.1
 */
@Component
public class ResultVOUtil {


    /**
     * @return
     * @Description 无返回结果成功
     * @Author JinQuan--WT1321
     */
    public static ResultVO success() {
        return success(null, 0);
    }

    /**
     * @param data 数据
     * @return
     * @Description 有返回结果返回
     * @Author JinQuan--WT1321
     */
    public static ResultVO success(Object data, int count) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(ResultEnum.SUCCESS.getCode());
        resultVO.setMsg(ResultEnum.SUCCESS.getMsg());
        resultVO.setCount(count);
        resultVO.setData(data);
        return resultVO;
    }

    /**
     * @param code 状态码
     * @param msg  消息
     * @return
     * @Description 成功返回
     * @author Zhang Yongtai
     */
    public static ResultVO success(Integer code, String msg) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }

    /**
     * @param code 状态码
     * @param msg  消息
     * @return
     * @Description 错误返回
     * @Author JinQuan--WT1321
     */
    public static ResultVO error(Integer code, String msg) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }

    /**
     * @param resultEnum 枚举对象
     * @return
     * @Description 错误返回
     * @Author JinQuan--WT1321
     */
    public static ResultVO error(ResultEnum resultEnum) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(resultEnum.getCode());
        resultVO.setMsg(resultEnum.getMsg());

        return resultVO;
    }
}
