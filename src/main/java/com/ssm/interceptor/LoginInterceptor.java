package com.ssm.interceptor;

import com.ssm.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 326944 on 2017/7/17.
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private ShardedJedisPool shardedJedisPool;

    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //获取请求的URL
        String url = httpServletRequest.getRequestURI();
        //URL:login.jsp是公开的;这个demo是除了login.jsp是可以公开访问的，其它的URL都进行拦截控制
        if(url.indexOf("login.do")>=0){
            return true;
        }
        //获取登录信息
        String cookie = CookieUtils.getCookie(httpServletRequest, CookieUtils.NAME);
        ShardedJedis jedis = shardedJedisPool.getResource();
        String user =  jedis.get(cookie);
        if(user != null){
            return true;
        }
        if(jedis != null){
            jedis.close();
        }
        //不符合条件的，跳转到登录界面
        httpServletRequest.getRequestDispatcher("/WEB-INF/view/login.html").forward(httpServletRequest, httpServletResponse);
        return false;
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
