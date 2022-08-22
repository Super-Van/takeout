package com.van.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.van.takeout.entity.Dish;
import com.van.takeout.entity.DishDto;
import com.van.takeout.service.CategoryService;
import com.van.takeout.service.DishFlavorService;
import com.van.takeout.service.DishService;
import com.van.takeout.util.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping
    public R<String> saveWithFlavor(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        //删除redis中的菜品数据
        dishService.deleteRedis(dishDto);
        return R.success("菜品添加成功");
    }

    @GetMapping("/page")
    public R<Page<DishDto>> page(int page, int pageSize, String name) {
        Page<Dish> dishPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name).orderByDesc(Dish::getUpdateTime);
        dishService.page(dishPage, queryWrapper);
        Page<DishDto> dtoPage = new Page<>();
        BeanUtils.copyProperties(dishPage, dtoPage, "records");
        List<DishDto> records = dishPage.getRecords().stream().map(dish -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);
            dishDto.setCategoryName(categoryService.getById(dish.getCategoryId()).getName());
            return dishDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(records);
        return R.success(dtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable("id") Long id) {
        return R.success(dishService.getByIdWithFlavor(id));
    }

    @PutMapping
    public R<String> updateWithFlavor(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        //删除redis中的菜品数据
        dishService.deleteRedis(dishDto);
        return R.success("菜品修改成功");
    }

    /**
     * 单品或批量的停售或启售
     *
     * @param status 前端设好的、待填入的状态值
     * @param ids    逗号分隔的id串
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable("status") int status, @RequestParam List<Long> ids) {
        List<Dish> dishes = ids.stream().map(id -> {
            Dish dish = new Dish();
            dish.setId(id);
            dish.setStatus(status);
            return dish;
        }).collect(Collectors.toList());
        dishService.updateBatchById(dishes);
        //删除redis中的菜品数据
        dishService.deleteRedis(ids);
        return R.success("菜品" + (status == 1 ? "启售" : "停售") + "成功");
    }

    /**
     * 单品或批量的删除
     *
     * @param ids 不带@RequestParam，会同时按名称ids与类型匹配，这里就不匹配，就不映射此方法，带@RequestParam就只按名称匹配，还调用相应类型转换器将String转为List
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        //要求删除前状态必须是停售
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId, ids).eq(Dish::getStatus, 1).last("LIMIT 1");
        if (dishService.getOne(queryWrapper) != null) {
            return R.error("启售菜品不能删除");
        }
        //从dish表删除
        dishService.removeByIds(ids);
        //从dish_flavor表删除
        dishFlavorService.deleteByDishIds(ids);
        //删除redis中的菜品数据
        dishService.deleteRedis(ids);
        return R.success("菜品删除成功");
    }


    /**
     * 共用一个好吗？在后台页面拉菜品列表是不用带口味信息的，浪费
     *
     * @param dish
     * @return
     */
    @Deprecated
    public R<List<Dish>> list(Dish dish) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId()).eq(Dish::getStatus, 1).orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishes = dishService.list(queryWrapper);
        return R.success(dishes);
    }

    @GetMapping("/list")
    public R<List<DishDto>> list(DishDto dishDto) {
        return R.success(dishService.listWithFlavor(dishDto));
    }
}
