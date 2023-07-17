package com.czj.controller.com;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czj.controller.util.DEA;
import com.czj.interceptor.PublicController;
import com.czj.pojo.Admin;
import com.czj.pojo.User;
import com.czj.service.AdminService;
import com.czj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/UserCon")
public class UserCon {


    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;

    // 新用户注册默认头像
    @Value("${myProject.static.UserAvatarUrl}")
    private String UserAvatarUrlDefault;

    /**
     * 表单参数接受类
     */
    static class Form{
        public String id;
        public String psw;
        public String question1;
        public String question2;
        public String answer1;
        public String answer2;
        public Integer identity;
        public String verifyCode;
        public String userId;
        public String username;
        public int pageSize;
        public int currentPage;
    }


    /**
     * 分页条件查询
     * @return 数据集
     */
    @PostMapping("/getPageByUsername")
    public DEA getPageByUsername(@RequestBody Form searchForm){
        // 封装查询条件
        String username = searchForm.username;
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username",searchForm.username);
        queryWrapper.like("user_id",searchForm.userId);
        queryWrapper.orderByDesc("identity");
        queryWrapper.orderByDesc("user_id");
        // 查询页面总数
        int count = userService.count(queryWrapper);
        int currentPage = searchForm.currentPage;
        int pageSize = searchForm.pageSize;
        // 超界检测-默认归零
        if(count < (currentPage - 1) * pageSize){
            currentPage = 1;
        }
        // 封装分页信息
        IPage<User> page = new Page<>(currentPage,pageSize);
        return  new DEA(userService.page(page,queryWrapper));
    }

    /**
     * 登录确认方法
     * @param form 用户名 密码 验证码
     * @param request 获取验证码
     * @return user对象
     */
    @PublicController
    @PostMapping("/verifyLogin")
    public DEA verifyLogin(@RequestBody Form form, HttpServletRequest request){
        HttpSession session = request.getSession();
        // 获取验证码值
        String checkCodeStr = (String) session.getAttribute("checkCodeStr");
        // 检查验证码
        if( !form.verifyCode.equalsIgnoreCase(checkCodeStr)){
            return new DEA("验证码错误！");
        }
        else {
            // 构造查询条件
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", form.id);
            queryWrapper.eq("password", form.psw);
            //
            User user = userService.getOne(queryWrapper);
            if (user != null) {
                session.setAttribute("userId", user.getUserId());
                return new DEA(user);
            } else {
                return new DEA(false, "账号密码错误！");
            }
        }
    }

    /**
     * 管理员登陆
     * @param form 用户名 密码 验证码
     * @param request 获取验证码
     * @return user对象
     */
    @PublicController
    @PostMapping("/adminLogin")
    public DEA adminLogin(@RequestBody Form form, HttpServletRequest request){
        HttpSession session = request.getSession();
        // 获取验证码值
        String checkCodeStr = (String) session.getAttribute("checkCodeStr");
        // 检查验证码
        if( !form.verifyCode.equalsIgnoreCase(checkCodeStr)){
            return new DEA("验证码错误！");
        }
        else {
            QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("admin_id", form.id);   // admin_id = root
            queryWrapper.eq("password", form.psw);  // password = 123456
            Admin user = adminService.getOne(queryWrapper);
            if (user != null) {
                request.getSession().setAttribute("userId",user.getAdminId());
               return new DEA(true,"欢迎管理员登陆！");
            } else {
                return new DEA(false, "账号密码错误！");
            }
        }
    }


    /**
     * 新建账户
     * @param user 数据表单
     * @return DEA
     */
    @PostMapping("/createUser")
    public DEA createUser(@RequestBody User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 设置查询类
        queryWrapper.eq("user_id", user.getUserId());
        // 查询用户是否已存在
        if(userService.list(queryWrapper).size() != 0){
            return new DEA(false,"账号已存在！");
        }
        user.setUsername(user.getUserId());
        // 密保
        user.setQuestion1("root");
        user.setQuestion2("root");
        user.setAnswer1("root");
        user.setAnswer2("root");
        // 更新数据
        return new DEA(userService.save(user));
    }

