package com.simo.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simo.reggie.commons.R;
import com.simo.reggie.dto.SetmealDto;
import com.simo.reggie.entity.Setmeal;
import com.simo.reggie.entity.SetmealDish;
import com.simo.reggie.mapper.SetmealDtoMapper;
import com.simo.reggie.service.ISetmealDishService;
import com.simo.reggie.service.ISetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 套餐 前端控制器
 * </p>
 *
 * @author simo
 * @since 2022-05-04
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealDtoMapper setmealDtoMapper;
    @Autowired
    private ISetmealService iSetmealService;
    @Autowired
    private ISetmealDishService iSetmealDishService;


    /**
     * 查询套餐页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page<SetmealDto>> getSetmealPage(Long page, Long pageSize, String name) {
        Page<SetmealDto> setmealDtoPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<SetmealDto> queryWrapper = new LambdaQueryWrapper<SetmealDto>()
                .like(StringUtils.hasText(name),SetmealDto::getName,name)
                .orderByDesc(SetmealDto::getUpdateTime);
        Page<SetmealDto> resPage = setmealDtoMapper.getSetmealPage(setmealDtoPage,queryWrapper);
        return R.success(resPage);
    }

    @GetMapping("/{id}")
    public R<SetmealDto> querySetmealById(@PathVariable Long id){
        Setmeal resSetmeal = iSetmealService.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(resSetmeal,setmealDto);
        LambdaQueryWrapper<SetmealDish> eq = new LambdaQueryWrapper<SetmealDish>()
                .eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> list = iSetmealDishService.list(eq);
        setmealDto.setSetmealDishes(list);
        return  R.success(setmealDto);
    }

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){

        iSetmealService.saveWithDish(setmealDto);

        return R.success("新增套餐成功");
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteSetmeal(String ids){
        iSetmealService.deleteSetmeal(ids);
        return R.success("删除套餐成功");
    }

    @PostMapping("/status/{status}")
    public R<String> changeStatus(@PathVariable Integer status,String ids){
        String[] ByIds = ids.split(",");
        LambdaUpdateWrapper<Setmeal> set = new LambdaUpdateWrapper<Setmeal>()
                .in(Setmeal::getId, ByIds)
                .set(Setmeal::getStatus, status);
        iSetmealService.update(set);
        return R.success("状态更改成功");
    }

    @PutMapping
    public R<String> editSetmeal(@RequestBody SetmealDto setmealDto){
        iSetmealService.editSetmeal(setmealDto);
        return R.success("修改套餐成功");
    }

    /**
     * 根据条件查询套餐数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = iSetmealService.list(queryWrapper);

        return R.success(list);
    }
}
