package com.simo.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simo.reggie.entity.Employee;
import com.simo.reggie.mapper.EmployeeMapper;
import com.simo.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceIpml extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService{
}
