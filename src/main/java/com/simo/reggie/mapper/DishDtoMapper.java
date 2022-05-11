package com.simo.reggie.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simo.reggie.dto.DishDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishDtoMapper extends BaseMapper<DishDto> {
    String querySql = "SELECT a.*, b.name as categoryName FROM dish AS a LEFT JOIN category AS b " +
            "ON b.id = a.category_id ${ew.customSqlSegment}";

    //String wrapperSql = "SELECT * from ( " + querySql + " ) AS q ${ew.customSqlSegment}";

    @Select(querySql)
    Page<DishDto> page(Page page, @Param("ew") Wrapper queryWrapper);

}
