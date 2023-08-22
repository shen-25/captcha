package com.zs.captcha.common;

import lombok.Getter;

/**
 * @author 35536
 */

@Getter
public enum RedisKeyPrefix {

    /**
     * redis的前置key
     */
    REQ_LIMIT("req_limit:", "用户请求限制");


    private final  String key;
    private final String desc;

    RedisKeyPrefix(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

}
