package com.van.takeout.controller;

import com.van.takeout.entity.User;
import com.van.takeout.service.MailService;
import com.van.takeout.service.UserService;
import com.van.takeout.util.R;
import com.van.takeout.util.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private MailService mailService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

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
        //手机号作键，验证码作值保存到redis中，10分钟时限
        redisTemplate.opsForValue().set(phone, code.toString(), 10L, TimeUnit.MINUTES);
        //通过QQ邮箱发送验证码
        mailService.sendMail(from, phone + "@163.com", "验证码为" + code + "，10分钟内有效。");
        log.info("验证码：" + code);
        return R.success("验证码成功发送到163邮箱");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map<String, String> map, HttpSession session) {
        String phone = map.get("phone");
        String code = map.get("code");
        if (code == null || phone == null || !code.equals(redisTemplate.opsForValue().get(phone))) {
            return R.error("登录失败");
        }
        //防止同一会话内登录又退出，后使用同一对验证码与手机号
        redisTemplate.delete(phone);
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
