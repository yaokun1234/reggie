package com.simo.reggie.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simo.reggie.dto.SetmealDto;
import com.simo.reggie.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealDtoMapper extends BaseMapper<SetmealDto> {

    String getSetmealPageSql = "SELECT s.*,c.name as categoryName" +
            " FROM setmeal AS s LEFT JOIN category AS c ON s.category_id = c.id" +
            " ${ew.customSqlSegment}";

    @Select(getSetmealPageSql)
    Page<SetmealDto> getSetmealPage(Page<SetmealDto> page, @Param("ew")Wrapper wrapper);
}
