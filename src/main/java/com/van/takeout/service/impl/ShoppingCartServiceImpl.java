package com.van.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.van.takeout.dao.ShoppingCartDao;
import com.van.takeout.entity.ShoppingCart;
import com.van.takeout.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartDao, ShoppingCart> implements ShoppingCartService {
    @Override
    public ShoppingCart getBySetmealIdAndUserId(ShoppingCart shoppingCart) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId()).eq(ShoppingCart::getUserId, shoppingCart.getUserId());
        return getOne(queryWrapper);
    }

    @Override
    public void updateNumberById(Long id, String operator) {
        LambdaUpdateWrapper<ShoppingCart> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.setSql("number = number" + operator + 1).eq(ShoppingCart::getId, id);
        update(updateWrapper);
    }

    /**
     * 只有菜品id、用户id、口味完全一样才让number+1，只要有一个分量不一样就是插入
     *
     * @param shoppingCart
     * @return
     */
    @Override
    public ShoppingCart getByDishIdAndUserIdAndFlavor(ShoppingCart shoppingCart) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId()).eq(ShoppingCart::getUserId, shoppingCart.getUserId()).eq(ShoppingCart::getDishFlavor, shoppingCart.getDishFlavor());
        return getOne(queryWrapper);
    }

    @Override
    public void updateNumberByByDishIdAndUserIdAndFlavor(ShoppingCart shoppingCart) {
        LambdaUpdateWrapper<ShoppingCart> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId()).eq(ShoppingCart::getUserId, shoppingCart.getUserId()).eq(ShoppingCart::getDishFlavor, shoppingCart.getDishFlavor()).set(ShoppingCart::getNumber, shoppingCart.getNumber());
        update(updateWrapper);
    }

    @Override
    public ShoppingCart getByDishIdAndUserId(ShoppingCart shoppingCart) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId()).eq(ShoppingCart::getUserId, shoppingCart.getUserId());
        return getOne(queryWrapper);
    }

    @Override
    public List<ShoppingCart> listByUserId(Object userId) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId).orderByDesc(ShoppingCart::getCreateTime);
        return list(queryWrapper);
    }

    @Override
    public void removeByUserId(Object userId) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        remove(queryWrapper);
    }
}
