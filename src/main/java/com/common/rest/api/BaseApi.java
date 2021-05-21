package com.common.rest.api;

import java.util.List;
import java.util.Map;

import com.common.enums.ResultCode;
import com.common.rest.result.Result;
import com.common.rest.result.ResultItems;
import com.common.rest.result.ResultRecord;
import com.common.utils.WebUtils;
import com.common.web.BaseWebController;

import javax.servlet.http.HttpServletRequest;

/**
 * description: 控制器基类 <br>
 * date: 2019/2/14 9:40 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
public class BaseApi extends BaseWebController {
    protected static final String CODE = "code";
    protected static final String ITEMS = "items";
    protected static final String RECORD = "record";
    protected static final String INFO = "info";
    protected static final String TOTAL = "total";

    public BaseApi() {
    }

    protected Result getResult(ResultCode code) {
        Result result = new Result(code.code(), code.info());
        return result;
    }

    protected Result getResult(ResultCode code, String info) {
        Result result = new Result(code.code(), info);
        return result;
    }

    protected <T> ResultRecord<T> getResult(ResultCode code, T record) {
        ResultRecord result = new ResultRecord(code.code(), code.info(), record);
        return result;
    }

    protected <T> ResultItems<T> getResult(ResultCode code, List<T> items) {
        ResultItems result = new ResultItems(code.code(), code.info(), items);
        return result;
    }

    protected <T> ResultItems<T> getResult(ResultCode code, List<T> items, long total) {
        ResultItems result = new ResultItems(code.code(), code.info(), items, total);
        result.setItems(items);
        return result;
    }

    protected <T> ResultItems<T> getResult(ResultCode code, List<T> items, int page, long total, int totalPage) {
        ResultItems result = new ResultItems(code.code(), code.info(), items, page, total, totalPage);
        result.setItems(items);
        return result;
    }

    @Override
    protected Map<String, Object> getParams(HttpServletRequest request) {
        return WebUtils.getParamsToMap(request);
    }
}
