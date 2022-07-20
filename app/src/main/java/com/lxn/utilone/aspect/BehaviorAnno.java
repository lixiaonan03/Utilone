package com.lxn.utilone.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
  *  @author lixiaonan
  *  功能描述: 新建自定义注解  该注解目标对象是方法，是运行时注解
  *  时 间： 2020/12/13 8:28 PM
  */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BehaviorAnno {
    String value();
}

