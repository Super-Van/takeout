package com.van.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.van.takeout.entity.DishFlavor;

import java.util.List;

public interface DishFlavorService extends IService<DishFlavor> {
    void deleteByDishIds(List<Long> idList);

    List<DishFlavor> listByDishId(Long id);
}
