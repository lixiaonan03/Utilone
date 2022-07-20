package com.lxn.utilone.aspect;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
  *  @author lixiaonan
  *  功能描述: 插桩类的
  *  时 间： 2020/12/13 8:11 PM
  */
@Aspect
public class SpendTimeAspect {

    //切入点
    @Pointcut("execution(BaseActivity.onCreate(..))")
    public void injectSpendCode() {
        //生命所有的类中oncreate方法，作为切入点
    }

    @Around("injectSpendCode()")
    public void aroundOnCreate(ProceedingJoinPoint joinPoint) throws Throwable {
        //插入的代码
        long start = System.currentTimeMillis();
        //执行原来的代码
        Object[] params = joinPoint.getArgs();
        joinPoint.proceed(params);
        //插入的代码
        Log.i("lxnDemo","method spend "+(System.currentTimeMillis()-start));
    }

//     （1）同一个切入点，After、Before、Around最多可以有两个，三者不能同时出现。
//     （2）不同切入点@After和@Before可以共存，但@Around不能与@After/@Before共存，否则无法编译
    //  配置前置通知
//        @Before("injectSpendCode()")
//        public void beforeMethod(JoinPoint joinPoint) throws Throwable {
//            Log.i("lxnDemo", "执行前-----------");
//        }
//
//        //  配置后置通知
//        @After("injectSpendCode()")
//        public void afterMethod(JoinPoint joinPoint) throws Throwable {
//            Log.i("lxnDemo", "执行后------------");
//        }



}