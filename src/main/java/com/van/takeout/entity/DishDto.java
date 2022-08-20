package com.van.takeout.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO-data transfer object-数据转换对象，一个对象去插两个表，属性集对每个表的字段集都成包含关系
 * 继承Dish，继承方法,扩展属性
 */
@Data
public class DishDto extends Dish {

    //前端JSON对象里的flavors数组转为此属性，数组元素与DishFlavor结构匹配多少填充多少
    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
