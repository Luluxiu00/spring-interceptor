package com.ssm.aspect;


import org.apache.log4j.Logger;
import org.apache.shiro.util.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by 326944 on 2017/7/17.
 */
@Component
@Aspect
public class AccessStatisticsAspect {
    //记录日志
    private static final Logger logger = Logger.getLogger(AccessStatisticsAspect.class);

    @Autowired(required = false)
    private HttpServletRequest request;

    /**
     * 配置切入点,该方法无方法体,主要为方便同类中其他方法使用此处配置的切入点
     */
    @SuppressWarnings("unused")
    @Pointcut("execution(* com.ssm.controller.*.*(..))")
    private void aspect() {
    }

    /**
     * 记录访问内容
     *
     * @param point
     */
    @Before("aspect()")
    public void before(JoinPoint point) {
        System.out.println("---------------------2344555-----------------------------------------------");
       try {
            //获取切入点的signatrue
            Signature signature = point.getSignature();
            //类名
            String className = signature.getDeclaringTypeName();

            point.getTarget();

            RequestMapping requestMapping = Class.forName(className).getAnnotation(RequestMapping.class);
            String[] modules = requestMapping.value();
            if (modules != null) {

                //登录登出不做统计
                String methodName = frontSubStr(signature.getName(), 20);
               /* if ("login".equals(methodName) || "logout".equals(methodName))
                    return;*/

                //访问类名
                System.out.println("访问类名: "+className);
                //访问方法名
                System.out.println("访问方法名: " +methodName);
                //请求参数列表
                Object[] requestParams = point.getArgs();
                //方法参数列表
                String paramStr = StringUtils.toString(requestParams);
                System.out.println("参数: " + paramStr);


                //模块名（requestMapping的value），统一格式“/" + modelName"
                String modelName = StringUtils.toString(modules);
                if (modelName.endsWith("/"))
                    modelName = modelName.substring(0, modelName.length() - 1);
                if (!modelName.startsWith("/"))
                    modelName = "/" + modelName;
                //模块名
                System.out.println("模块名: " + modelName);
                //访问时间
                System.out.println("访问时间: " + new Date());
                //访问类型app/pc
                System.out.println("访问IP: " + getVisitorIp());
            }
        } catch (Exception e) {
            logger.error("[ before ] ErrorInfo : " + e.getMessage());
        }
    }

    /**
     * 通过request 获取真实ip
     *
     * @return 返回访问者 ip
     */
    private String getVisitorIp() {
        String ip = request.getHeader("Proxy-Client-IP");
        if (isEmpty(ip))
            ip = request.getHeader("WL-Proxy-Client-IP");
        if (isEmpty(ip))
            ip = request.getRemoteAddr();
        return ip;
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    private boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 截取字符串前n-3个字符
     *
     * @param str
     * @param n
     * @return
     */
    private String frontSubStr(String str, int n) {
        //字符串为空则返回“’
        if (str == null || str.isEmpty()) {
            return "";
        }
        //截取字符串前n-3个字符，并添加。。。
        return n >= str.length() ? str : str.substring(0, n - 3) + "...";
    }
}
