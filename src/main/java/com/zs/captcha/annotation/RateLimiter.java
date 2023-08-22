package com.zs.captcha.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 接口访问频率注解，默认20秒只能访问1次
 * 限流注解，添加了 {@link } 必须通过 {@link Annotation} 获取，才会生效
 * @author 35536
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {


    long DEFAULT_REQUEST = 2;


    /**
     * count 最大请求数
     */
    long count() default DEFAULT_REQUEST;

    /**
     * 超时时长，默认20秒
     */
    long interval() default 20000;

    /**
     * 超时时间单位，默认毫秒
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}