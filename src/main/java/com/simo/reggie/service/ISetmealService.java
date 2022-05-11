package com.simo.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.simo.reggie.dto.SetmealDto;
import com.simo.reggie.entity.Setmeal;

/**
 * <p>
 * 套餐 服务类
 * </p>
 *
 * @author simo
 * @since 2022-05-04
 */
public interface ISetmealService extends IService<Setmeal> {

    void saveWithDish(SetmealDto setmealDto);

    void deleteSetmeal(String ids);

    void editSetmeal(SetmealDto setmealDto);
}
