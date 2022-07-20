package com.lxn.utilone.aspect;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
  *  @author lixiaonan
  *  功能描述: 插桩类的
  *  时 间： 2020/12/13 8:11 PM
  */
@Aspect
public class LxnAnnoAspect {



    // 切入点
    @Pointcut("execution(@com.lxn.utilone.aspect.BehaviorAnno * * (..))")
    public void clickBehavior() {
        Log.i("lxnDemo", "clickBehavior自定义注解------------");
    }

    //关联的操作methodAnnotatedWithBehaviorTraceAspect()
    @Around("clickBehavior()")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable{
        Object result=null;
        if(null!=joinPoint){
            MethodSignature methodSignature=(MethodSignature)joinPoint.getSignature();
            String className=methodSignature.getDeclaringType().getSimpleName();
            String methodName=methodSignature.getName();
            String funName=methodSignature.getMethod().getAnnotation(BehaviorAnno.class).value();
            Log.i("lxnDemo","arround method is executed.before=="+funName);

            //统计时间
            long begin=System.currentTimeMillis();
            result=joinPoint.proceed();
            long duration=System.currentTimeMillis()-begin;
            Log.i("lxnDemo",String.format("around method is executed .after 功能：%s,%s类的%s方法执行了，用时%d ms",funName,className,methodName,duration));
            return result;
        }
        return result;
    }



}