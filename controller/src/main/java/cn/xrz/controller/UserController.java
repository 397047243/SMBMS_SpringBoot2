package cn.xrz.controller;


import cn.xrz.pojo.Role;
import cn.xrz.pojo.User;
import cn.xrz.service.RoleService;
import cn.xrz.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author XRZ
 * @date 2019\2\26 0026
 * @Description : 用户控制类
 */
@SessionAttributes("userSession") //此处用于声明取出session key
@Controller
public class UserController{

    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;

    /**
     *  添加用户
     * @param user 接受form表单传送过来的user
     * @param userSession 使用ModelAttribute将session域中的user 传递至 userSession
     * @param attach 获取文件上传对象
     *               多文件上传时必须使用数组和RequestParam入参的形式，value值对应=input的name属性值
     *  注意：
     *      此处接受form表单传递过来的数据时，SpringMVC无法映射Date格式的数据
     *      解决：在实体类中的date属性上添加注解 @DateTimeFormat(pattern = "yyyy-MM-dd")
     * @return
     */
    @RequestMapping("/useradd")
    public String add(User user, @ModelAttribute("userSession") User userSession, HttpServletRequest request,
                      @RequestParam(value = "files",required = false) MultipartFile[] attachs) throws Exception {
        //定义上传目标路径
        String path = "D:"+File.separator+"Y2_Plus"+File.separator+"upLoad"; //File.separator 自适应路径分隔符
//        上传至项目中
//        path = request.getSession().getServletContext().getRealPath("/upload");
        String idPicPath = null;//证件照上传路径
        String workPicPath = null;//工作照上传路径
        String errorinfo = "uploadFileIdError";
        for (int i = 0; i < attachs.length; i++) { //遍历文件
            if(i == 1){
                errorinfo = "uploadFileWorkError";
            }
            MultipartFile attach = attachs[i];
            if(!attach.isEmpty()){//判断文件是否为空
                String oldFileName = attach.getOriginalFilename(); //获取源文件名 用于提取后缀
                String prefix = FilenameUtils.getExtension(oldFileName);//获取文件名后缀
                if(attach.getSize() > 10485760){ //验证文件大小 单位字节
                    request.setAttribute(errorinfo," * 上传文件大小超过10MB");
                    return "useradd"; //添加失败则返回添加页面
                }else if(prefix.equalsIgnoreCase("jpg") || //验证文件格式
                        prefix.equalsIgnoreCase("jpeg") ||
                        prefix.equalsIgnoreCase("png") ||
                        prefix.equalsIgnoreCase("pneg")){
                    String fileName = System.currentTimeMillis()+".jpg";  //设置新文件名 确保文件名的唯一
                    File file = new File(path+fileName); //创建文件对象
                    if(!file.getParentFile().exists()){ //如果文件不存在
                        file.getParentFile().mkdirs(); //创建该文件
                    }
                    attach.transferTo(file); //将上传的实体文件复制到指定文件中
                    if(i == 0){
                        idPicPath = path + file.separator + fileName; //设置证件照的路径值
                    }else{
                        workPicPath = path + file.separator + fileName; //设置工作照的路径值
                    }
                }else{
                    request.setAttribute(errorinfo," * 上传文件格式不正确！");
                    return "useradd"; //添加失败则返回添加页面
                }
            }
        }
        user.setCreationDate(new Date()); //设置创建时间
        user.setCreatedBy(userSession.getId()); //设置创建人
        user.setIdPicPath(idPicPath); //设置证件照上传的路径
        user.setWorkPicPath(workPicPath);
        if(this.userService.add(user)){
            return "redirect:userQuery"; //添加成功连至下方的/userQuery
        }else{
            return "useradd"; //添加失败则返回添加页面
        }
    }

    /**
     *  查询用户列表 并 转发 至 userList 视图
     * @param queryname
     * @param queryUserRole
     * @param pageIndex
     * @return
     */
    @RequestMapping("/userQuery")
    public ModelAndView userQuery(@RequestParam(value = "queryname",required = false) String queryname,
                                  @RequestParam(value = "queryUserRole",required = false) Integer queryUserRole,
                                  @RequestParam(value = "pageIndex",required = false) Integer pageIndex) throws Exception {
        pageIndex = pageIndex == null ? 1:pageIndex; //非空验证
        //使用分页插件 startPage(当前页数,每页显示条数)
        PageHelper.startPage(pageIndex,5);
        List<User> userList= userService.getUserList(queryname, queryUserRole);
        PageInfo<User> page = new PageInfo<>(userList); //获取分页详细信息
        //创建 ModelAndView 并在构造函数中设置其返回的视图
        ModelAndView modelAndView = new ModelAndView("userlist");
        //相当于在request请求域中设置参数
        modelAndView.addObject("userList", userList); //用户集合
        modelAndView.addObject("roleList", this.roleService.getRoleList()); //角色列表
        modelAndView.addObject("queryUserName", queryname); //查询用户名
        modelAndView.addObject("queryUserRole", queryUserRole); //查询角色
        modelAndView.addObject("totalPageCount", page.getPages()); //总页数
        modelAndView.addObject("totalCount", page.getPageSize()); //总条数
        modelAndView.addObject("currentPageNo", pageIndex); //当前页数
        modelAndView.addObject("isPrev", page.isHasPreviousPage()); //是否有上一页
        modelAndView.addObject("isNext", page.isHasNextPage()); //是否有下一页
        int[] counts = new int[page.getPages()];
        for (int i = 0; i < counts.length; i++) {
            counts[i] = i+1;
        }
        modelAndView.addObject("Counts", counts); //总页数数组

        return modelAndView;
    }

