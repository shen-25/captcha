package com.zs.captcha.aspect;


import com.zs.captcha.annotation.RateLimiter;
import com.zs.captcha.common.RedisKeyPrefix;
import com.zs.captcha.util.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.concurrent.*;

/**
 * @author word
 */
@Aspect
@Component
@Slf4j
public class RateLimiterAspect {

    @Autowired
    private  StringRedisTemplate  stringRedisTemplate;


    @Autowired
    private  RedisScript<Long> limitRedisScript;

    // 切点
    @Pointcut("@annotation(rateLimiter)")
    public void controllerAspect( RateLimiter rateLimiter) {

    }


    @Around("controllerAspect(rateLimiter)")
    public Object doAround(ProceedingJoinPoint joinPoint, RateLimiter rateLimiter) throws Throwable {
        if (rateLimiter != null) {
            String ip = IPUtil.getIpAddr();
            String uri = IPUtil.getRequestURI();
             // 最终的 key 格式为：
            String key = RedisKeyPrefix.REQ_LIMIT.getKey() + ip + ":" + uri;
            long max = rateLimiter.count();
            long timeout = rateLimiter.interval();
            TimeUnit timeUnit = rateLimiter.timeUnit();
            // 判断是否需要限流
            boolean limited = shouldLimited(key, max, timeout, timeUnit);
            if (limited) {
                throw new RuntimeException("手速太快了，慢点儿吧~");
            }
        }
        return joinPoint.proceed();
    }

    private boolean shouldLimited(String key, long max, long timeout, TimeUnit timeUnit) {
        // 统一使用单位毫秒
        long ttl = timeUnit.toMillis(timeout);
        // 当前时间毫秒数
        long now = Instant.now().toEpochMilli();
        long expired = now - ttl;
        // 注意这里必须转为 String,否则会报错 java.lang.Long cannot be cast to java.lang.String
        Long executeTimes = stringRedisTemplate.execute(limitRedisScript, Collections.singletonList(key),
                String.valueOf(now), String.valueOf(ttl), String.valueOf(expired), String.valueOf(max));
        if (executeTimes != null) {
            if (executeTimes == 0) {
                log.warn("【{}】在单位时间 {} 毫秒内已达到访问上限，当前接口上限 {}", key, ttl, max);
                return true;
            }
        }
        return false;
    }

}