    /**
     * 重置密码服务
     * @param form  提供Id和新的密码
     * @return bool
     */
    @PublicController
    @PostMapping("/updatePswRoot")
    public DEA updatePswRoot(@RequestBody Form form){

        // 构造跟新对象
        User user = new User();
        user.setPassword(form.psw);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",form.userId);
        return new DEA(userService.update(user, queryWrapper));
    }

    /**
     * 注销账户 todo 文件及相关数据待处理
     * @param user 包含id
     * @return DEA
     */
    @PostMapping("/deleteUser")
    public DEA deleteUser(@RequestBody User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",user.getUserId());
        // 删除数据
        return new DEA(userService.remove(queryWrapper));
    }


    /**
     * 用户注册服务
     * @param form 注册所需要的user字段
     * @param request 获取验证码
     * @return bool
     */
    @PublicController
    @PostMapping("/register")
    public DEA register(@RequestBody Form form, HttpServletRequest request){

        HttpSession session = request.getSession();
        // 获取验证码值
        String checkCodeStr = (String) session.getAttribute("checkCodeStr");
        // 检查验证码
        if( !form.verifyCode.equalsIgnoreCase(checkCodeStr)){
            return new DEA("验证码错误！");
        }
        // 封装用户数据类型
        User user = new User(form.id, form.psw, form.question1, form.question2, form.answer1, form.answer2, form.identity);
        // 为用户添加默认头像
        user.setAvatarUrl(UserAvatarUrlDefault);
        // 检查账号重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", form.id);
        if(userService.getOne(queryWrapper) != null){
            return new DEA("该账号已注册！");
        }
        // 数据持久化
        return new DEA(userService.save(user));
    }

    /**
     * 获取密保问题
     * @param form 用户ID
     * @return  User对象 用户密保
     */
    @PublicController
    @PostMapping("/getQuestion")
    public DEA getQuestion(@RequestBody Form form){

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", form.id);
        User user = userService.getOne(queryWrapper);
        User newUser = new User();
        newUser.setQuestion1(user.getQuestion1());
        newUser.setQuestion2(user.getQuestion2());
        newUser.setAnswer1(user.getAnswer1());
        newUser.setAnswer2(user.getAnswer2());
        newUser.setIdentity(user.getIdentity()); // 防止空字段序列化
        return new DEA(newUser);
    }

    /**
     * 重置密码服务
     * @param form  提供Id和新的密码
     * @return bool
     */
    @PublicController
    @PostMapping("/updatePsw")
    public DEA updatePsw(@RequestBody Form form){

        // 构造跟新对象
        User user = new User();
        user.setPassword(form.psw);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",form.id); // where user_id=2000
        return new DEA(userService.update(user, queryWrapper));
    }

    /**
     * 储存用户ID到session
     * @param form 提供userID
     * @param request 提供session
     * @return bool
     */
    @PublicController
    @PostMapping("/setUserIdToSession")
    public DEA setUserIdToSession(@RequestBody Form form, HttpServletRequest request){

        request.getSession().setAttribute("userId", form.id);
        return new DEA(true);
    }

    /**
     *  退出登陆 删除session中的用户ID
     * @param request 提供session
     * @return bool
     */
    @GetMapping("/clearUserIdInSession")
    public DEA clearUserIdInSession(HttpServletRequest request){
        request.getSession().removeAttribute("userId");
        return new DEA(true);
 }

    /**
     * 获取当前登录用户信息
     * @param request 获取session
     * @return user对象
     */
    @GetMapping("/getUserInfo")
    public DEA getUserInfo(HttpServletRequest request){
        String userId =(String)  request.getSession().getAttribute("userId");

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);

        return new DEA(userService.getOne(queryWrapper));
    }

    /**
     *  更新用户数据
     * @param user user对象  假设传递的userId=2000
     * @return bool
     */
    @PostMapping("/updateUserInfo")
    public DEA updateUserInfo(@RequestBody User user){

        // 定义条件构造器
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        // 向条件构造器中添加等价条件，条件内容是user_id=2000;
        queryWrapper.eq("user_id", user.getUserId());
        return new DEA(userService.update(user,queryWrapper));
    }

}
