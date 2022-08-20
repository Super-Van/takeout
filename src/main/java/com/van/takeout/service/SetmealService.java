package com.van.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.van.takeout.entity.Setmeal;
import com.van.takeout.entity.SetmealDto;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void updateWithDish(SetmealDto setmealDto);

    void deleteWithDish(List<Long> ids);

    List<Setmeal> listByCategoryIdAndStatus(Long categoryId, Integer status);
}
