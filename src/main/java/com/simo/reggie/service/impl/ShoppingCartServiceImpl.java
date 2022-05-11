package com.simo.reggie.service.impl;

import com.simo.reggie.entity.ShoppingCart;
import com.simo.reggie.mapper.ShoppingCartMapper;
import com.simo.reggie.service.IShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 购物车 服务实现类
 * </p>
 *
 * @author simo
 * @since 2022-05-10
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements IShoppingCartService {

}
