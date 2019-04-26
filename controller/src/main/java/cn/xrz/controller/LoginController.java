package cn.xrz.controller;

import cn.xrz.pojo.User;
import cn.xrz.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * @author XRZ
 * @date 2019\2\26 0026
 * @Description : 登录控制类
 */
@Controller
@SessionAttributes("userSession") //是声明该属性放在Session作用域中
public class LoginController {

    @Resource
    private UserService userService;



    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    /**
     * 登录方法  只接受POST请求
     * @param userCode
     * @param userPassword
     * @param model 用于在Session中保存数据
     * @return
     */
    @RequestMapping("/loginSub")
    public ModelAndView Login(@RequestParam("userCode") String userCode,
                              @RequestParam("userPassword") String userPassword, Model model) throws Exception {
        User user = this.userService.login(userCode,userPassword);
        if(null != user){
            model.addAttribute("userSession",user); //在Session中保存User
            return new ModelAndView("frame"); //跳转主页面
        }else{
            ModelAndView modelAndView = new ModelAndView("index"); //在ModelAndView构造函数中指定返回页面
            modelAndView.addObject("error", "用户名或密码不正确"); //在request请求域中设置错误信息
            return modelAndView; //返回model
        }
    }

    @RequestMapping("/logout")
    public String LogOut(SessionStatus sessionStatus){
        //清除Session 只能清除在SessionAttributes声明中的属性
        sessionStatus.setComplete();
        return "index"; //转发至登录界面
    }

}
