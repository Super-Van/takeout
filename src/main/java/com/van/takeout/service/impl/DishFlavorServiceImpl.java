package com.van.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.van.takeout.dao.DishFlavorDao;
import com.van.takeout.entity.DishFlavor;
import com.van.takeout.service.DishFlavorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorDao, DishFlavor> implements DishFlavorService {
    @Override
    @Transactional
    public void deleteByDishIds(List<Long> dishIds) {
        //清空诸dishId对应的口味记录
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DishFlavor::getDishId, dishIds);
        remove(queryWrapper);
    }

    @Override
    public List<DishFlavor> listByDishId(Long id) {
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        return list(queryWrapper);
    }
}
