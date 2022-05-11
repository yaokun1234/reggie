package com.simo.reggie.mapper;

import com.simo.reggie.entity.Dish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 菜品管理 Mapper 接口
 * </p>
 *
 * @author simo
 * @since 2022-05-04
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {

}
