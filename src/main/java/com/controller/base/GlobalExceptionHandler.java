package com.controller.base;

import com.common.enums.ResultCode;
import com.common.exception.*;
import com.common.rest.api.BaseApi;
import com.common.rest.result.Result;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * description: SendMailException <br>
 * date: 2019/2/22 16:40 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler extends BaseApi {

    /**
     * description: 状态转换异常 <br>
     * author: zhangzhe <br>
     * date: 2019/10/14 8:52 <br>
     *
     * @param ex
     * @return com.sxgis.field.common.rest.result.Result
     */
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(StateNotFountException.class)
    public Result stateNotFountExceptionHandler(StateNotFountException ex) {
        return this.getResult(ResultCode.STATE_ERROR, ex.getMessage());
    }

    /**
     * description: 参数验证异常 <br>
     * author: zhangzhe <br>
     * date: 2019/10/14 8:52 <br>
     *
     * @param ex
     * @return com.sxgis.field.common.rest.result.Result
     */
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ParamException.class)
    public Result paramExceptionHandler(ParamException ex) {
        return this.getResult(ResultCode.PARAM_ERROR, ex.getMessage());
    }

    /**
     * description: 文件操作异常 <br>
     * author: zhangzhe <br>
     * date: 2019/10/14 8:52 <br>
     *
     * @param ex
     * @return com.sxgis.field.common.rest.result.Result
     */
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(FileOptionException.class)
    public Result fileOptionExceptionHandler(FileOptionException ex) {
        return this.getResult(ResultCode.FILE_ERROR, ex.getMessage());
    }

    /**
     * description: 实体不存在异常 <br>
     * author: zhangzhe <br>
     * date: 2019/10/14 8:52 <br>
     *
     * @param ex
     * @return com.sxgis.field.common.rest.result.Result
     */
//    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public Result entityNotFoundExceptionHandler(EntityNotFoundException ex) {
        return this.getResult(ResultCode.ENTITY_ERROR, ex.getMessage());
    }

    /**
     * description: 数据操作失败异常 <br>
     * author: zhangzhe <br>
     * date: 2019/10/14 8:51 <br>
     *
     * @param ex
     * @return com.sxgis.field.common.rest.result.Result
     */
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataOptionException.class)
    public Result dataConverterExceptionHandler(DataOptionException ex) {
        return this.getResult(ResultCode.DATA_ERROR, ex.getMessage());
    }

    /**
     * description: 数据转换失败异常 <br>
     * author: zhangzhe <br>
     * date: 2019/10/14 8:51 <br>
     *
     * @param ex
     * @return com.sxgis.field.common.rest.result.Result
     */
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataConverterException.class)
    public Result dataConverterExceptionHandler(DataConverterException ex) {
        return this.getResult(ResultCode.CONVERT_ERROR, ex.getMessage());
    }

    /**
     * description: 删除失败异常 <br>
     * author: zhangzhe <br>
     * date: 2019/10/14 8:51 <br>
     *
     * @param ex
     * @return com.sxgis.field.common.rest.result.Result
     */
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(CanNotDeleteException.class)
    public Result canNotDeleteExceptionHandler(CanNotDeleteException ex) {
        return this.getResult(ResultCode.DELETE_ERROR, ex.getMessage());
    }

    /**
     * description: 暂时无法定位的异常 <br>
     * author: zhangzhe <br>
     * date: 2019/10/14 8:53 <br>
     *
     * @param ex
     * @return com.sxgis.field.common.rest.result.Result
     */
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public Result httpMediaTypeNotAcceptableExceptionHandler(
            HttpMediaTypeNotAcceptableException ex) {
        return this.getResult(ResultCode.UNKNOWN_ERROR, ex.getMessage());
    }

    /**
     * description: token验证失败 <br>
     * author: zhangzhe <br>
     * date: 2020/4/27 15:19 <br>
     *
     * @param ex
     * @return com.sxgis.field.common.rest.result.Result
     */
    @ExceptionHandler(JwtCustomException.class)
    public Result jwtCustomExceptionHandler(
            JwtCustomException ex) {
        return this.getResult(ResultCode.CONVERT_ERROR, ex.getMessage());
    }

    /**
     * description: 其它异常 <br>
     * author: zhangzhe <br>
     * date: 2019/10/14 8:53 <br>
     *
     * @param ex
     * @return com.sxgis.field.common.rest.result.Result
     */
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(Exception ex) {
        return this.getResult(ResultCode.UNKNOWN_ERROR, ex.getMessage());
    }
}
