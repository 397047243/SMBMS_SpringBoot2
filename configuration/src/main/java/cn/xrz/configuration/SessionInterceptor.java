package cn.xrz.configuration;

import cn.xrz.interceptor.UserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;

/**
 * @author XRZ
 * @date 2019-04-15
 * @Description : 配置类，注册拦截器
 */
@Configuration
public class SessionInterceptor implements WebMvcConfigurer {

    /**
     * 重写拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        ArrayList<String> list = new ArrayList<>();
        list.add("/favicon.ico");
        list.add("/index");
        list.add("/loginSub");
        list.add("/logout");
        list.add("/js/**");
        list.add("/css/**");
        list.add("/images/**");
        registry.addInterceptor(new UserInterceptor()) //拦截器的实现类
                .addPathPatterns("/**")                //拦截的请求
                .excludePathPatterns(list); //不拦截的请求
    }

}
