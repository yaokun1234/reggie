package com.simo.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simo.reggie.entity.Dish;
import com.simo.reggie.mapper.DishMapper;
import com.simo.reggie.service.IDishService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜品管理 服务实现类
 * </p>
 *
 * @author simo
 * @since 2022-05-04
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements IDishService {

}
