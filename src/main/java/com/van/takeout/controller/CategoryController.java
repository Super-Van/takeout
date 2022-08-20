package com.van.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.van.takeout.entity.Category;
import com.van.takeout.service.CategoryService;
import com.van.takeout.util.R;
import com.van.takeout.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R save(@RequestBody @Valid Category category, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Utils.makeFieldErrors(bindingResult);
        }
        categoryService.save(category);
        return R.success("分类添加成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        //分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        //查询
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> delete(Long ids) {
        //重载remove方法
        categoryService.remove(ids);
        return R.success("分类删除成功");
    }

    @PutMapping
    public R update(@RequestBody @Valid Category category, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Utils.makeFieldErrors(bindingResult);
        }
        categoryService.updateById(category);
        return R.success("分类信息修改成功");
    }

    @GetMapping("/list")
    public R list(@Valid Category category, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Utils.makeFieldErrors(bindingResult);
        }
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        List<Category> categories = categoryService.list(queryWrapper);
        return R.success(categories);
    }
}
