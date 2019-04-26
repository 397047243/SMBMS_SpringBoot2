package cn.xrz.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author XRZ
 * @date 2019-04-15
 * @Description : 用户拦截器 用于验证用户是否登陆
 */
public class UserInterceptor implements HandlerInterceptor {

    /**
     * 进入Controller层之前拦截请求
     * @param request
     * @param response
     * @param handler 请求的目标对象
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Object userSession = request.getSession().getAttribute("userSession");
        if(null != userSession){
            System.err.println("============================>不拦截");
            return true;
        }
        System.err.println("============================>拦截");
//        response.sendRedirect("/index"); //返回首页
        request.setAttribute("error","登陆失效，请重新登陆！");
        request.getRequestDispatcher("index").forward(request,response);
        return false;
    }
}
