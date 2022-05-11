package com.simo.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.simo.reggie.commons.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*" )
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器
    public static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        //本次请求的uri
        String requestURI = request.getRequestURI();
        //定义不需要处理的uri
        String[] uris = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login"
        };
        //判断当前请求是否需要处理
        boolean check = check(uris, requestURI);
            //不需要处理则返回
        if(check){
            filterChain.doFilter(request,response);
            return;
        }
            //需要处理则继续向下执行
        //处理请求
        //查看当前请求有没有登录
        if(request.getSession().getAttribute("employee") != null){
            Long empId = (Long)request.getSession().getAttribute("employee");
            threadLocal.set(empId);
            filterChain.doFilter(request,response);
            return;
        }

        if(request.getSession().getAttribute("user") != null){
            Long userId = (Long)request.getSession().getAttribute("user");
            threadLocal.set(userId);
            filterChain.doFilter(request,response);
            return;
        }

        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

        //登录了就通过
            //没有登录则告诉前端
    }
    public boolean check(String[] strs,String uri){
        for(String str : strs){
            boolean match = ANT_PATH_MATCHER.match(str, uri);
            if(match == true){
                return true;
            }
        }
        return false;
    }
}
