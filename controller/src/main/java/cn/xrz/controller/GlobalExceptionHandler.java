package cn.xrz.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author XRZ
 * @date 2019-04-12
 * @Description : 捕获全局异常
 *                注：默认异常在error文件夹下面的异常页面
 *
 * @ControllerAdvice是controller的一个辅助类，最常用的就是作为全局异常处理的切面类
 * @ControllerAdvice可以指定扫描范围
 * @ControllerAdvice约定了几种可行的返回值，如果是直接返回model类的话，需要使用@ResponseBody进行json转换
 *                  返回String，表示跳到某个view
 *                  返回modelAndView
 *                  返回model + @ResponseBody
 */
@Slf4j //lombok 支持的日志
@ControllerAdvice(basePackages = "cn.xrz.smbms.controller") //配置捕获的范围 （使用AOP 异常通知实现）
public class GlobalExceptionHandler {


    @ExceptionHandler(RuntimeException.class) //运行时异常
    public ModelAndView errorResult(RuntimeException e){

        // 纪录日志...
        log.error("500:系统错误=======>"+e.getMessage()); //直接使用log 纪录日志 注意需要安装lombok插件

        ModelAndView modelAndView = new ModelAndView("error/500");
        modelAndView.addObject("message",e.getMessage()); //设置错误信息
        return modelAndView;
    }

}