    /**
     * 返回角色列表的JSON格式数据
     *
     * @ResponseBody
     *              将controller的方法返回的对象通过适当的转换器转换为指定的格式之后，
     *              写入到response对象的body区，通常用来返回JSON数据或者是XML
     * @return
     */
    @ResponseBody
    @RequestMapping("/getrolelist")
    public List<Role> getrolelist(){
        return this.roleService.getRoleList();
    }

    /**
     * 验证用户是否存在
     * @param userCode
     * @return
     */
    @ResponseBody
    @RequestMapping("/userCodeExist/userCode={userCode}")
    public User userCodeExist(@PathVariable("userCode") String userCode) throws Exception {
        return  this.userService.selectUserCodeExist(userCode);
    }

    /**
     * 返回用户详细信息
     * url:/viewUser?uid=xxx
     *      如果url中的参数名和方法中的形参名字相同 可省略@RequestParm 为规范建议写，此处测试
     * @param uid
     */
    @RequestMapping("/viewUser")
    public ModelAndView viewUser(String uid) throws Exception {
        ModelAndView modelAndView = new ModelAndView("userview");
        //通过modelAndView在request请求域中设置参数
        modelAndView.addObject("user",this.userService.getUserById(uid));
        return modelAndView;
    }

    /**
     * 根据用户id 删除用户
     * @param uid
     * @return  HashMap  转成JSON格式输出
     */
    @ResponseBody
    @RequestMapping("/delUser")
    public HashMap<String, String> delUser(@RequestParam("uid") Integer uid) throws Exception {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(uid <= 0){
            resultMap.put("delResult", "notexist");
        }else{
            if(userService.deleteUserById(uid)){
                resultMap.put("delResult", "true");
            }else{
                resultMap.put("delResult", "false");
            }
        }
        return  resultMap;
    }

    /**
     * 用户修改回显
     *          注意：此处回显日期异常，在后台中设置参数失败，改在JSP页面使用fmt标签设置
     * @param uid
     * @return
     */
    @RequestMapping("/modify")
    public ModelAndView modify(@RequestParam("uid") String uid) throws Exception {
        ModelAndView modelAndView = new ModelAndView("usermodify");
        modelAndView.addObject("user",userService.getUserById(uid));
        return  modelAndView;
    }

    /**
     * 接受修改映射过来是user进行保存
     * @param user
     * @return
     */
    @RequestMapping("/modifyexe")
    public String modifyexe(User user) throws Exception {
        System.err.println(user);
        if(userService.modify(user)){
            return "redirect:userQuery"; //修改成功重定向查询页面
        }else{
            return  "usermodify"; //修改失败返回回显页面
        }
    }

    /**
     *  修改密码
     * @param newpassword 传递过来的新密码
     * @param userSession 获取session作用域中的key为userSession 的Session 并赋值给形参 userSession
     * @param sessionStatus 用于清除Session
     * @return
     */
    @RequestMapping("/updatePwd")
    public ModelAndView updatePwd(@RequestParam("newpassword") String newpassword, @ModelAttribute("userSession") User userSession, SessionStatus sessionStatus) throws Exception {
        ModelAndView modelAndView = new ModelAndView("pwdmodify");
        if(userService.updatePwd(userSession.getId(),newpassword)){
            modelAndView.addObject("message","修改密码成功,请退出并使用新密码重新登录！");
            sessionStatus.setComplete();//注销用户
        }else{
            modelAndView.addObject("message","修改密码失败!");
        }
        return  modelAndView;
    }

    /**
     * 验证旧密码
     * @param oldpassword
     * @param user
     * @return JSON格式的验证数据
     */
    @ResponseBody
    @RequestMapping("/pwdModify")
    public Object pwdModify(@RequestParam("oldpassword") String oldpassword, @ModelAttribute("userSession") User user){
        Map<String, String> resultMap = new HashMap<String, String>();
        if(null == user){ //Session过期
            resultMap.put("result", "sessionerror");
        }else if(oldpassword == null){ //密码为空
            resultMap.put("result", "error");
        }else{
            if(oldpassword.equals(user.getUserPassword())){ //判断密码是否相同
                resultMap.put("result", "true");
            }else{
                resultMap.put("result", "false");
            }
        }
        return resultMap;
    }

    /**
     * 去修改用户的页面
     * @return
     */
    @RequestMapping("/pwdmodify_")
    public String pwdmodify(){
        return "pwdmodify";
    }

    /**
     * 去添加用户的页面
     * @return
     */
    @RequestMapping("/touseradd")
    public String touseradd(){
        return "useradd";
    }
}
