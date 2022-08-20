package com.van.takeout.controller;

import com.van.takeout.entity.AddressBook;
import com.van.takeout.service.AddressBookService;
import com.van.takeout.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    @PostMapping
    public R<String> save(@RequestBody AddressBook addressBook, HttpSession session) {
        addressBook.setUserId((Long) session.getAttribute("user"));
        addressBookService.save(addressBook);
        return R.success("地址簿保存成功");
    }

    @GetMapping("/list")
    public R<List<AddressBook>> list(HttpSession session) {
        return R.success(addressBookService.listByUserId(session.getAttribute("user")));
    }

    @PutMapping("/default")
    public R<String> setDefault(@RequestBody AddressBook addressBook, HttpSession session) {
        addressBook.setIsDefault(1);
        addressBookService.setDefault(addressBook, session.getAttribute("user"));
        return R.success("默认地址设置成功");
    }

    @GetMapping("/{id}")
    public R<AddressBook> get(@PathVariable("id") Long id) {
        return R.success(addressBookService.getById(id));
    }

    @DeleteMapping
    public R<String> delete(@RequestParam Long ids) {
        addressBookService.removeById(ids);
        return R.success("地址簿删除成功");
    }

    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook) {
        addressBookService.updateById(addressBook);
        return R.success("地址簿修改成功");
    }

    @GetMapping("/default")
    public R<AddressBook> getDefault(HttpSession session) {
        return R.success(addressBookService.getByUserIdAndDefault(session.getAttribute("user")));
    }
}
