package com.van.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.van.takeout.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {
    ShoppingCart getBySetmealIdAndUserId(ShoppingCart shoppingCart);

    @Deprecated
    ShoppingCart getByDishIdAndUserIdAndFlavor(ShoppingCart shoppingCart);

    void updateNumberById(Long id, String operator);

    @Deprecated
    void updateNumberByByDishIdAndUserIdAndFlavor(ShoppingCart shoppingCart);

    ShoppingCart getByDishIdAndUserId(ShoppingCart shoppingCart);

    List<ShoppingCart> listByUserId(Object userId);

    void removeByUserId(Object userId);
}
