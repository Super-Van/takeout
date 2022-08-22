package com.van.takeout;

import com.van.takeout.entity.Employee;
import com.van.takeout.service.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
class TakeoutApplicationTests {
    @Autowired
    private MailService mailService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void testDataBinder() {
        Employee employee = new Employee();
        employee.setName("vivo");
        System.out.println(employee);
    }

    @Test
    void testSendMsg(@Value("${spring.mail.username}") String from) {
        mailService.sendMail(from, "15927376978@163.com", "验证码为2222");
    }

    @Test
    void testRedis() {
        System.out.println(redisTemplate.opsForValue().get("dish_1397844263642378242"));
    }

}
