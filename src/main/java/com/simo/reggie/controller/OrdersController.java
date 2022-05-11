package com.simo.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simo.reggie.commons.R;
import com.simo.reggie.entity.Orders;
import com.simo.reggie.service.IOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author simo
 * @since 2022-05-10
 */
@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    IOrdersService iOrdersService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        iOrdersService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("/page")
    public R<Page<Orders>> orderPage(Long page, Long pageSize, String number, String beginTime,String endTime){
        Page<Orders> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<Orders>()
                .like(StringUtils.hasText(number), Orders::getNumber, number)
                .between((beginTime != null && endTime != null),Orders::getOrderTime,beginTime,endTime)
                .orderByDesc(Orders::getOrderTime);
        Page<Orders> resPage = iOrdersService.page(pageInfo, queryWrapper);
        return R.success(resPage);
    }

    @PutMapping
    public R<String> orderStatus(@RequestBody Orders orders){
        iOrdersService.updateById(orders);
        return R.success("订单状态更改成功");
    }

}
