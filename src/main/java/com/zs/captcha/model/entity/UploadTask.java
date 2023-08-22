package com.zs.captcha.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 分片上传-分片任务记录
 * </p>
 *
 * @author word
 * @since 2023-08-22
 */
@Data
@TableName("upload_task")
public class UploadTask implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 分片上传的uploadId
     */
    private String uploadId;

    /**
     * 文件唯一标识（md5）
     */
    private String fileIdentifier;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 所属桶名
     */
    private String bucketName;

    /**
     * 文件的key
     */
    private String objectKey;

    /**
     * 文件大小（byte）
     */
    private Long totalSize;

    /**
     * 每个分片大小（byte）
     */
    private Long chunkSize;

    /**
     * 分片数量
     */
    private Integer chunkNum;

}
