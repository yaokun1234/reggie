package com.simo.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simo.reggie.commons.R;
import com.simo.reggie.dto.DishDto;
import com.simo.reggie.entity.Category;
import com.simo.reggie.entity.Dish;
import com.simo.reggie.entity.DishFlavor;
import com.simo.reggie.mapper.DishDtoMapper;
import com.simo.reggie.service.CategoryService;
import com.simo.reggie.service.IDishFlavorService;
import com.simo.reggie.service.IDishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜品管理 前端控制器
 * </p>
 *
 * @author simo
 * @since 2022-05-04
 */
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private IDishService iDishService;

    @Autowired
    private IDishFlavorService iDishFlavorService;

    @Autowired
    CategoryService categoryService;

    /**
     * 添加菜品
     * @param
     * @return
     */
    @PostMapping
    public R<String> addDish(@RequestBody DishDto dishDto){
        iDishService.save(dishDto);
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishDto.getId());
        }
        iDishFlavorService.saveBatch(flavors);
        return R.success("添加成功");
    }

    /**
     * 查询菜品分页
     * @param page
     * @param pageSize
     * @return
     */
    @Autowired
    DishDtoMapper dishDtoMapper;
    @GetMapping("/page")
    public R<Page> getDishPage(Long page,Long pageSize,String name){
        Page<DishDto> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<DishDto> dishDtoLambdaQueryWrapper = new LambdaQueryWrapper<DishDto>()
                .like(StringUtils.hasText(name), DishDto::getName, name)
                .orderByAsc(DishDto::getSort)
                .orderByDesc(DishDto::getUpdateTime);
        Page<DishDto> page1 = dishDtoMapper.page(pageInfo, dishDtoLambdaQueryWrapper);
        return R.success(page1);
    }
    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    @Transactional
    public R<String> deleteDish(String ids){
        String[] split = ids.split(",");
        LambdaUpdateWrapper<Dish> in = new LambdaUpdateWrapper<Dish>()
                .in(Dish::getId, split);
        iDishService.remove(in);
        LambdaUpdateWrapper<DishFlavor> inDishFlavor = new LambdaUpdateWrapper<DishFlavor>()
                .in(DishFlavor::getDishId, split);
        iDishFlavorService.remove(inDishFlavor);
        return R.success("删除成功");
    }

    /**
     * 更改菜品状态
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> dishStatus(@PathVariable Integer status,@RequestParam String ids){
        String[] split = ids.split(",");
        LambdaUpdateWrapper<Dish> set = new LambdaUpdateWrapper<Dish>()
                .in(Dish::getId,split)
                .set(Dish::getStatus, status);
        iDishService.update(set);
        return R.success("状态改变成功");
    }

    /**
     * 查询菜品通过ID
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> queryDishById(@PathVariable Long id){
        Dish dish = iDishService.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        LambdaQueryWrapper<DishFlavor> eq = new LambdaQueryWrapper<DishFlavor>()
                .eq(DishFlavor::getDishId, id);
        List<DishFlavor> flavors = iDishFlavorService.list(eq);
        dishDto.setFlavors(flavors);
        return R.success(dishDto);
    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    @Transactional
    public R<String> editDish(@RequestBody DishDto dishDto){
        iDishService.updateById(dishDto);
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishDto.getId());
        }
        LambdaUpdateWrapper<DishFlavor> eq = new LambdaUpdateWrapper<DishFlavor>()
                .eq(DishFlavor::getDishId, dishDto.getId());
        iDishFlavorService.remove(eq);
        iDishFlavorService.saveBatch(flavors);
        return R.success("修改菜品成功");
    }

//    @GetMapping("list")
//    public R<List<Dish>> queryDishList(Dish dish){
//        LambdaQueryWrapper<Dish> eq = new LambdaQueryWrapper<Dish>()
//                .eq(Dish::getCategoryId, dish.getCategoryId())
//                .eq(Dish::getStatus,1)
//                .orderByAsc(Dish::getSort);
//        List<Dish> list = iDishService.list(eq);
//        return R.success(list);
//    }
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);

        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = iDishService.list(queryWrapper);

        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //当前菜品的id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            //SQL:select * from dish_flavor where dish_id = ?
            List<DishFlavor> dishFlavorList = iDishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }
}
