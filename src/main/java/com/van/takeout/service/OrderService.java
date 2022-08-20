package com.van.takeout.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.van.takeout.entity.Order;

import java.time.LocalDateTime;

public interface OrderService extends IService<Order> {
    void submit(Order order, Long userId);

    Page<Order> pageByUserId(int page, int pageSize, Object userId);

    Page<Order> pageByMany(int page, int pageSize, String number, LocalDateTime beginTime, LocalDateTime endTime);
}
