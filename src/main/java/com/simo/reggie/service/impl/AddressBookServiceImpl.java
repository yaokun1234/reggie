package com.simo.reggie.service.impl;

import com.simo.reggie.entity.AddressBook;
import com.simo.reggie.mapper.AddressBookMapper;
import com.simo.reggie.service.IAddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 地址管理 服务实现类
 * </p>
 *
 * @author simo
 * @since 2022-05-10
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements IAddressBookService {

}
