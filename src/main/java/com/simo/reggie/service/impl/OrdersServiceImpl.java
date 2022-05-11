package com.simo.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.simo.reggie.commons.CustomException;
import com.simo.reggie.entity.*;
import com.simo.reggie.mapper.OrdersMapper;
import com.simo.reggie.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.simo.reggie.filter.LoginCheckFilter.threadLocal;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author simo
 * @since 2022-05-10
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {

    @Autowired
    IUserService iUserService;

    @Autowired
    IShoppingCartService iShoppingCartService;

    @Autowired
    IAddressBookService iAddressBookService;

    @Autowired
    IOrderDetailService iOrderDetailService;
    /**
     * 下单
     * @param orders
     */
    @Override
    @Transactional
    public void submit(Orders orders) {

        Long userId = threadLocal.get();
        User user = iUserService.getById(userId);
        //订单号
        long orderId = IdWorker.getId();

        //通过地址ID获得地址
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = iAddressBookService.getById(addressBookId);
        if(addressBook == null){
            throw new CustomException("用户地址信息有误，不能下单");
        }

        //计算订单金额
        AtomicInteger amount = new AtomicInteger(0);
        LambdaQueryWrapper<ShoppingCart> eq = new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId, userId);
        //通过用户Id获取购物车列表
        List<ShoppingCart> shoppingCartList = iShoppingCartService.list(eq);
        if(shoppingCartList == null || shoppingCartList.size() == 0){
            throw new CustomException("购物车为空，不能下单");
        }
        //订单细节，通过购物车填充订单细节
        List<OrderDetail> orderDetails = new ArrayList<>();
        shoppingCartList.forEach((item)->{
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            orderDetails.add(orderDetail);
        });
        //保存订单
        orders.setNumber(String.valueOf(orderId));
        orders.setStatus(2);
        orders.setUserId(userId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        this.save(orders);

       //保存订单明细
        iOrderDetailService.saveBatch(orderDetails);

       //清除购物车
        iShoppingCartService.remove(eq);
    }
}
