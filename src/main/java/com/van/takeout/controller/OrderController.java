package com.van.takeout.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.van.takeout.entity.Order;
import com.van.takeout.entity.OrderDto;
import com.van.takeout.service.OrderService;
import com.van.takeout.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Order order, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        orderService.submit(order, userId);
        return R.success("下单成功");
    }

    @GetMapping("/userPage")
    public R<Page<OrderDto>> userPage(@RequestParam int page, @RequestParam int pageSize, HttpSession session) {
        return R.success(orderService.pageByUserId(page, pageSize, session.getAttribute("user")));
    }

    /**
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime 类型转换器按照默认的格式进行解析转换，自己须另指定格式
     * @param endTime
     * @return
     */
    @GetMapping("/page")
    public R<Page<Order>> page(int page, int pageSize, String number, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime beginTime, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return R.success(orderService.pageByMany(page, pageSize, number, beginTime, endTime));
    }
}
