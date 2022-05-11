package com.simo.reggie.dto;

import com.simo.reggie.entity.Setmeal;
import com.simo.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
