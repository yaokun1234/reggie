package com.simo.reggie.service;

import com.simo.reggie.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author simo
 * @since 2022-05-10
 */
public interface IOrdersService extends IService<Orders> {

    void submit(Orders orders);
}
