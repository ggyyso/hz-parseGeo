package com.controller.base;

import com.model.TUser;
import com.common.exception.LoginException;
import com.common.rest.api.BaseApi;

import javax.servlet.http.HttpServletRequest;

/**
 * description: AbstractResult API 基类<br>
 * date: 2019-08-15 <br>
 * author: zhangzhe <br>
 * version: 1.0 <br>
 */
public class MyBaseApi extends BaseApi {

    /**
     * description: 获取当前已登录的用户 <br>
     * author: zhangzhe <br>
     * date: 2019/10/23 17:22 <br>
     *
     * @param request
     * @return com.sxgis.field.mdel.SecurityUser
     */
    protected TUser getloginUser(HttpServletRequest request) {
        TUser user = (TUser) request.getSession()
                .getAttribute("user");
        if (user == null) {
            throw new LoginException("用户未登录或者登录已过期");
        }

        return user;
    }

    /**
     * description: 获取当前已登录的用户 <br>
     * author: zhangzhe <br>
     * date: 2019/10/23 17:22 <br>
     *
     * @param request
     * @return com.sxgis.field.model.SecurityUser
     */
    protected TUser getloginUserOrNull(HttpServletRequest request) {
        TUser user = (TUser) request.getSession()
                .getAttribute("user");
        if (user == null) {
            return new TUser();
        }

        return user;
    }
}
