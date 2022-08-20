package com.van.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.van.takeout.dao.AddressBookDao;
import com.van.takeout.entity.AddressBook;
import com.van.takeout.service.AddressBookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookDao, AddressBook> implements AddressBookService {
    @Override
    public List<AddressBook> listByUserId(Object userId) {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, userId).orderByDesc(AddressBook::getUpdateTime);
        return list(queryWrapper);
    }

    @Override
    @Transactional
    public void setDefault(AddressBook addressBook, Object userId) {
        //把当前用户其他（所有）地址的is_default分量置0
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AddressBook::getUserId, userId).set(AddressBook::getIsDefault, 0);
        update(updateWrapper);
        //把id对应记录的is_default分量置1
        updateById(addressBook);
    }

    @Override
    public AddressBook getByUserIdAndDefault(Object userId) {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, userId).eq(AddressBook::getIsDefault, 1);
        return getOne(queryWrapper);
    }

    @Override
    public void saveForUser(AddressBook addressBook, Object userId) {
        addressBook.setUserId((Long) userId);
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, userId);
        //若该用户尚无地址，则此第一个地址为默认地址
        if (count(queryWrapper) == 0) {
            addressBook.setIsDefault(1);
        }
        save(addressBook);
    }
}
