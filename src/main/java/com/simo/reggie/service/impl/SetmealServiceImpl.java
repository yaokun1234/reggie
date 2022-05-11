package com.simo.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simo.reggie.dto.SetmealDto;
import com.simo.reggie.entity.Setmeal;
import com.simo.reggie.entity.SetmealDish;
import com.simo.reggie.mapper.SetmealMapper;
import com.simo.reggie.service.ISetmealDishService;
import com.simo.reggie.service.ISetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 套餐 服务实现类
 * </p>
 *
 * @author simo
 * @since 2022-05-04
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements ISetmealService {

    @Autowired
    ISetmealDishService iSetmealDishService;

    @Transactional
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.forEach((item)->{
            item.setSetmealId(String.valueOf(setmealDto.getId()));
        });
        iSetmealDishService.saveBatch(setmealDishes);
    }

    @Transactional
    @Override
    public void deleteSetmeal(String ids) {
        List<String> byIds = Arrays.asList(ids.split(","));
        this.removeByIds(byIds);
        LambdaUpdateWrapper<SetmealDish> in = new LambdaUpdateWrapper<SetmealDish>()
                .in(SetmealDish::getSetmealId, byIds);
        iSetmealDishService.remove(in);
    }

    @Transactional
    @Override
    public void editSetmeal(SetmealDto setmealDto) {
        this.updateById(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.forEach((item) -> {
            item.setSetmealId(String.valueOf(setmealDto.getId()));
        });
        iSetmealDishService.saveBatch(setmealDishes);
    }
}
