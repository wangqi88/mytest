package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.MD5Util;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import javax.servlet.http.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 王琦
 * 2021/6/5
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    //处理登录请求
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(HttpServletRequest request){
        Cookie[]cookies = request.getCookies();
        String loginAct = null;
        String loginPwd = null;

        for (Cookie cookie : cookies) {
            String name =cookie.getName();
            if ("loginAct".equals(name)){
                loginAct=cookie.getValue();
                continue;
            }
            if ("loginPwd".equals(name)){
                loginPwd=cookie.getValue();
            }
        }
        if (loginAct != null && loginPwd != null){
            Map<String,Object>map = new HashMap<>();
            map.put("loginAct", loginAct);
            map.put("loginPwd", MD5Util.getMD5(loginPwd));

            User user= userService.queryUserByLoginAndPwd(map);
            //user存储session
            request.getSession().setAttribute("sessionUser", user);
            //跳到后台
            return "redirect:/workbench/index.do";
        }else {
            return "settings/qx/user/login";
        }
    }

    @RequestMapping("/settings/qx/user/login.do")
        public @ResponseBody Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpServletResponse response, HttpSession session){

        System.out.println(request.getRemoteAddr());

        Map<String,Object> map= new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", MD5Util.getMD5(loginPwd));

     User user = userService.queryUserByLoginAndPwd(map);

        //返回对象
        ReturnObject returnObject = new ReturnObject();
        //用户名不能为空
        if (user==null){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("用户名或密码错误");
        }else {
            if (DateUtils.formatDateTime(new Date()).compareTo(user.getExpireTime())>0){
                //账号已过期，登陆失败
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账户已过期");
                //验证账户是否被锁定
            }else if ("0".equals(user.getLockState())){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账户已被锁定");
                //验证IP地址是否在允许范围内
            }else if (!user.getAllowIps().contains(request.getRemoteAddr())){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("此IP地址不可访问");
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);

                //保存user到sessiion中去，验证是否登录做准备
                 session.setAttribute(Contants.SESSION_USER, user);

                 //免登录功能
                 if ("true".equals(isRemPwd)){
                     //生成cookie
                     Cookie c1 = new Cookie("loginAct", loginAct);
                     c1.setMaxAge(10*24*60*60);//保存的时间
                     response.addCookie(c1);

                     Cookie c2 = new Cookie("loginPwd", loginPwd);
                     c2.setMaxAge(10*24*60*60);//保存的时间
                     response.addCookie(c2);
                 }else {
                     //不保存cookie，清理cookie

                     Cookie c1 = new Cookie("loginAct", null);
                     c1.setMaxAge(0);
                     response.addCookie(c1);

                     Cookie c2 = new Cookie("loginPwd", null);
                     c2.setMaxAge(0);
                     response.addCookie(c2);

                 }
            }
        }

        return returnObject;
    }

    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response,HttpSession session){
        //清空cookin
        Cookie c1 = new Cookie("loginAct", null);
        c1.setMaxAge(0);
        response.addCookie(c1);

        Cookie c2 = new Cookie("loginPwd", null);
        c2.setMaxAge(0);
        response.addCookie(c2);

        //销毁sessin
        session.invalidate();

        return "redirect:/";

    }

}
