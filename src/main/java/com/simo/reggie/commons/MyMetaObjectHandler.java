package com.simo.reggie.commons;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.simo.reggie.filter.LoginCheckFilter.threadLocal;

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime",LocalDateTime.now(),metaObject);
        this.setFieldValByName("updateTime",LocalDateTime.now(),metaObject);
        this.setFieldValByName("createUser",threadLocal.get(),metaObject);
        this.setFieldValByName("updateUser",threadLocal.get(),metaObject);
//        metaObject.setValue("createTime", LocalDateTime.now());
//        metaObject.setValue("updateTime",LocalDateTime.now());
//        metaObject.setValue("createUser",threadLocal.get());
//        metaObject.setValue("updateUser",threadLocal.get());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime",LocalDateTime.now(),metaObject);
        this.setFieldValByName("updateUser",threadLocal.get(),metaObject);
//        metaObject.setValue("updateTime",LocalDateTime.now());
//        metaObject.setValue("updateUser",threadLocal.get());
    }
}
