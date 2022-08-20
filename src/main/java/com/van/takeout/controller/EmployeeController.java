package com.van.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.van.takeout.entity.Employee;
import com.van.takeout.service.EmployeeService;
import com.van.takeout.util.R;
import com.van.takeout.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee empParam, HttpServletRequest request) {
        String pwd = DigestUtils.md5DigestAsHex(empParam.getPassword().getBytes());
        //条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //只根据员工名
        queryWrapper.eq(Employee::getUsername, empParam.getUsername());
        //员工名有唯一索引
        Employee employee = employeeService.getOne(queryWrapper);
        if (employee == null || !Objects.equals(pwd, employee.getPassword())) {
            return R.error("用户名或密码错误");
        }
        if (employee.getStatus() == 0) {
            return R.error("账户已被禁用");
        }
        request.getSession().setAttribute("employee", employee.getId());
        log.info(employee.getUsername() + "登录");
        return R.success(employee);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpSession session) {
        session.removeAttribute("employee");
        return R.success("退出成功");
    }

    //发现这玩意儿尾部有没有/还不一样
    //@PostMapping("")
    @PostMapping
    public R register(@RequestBody @Valid Employee employee, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return Utils.makeFieldErrors(bindingResult);
        }
        //默认初始密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        Long empId = (Long) session.getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        employeeService.save(employee);
        log.info("新增员工：" + employee);
        return R.success("添加员工成功");
    }

    @GetMapping("/page")
    public R<Page<Employee>> page(int page, int pageSize, String name) {
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //过滤
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        //排序
        queryWrapper.orderByDesc(Employee::getCreateTime);
        //查询
        employeeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 理编辑提交或禁用请求
     *
     * @param employee
     * @param bindingResult
     * @param session
     * @return
     */
    @PutMapping
    public R update(@RequestBody @Valid Employee employee, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return Utils.makeFieldErrors(bindingResult);
        }
        //大概伪造报文的健壮性问题等到后续spring security来做了
        Long empId = (Long) session.getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);
        //前端已经把状态改好了
        employeeService.updateById(employee);
        //尚在线的员工要强制下线，移除session，怎么搞？
        return R.success("员工状态修改成功");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable("id") Long id) {
        return R.success(employeeService.getById(id));
    }
}
