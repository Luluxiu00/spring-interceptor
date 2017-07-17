package com.ssm.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 326944 on 2017/7/17.
 */
public class CookieUtils {
    public static final String NAME="loginName";
    public static final Integer TIME = 1800;

    /**
     * 获得cookie
     * @return
     */
    public static String getCookie(HttpServletRequest req, String name){
        Cookie[] cookie=req.getCookies();
        if(cookie !=null && cookie.length > 0){
            for(Cookie ck : cookie){
                if(ck.getName().equals(name)){
                    return ck.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 设置cookie
     * @param response
     * @param loginName
     */
    public static void setCookie(HttpServletResponse response, String loginName){
        //设置cookie 不同域名共享
        Cookie cookie=new Cookie(NAME,loginName);
        cookie.setMaxAge(TIME);
        response.addCookie(cookie);
    }
}
