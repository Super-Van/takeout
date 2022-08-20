package com.van.takeout.controller;

import com.van.takeout.entity.ShoppingCart;
import com.van.takeout.service.ShoppingCartService;
import com.van.takeout.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    ShoppingCartService shoppingCartService;

    /**
     * 前端无视口味的区别，即userId并dishId或userId并setmealId成主键，迫不得已弃用
     *
     * @param shoppingCart
     * @param session
     * @return
     */
    @Deprecated
    public R<ShoppingCart> addDeprecated(@RequestBody ShoppingCart shoppingCart, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        shoppingCart.setUserId(userId);
        ShoppingCart shoppingCartNew;
        //是套餐
        if (shoppingCart.getSetmealId() != null) {
            //看诸id是否重复，是则修改-数量+1，否则插入
            shoppingCartNew = shoppingCartService.getBySetmealIdAndUserId(shoppingCart);
            if (shoppingCartNew != null) {
                shoppingCartNew.setNumber(shoppingCartNew.getNumber() + 1);
                shoppingCartService.updateNumberById(shoppingCartNew.getId(), "+");
                return R.success(shoppingCartNew);
            }
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            return R.success(shoppingCart);
        }
        //是菜品，判断诸id连同口味是否重复，是则修改数量，否则插入
        shoppingCartNew = shoppingCartService.getByDishIdAndUserIdAndFlavor(shoppingCart);
        if (shoppingCartNew != null) {
            shoppingCartNew.setNumber(shoppingCartNew.getNumber() + 1);
            shoppingCartService.updateNumberById(shoppingCartNew.getId(), "+");
            return R.success(shoppingCartNew);
        }
        shoppingCart.setNumber(1);
        shoppingCartService.save(shoppingCart);
        return R.success(shoppingCart);
    }

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        shoppingCart.setUserId(userId);
        ShoppingCart shoppingCartNew;
        //是套餐
        if (shoppingCart.getSetmealId() != null) {
            //看诸id是否重复，是则修改-数量+1，否则插入
            shoppingCartNew = shoppingCartService.getBySetmealIdAndUserId(shoppingCart);
            if (shoppingCartNew != null) {
                //慎重考虑高并发问题，getNumber()可能得到脏数据，改用set x = x + 1，前端展示的还可能是脏数据（倒是正常）
                shoppingCartNew.setNumber(shoppingCartNew.getNumber() + 1);
                shoppingCartService.updateNumberById(shoppingCartNew.getId(), "+");
                return R.success(shoppingCartNew);
            }
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            return R.success(shoppingCart);
        }
        //是菜品，判断诸id是否重复，是则修改数量，否则插入
        shoppingCartNew = shoppingCartService.getByDishIdAndUserId(shoppingCart);
        if (shoppingCartNew != null) {
            shoppingCartNew.setNumber(shoppingCartNew.getNumber() + 1);
            shoppingCartService.updateNumberById(shoppingCartNew.getId(), "+");
            return R.success(shoppingCartNew);
        }
        shoppingCart.setNumber(1);
        shoppingCartService.save(shoppingCart);
        return R.success(shoppingCart);
    }

    private R<ShoppingCart> updateNumberOrRecord(ShoppingCart shoppingCart, ShoppingCart shoppingCartNew) {
        //考虑狂点两下的情况？此处倒很难发生，又不像抢车票
        if (shoppingCartNew == null) {
            return R.error("操作频繁");
        }
        //当前number分量已经是1
        if (shoppingCartNew.getNumber() == 1) {
            shoppingCartNew.setNumber(0);
            shoppingCartService.removeById(shoppingCartNew.getId());
            return R.success(shoppingCartNew);
        }
        shoppingCartNew.setNumber(shoppingCartNew.getNumber() - 1);
        shoppingCartService.updateNumberById(shoppingCartNew.getId(), "-");
        return R.success(shoppingCartNew);
    }

    /**
     * @param shoppingCart 仅封装dishId或setmealId
     * @return
     */
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        shoppingCart.setUserId(userId);
        ShoppingCart shoppingCartNew;
        //是菜品
        if (shoppingCart.getDishId() != null) {
            shoppingCartNew = shoppingCartService.getByDishIdAndUserId(shoppingCart);
            return updateNumberOrRecord(shoppingCart, shoppingCartNew);
        }
        shoppingCartNew = shoppingCartService.getBySetmealIdAndUserId(shoppingCart);
        return updateNumberOrRecord(shoppingCart, shoppingCartNew);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(HttpSession session) {
        return R.success(shoppingCartService.listByUserId(session.getAttribute("user")));
    }

    @DeleteMapping("/clean")
    public R<String> clean(HttpSession session) {
        shoppingCartService.removeByUserId(session.getAttribute("user"));
        return R.success("购物车已清空");
    }
}
