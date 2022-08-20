package com.van.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.van.takeout.entity.AddressBook;

import java.util.List;

public interface AddressBookService extends IService<AddressBook> {
    List<AddressBook> listByUserId(Object userId);

    void setDefault(AddressBook addressBook, Object userId);

    AddressBook getByUserIdAndDefault(Object userId);

    void saveForUser(AddressBook addressBook, Object userId);
}
