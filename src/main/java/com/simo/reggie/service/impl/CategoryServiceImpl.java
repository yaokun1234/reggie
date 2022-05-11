package com.simo.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simo.reggie.commons.CustomException;
import com.simo.reggie.entity.Category;
import com.simo.reggie.entity.Dish;
import com.simo.reggie.entity.Setmeal;
import com.simo.reggie.mapper.CategoryMapper;
import com.simo.reggie.service.CategoryService;
import com.simo.reggie.service.IDishService;
import com.simo.reggie.service.ISetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private IDishService iDishService;
    @Autowired
    private ISetmealService iSetmealService;

    /**
     * 增强删除分类操作
     * @param id
     * @return
     */
    @Override
    public boolean removeById(Serializable id) {
        //查询分类是否关联菜品
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<Dish>()
                .eq(Dish::getCategoryId,id);
        int count1 = iDishService.count(dishLambdaQueryWrapper);
        //关联则抛异常
        if(count1 > 0){
            throw new CustomException("当前分类关联了菜品，不能删除");
        }

        //查询分类是否关联套餐
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<Setmeal>()
                .eq(Setmeal::getCategoryId, id);
        //关联则抛异常
        int count2 = iSetmealService.count(setmealLambdaQueryWrapper);
        if(count2 > 0){
            throw new CustomException("当前分类关联了套餐，不能删除");
        }
        return super.removeById(id);
    }
}
