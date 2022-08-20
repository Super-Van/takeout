package com.van.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.van.takeout.dao.DishDao;
import com.van.takeout.entity.Dish;
import com.van.takeout.entity.DishDto;
import com.van.takeout.entity.DishFlavor;
import com.van.takeout.service.DishFlavorService;
import com.van.takeout.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishDao, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //mybatis plus在插入前先调用dishDto的setId方法用雪花算法给id赋值
        save(dishDto);
        //插入dish_flavor表
        List<DishFlavor> flavors = dishDto.getFlavors().stream().map(dishFlavor -> {
            dishFlavor.setDishId(dishDto.getId());
            return dishFlavor;
        }).collect(Collectors.toList());
        //批量插入
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //dish数据
        Dish dish = getById(id);
        //flavor数据
        List<DishFlavor> dishFlavors = dishFlavorService.listByIds(Collections.singletonList(id));
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        dishDto.setFlavors(dishFlavors);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表
        updateById(dishDto);
        //更新dish_flavor表，先清空dishId对应的几种口味，再批量插入
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        List<DishFlavor> flavors = dishDto.getFlavors().stream().map(dishFlavor -> {
            dishFlavor.setDishId(dishDto.getId());
            return dishFlavor;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public List<DishDto> listWithFlavor(DishDto dishDtoParam) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dishDtoParam.getCategoryId() != null, Dish::getCategoryId, dishDtoParam.getCategoryId()).eq(Dish::getStatus, 1).orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<DishDto> dishDtos = list(queryWrapper).stream().map(dish -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);
            dishDto.setFlavors(dishFlavorService.listByDishId(dish.getId()));
            return dishDto;
        }).collect(Collectors.toList());
        return dishDtos;
    }
}
