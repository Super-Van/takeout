package com.van.takeout.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 用户信息
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;


    //姓名
    private String name;


    //手机号
    @Pattern(regexp = "^(13[0-9]|14[5|7]|15[012356789]|18[0|12356789])\\d{8}$", message = "手机号码格式不正确")
    private String phone;


    //性别 0 女 1 男
    @Range(max = 1, message = "性别值非法")
    private String sex;


    //身份证号
    private String idNumber;


    //头像
    private String avatar;


    //状态 0:禁用，1:正常
    @Range(max = 1, message = "状态值非法")
    private Integer status;
}
