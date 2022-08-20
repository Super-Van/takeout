package com.van.takeout.controller;

import com.van.takeout.entity.User;
import com.van.takeout.service.MailService;
import com.van.takeout.service.UserService;
import com.van.takeout.util.R;
import com.van.takeout.util.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private MailService mailService;
    @Autowired
    private UserService userService;

    /**
     * @param map     目前针对@RequestBody，请求参数只能封装进实体类对象或映射
     * @param session
     * @param from
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> getValidationCode(@RequestBody Map<String, String> map, HttpSession session, @Value("${spring.mail.username}") String from) {
        String phone = map.get("phone");
        if (phone == null) {
            return R.error("请填入手机号");
        }
        //生成随机验证码
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        //将配对手机号与验证码保存到session中
        session.setAttribute("phone", phone);
        session.setAttribute("code", code.toString());
        //通过QQ邮箱发送验证码
        mailService.sendMail(from, phone + "@163.com", "验证码为" + code);
        log.info("验证码：" + code);
        return R.success("验证码成功发送到163邮箱");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map<String, String> map, HttpSession session) {
        String phone = map.get("phone");
        String code = map.get("code");
        if (code == null || phone == null || !(code.equals(session.getAttribute("code")) && phone.equals(session.getAttribute("phone")))) {
            return R.error("登录失败");
        }
        //防止同一会话内登录又退出，后使用同一对验证码与手机号
        session.removeAttribute("code");
        session.removeAttribute("phone");
        //或是注册或是登录
        User user = userService.loginOrRegister(phone);
        session.setAttribute("user", user.getId());
        return R.success(user);
    }

    @PostMapping("/loginout")
    public R<String> logout(HttpSession session) {
        session.removeAttribute("user");
        return R.success("退出成功");
    }
}
