package com.van.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.van.takeout.entity.User;

public interface UserService extends IService<User> {
    User loginOrRegister(String phone);
}
