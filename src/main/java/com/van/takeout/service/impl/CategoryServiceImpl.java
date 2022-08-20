package com.van.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.van.takeout.dao.CategoryDao;
import com.van.takeout.entity.Category;
import com.van.takeout.entity.Dish;
import com.van.takeout.entity.Setmeal;
import com.van.takeout.exception.CustomException;
import com.van.takeout.service.CategoryService;
import com.van.takeout.service.DishService;
import com.van.takeout.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements CategoryService {
    @Autowired
    DishService dishService;
    @Autowired
    SetmealService setmealService;

    @Override
    @Transactional
    public void remove(Long id) {
        //分类关联菜品与否
        LambdaQueryWrapper<Dish> queryWrapper1 = new LambdaQueryWrapper<>();
        //只要有一条匹配就有关联
        queryWrapper1.eq(Dish::getCategoryId, id).last("LIMIT 1");
        Dish dish = dishService.getOne(queryWrapper1);
        if (dish != null) {
            throw new CustomException("分类关联食品，不可删除");
        }
        //分类关联套餐与否
        LambdaQueryWrapper<Setmeal> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(Setmeal::getCategoryId, id);
        Setmeal setmeal = setmealService.getOne(queryWrapper2);
        if (setmeal != null) {
            throw new CustomException("分类关联套餐，不可删除");
        }
        removeById(id);
    }
}
