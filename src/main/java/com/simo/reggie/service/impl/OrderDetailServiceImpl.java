package com.simo.reggie.service.impl;

import com.simo.reggie.entity.OrderDetail;
import com.simo.reggie.mapper.OrderDetailMapper;
import com.simo.reggie.service.IOrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单明细表 服务实现类
 * </p>
 *
 * @author simo
 * @since 2022-05-10
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {

}
