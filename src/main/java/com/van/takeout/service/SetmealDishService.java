package com.van.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.van.takeout.entity.SetmealDish;

import java.util.List;

public interface SetmealDishService extends IService<SetmealDish> {
    void deleteBySetmealIds(List<Long> ids);
}
