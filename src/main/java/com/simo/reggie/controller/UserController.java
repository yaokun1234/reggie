package com.simo.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.simo.reggie.commons.R;
import com.simo.reggie.entity.User;
import com.simo.reggie.service.IUserService;
import com.simo.reggie.utils.SMSUtils;
import com.simo.reggie.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * <p>
 * 用户信息 前端控制器
 * </p>
 *
 * @author simo
 * @since 2022-05-10
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    IUserService iUserService;
    /**
     * 手机验证码登录
     * @param user
     * @param
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user,HttpSession session) {
        String phone = user.getPhone();
        String code = ValidateCodeUtils.generateValidateCode(6).toString();
        System.out.println("code:"+code);
        //SMSUtils.sendMessage("记录it学习","SMS_235811961",phone,code);
        session.setAttribute(phone,code);
        return R.success("短信发送成功");
    }

    @PostMapping("login")
    public R<User> login(@RequestBody Map map,HttpSession session){
        String phone = (String) map.get("phone");
        String code = (String) map.get("code");
        String sessionCode = (String)session.getAttribute(phone);


        //测试用例
        if(code.equals("123456")){
            LambdaQueryWrapper<User> eq = new LambdaQueryWrapper<User>()
                    .eq(User::getPhone, "18226154078");
            User user = iUserService.getOne(eq);
            session.setAttribute("user",user.getId());
            return R.success(user);
        }


        if(code.equals(sessionCode)){
            //判断当前是否是新用户，新用户则自动注册
            LambdaQueryWrapper<User> eq = new LambdaQueryWrapper<User>()
                    .eq(User::getPhone, phone);
            User user = iUserService.getOne(eq);
            if(user == null){
                user = new User();
                user.setPhone(phone);
                iUserService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }else{
            return R.error("登录失败");
        }

    }


}
