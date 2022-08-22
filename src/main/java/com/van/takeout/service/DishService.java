package com.van.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.van.takeout.entity.Dish;
import com.van.takeout.entity.DishDto;

import java.util.List;

public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDto dishDto);

    DishDto getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);

    List<DishDto> listWithFlavor(DishDto dishDto);

    void deleteRedis(DishDto dishDto);

    void deleteRedis(List<Long> ids);
}
