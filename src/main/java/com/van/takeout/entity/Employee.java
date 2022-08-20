package com.van.takeout.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ToString
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    //用于分布式的雪花算法依靠长整型
    private Long id;

    private String username;

    @Length(min = 2, max = 5, message = "员工姓名长度应在2到5位之间")
    private String name;

    private String password;

    @Pattern(regexp = "^1[35]\\d{9}$", message = "手机号码格式不正确")
    private String phone;

    @Pattern(regexp = "^0|1$", message = "性别格式不正确")
    private String sex;

    private String idNumber;

    @Range(max = 1, message = "状态值不合法")
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    //插入时的自动赋值
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    //插入或更新时的自动赋值
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
}
