package com.simo.reggie.mapper;

import com.simo.reggie.entity.AddressBook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 地址管理 Mapper 接口
 * </p>
 *
 * @author simo
 * @since 2022-05-10
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {

}
