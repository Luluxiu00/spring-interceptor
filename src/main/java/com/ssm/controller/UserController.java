package com.ssm.controller;

import com.alibaba.fastjson.JSONObject;
import com.ssm.pojo.User;
import com.ssm.service.UserService;
import com.ssm.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 326944 on 2017/7/17.
 */

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ShardedJedisPool shardedJedisPool;

    @RequestMapping("/")
    public String login(){
        return "login";
    }

    /**
     * 登录
     * @param response
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ModelAndView login(HttpServletResponse response, String username, String password){
        ShardedJedis jedis = shardedJedisPool.getResource();
        User loginUser = userService.login(username,password);
        ModelAndView mv = new ModelAndView();
        if (loginUser != null){
            //保存信息到redis中
            jedis.setex(username,1800, JSONObject.toJSONString(loginUser));
            CookieUtils.setCookie(response,username);
            mv.setViewName("success");
        }else{
            mv.addObject("errorMsg","用户名或密码错误!");
            mv.setViewName("login");
        }
        //关闭连接
        if (jedis != null){
            jedis.close();
        }
        return mv;
    }

    /**
     * 退出
     * @param req
     * @return
     */
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public String login(HttpServletRequest req){
        ShardedJedis jedis = shardedJedisPool.getResource();
        String cookie = CookieUtils.getCookie(req, CookieUtils.NAME);
        jedis.del(cookie);
        return "login";
    }



    @RequestMapping(value = "/show",method = RequestMethod.GET)
    public String show(){
        return "success";
    }
}
