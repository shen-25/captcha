package com.zs.captcha.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author word
 */
@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {

    /**
     * 访问地址
     */
    private String endpoint;

    /**
     * 默认存储桶
     */
    private String bucketName;

}