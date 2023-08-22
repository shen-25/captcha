package com.zs.captcha.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.captcha.mapper.UploadTaskMapper;
import com.zs.captcha.model.entity.UploadTask;
import com.zs.captcha.service.UploadTaskService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 分片上传-分片任务记录 服务实现类
 * </p>
 *
 * @author word
 * @since 2023-08-22
 */
@Service
public class UploadTaskServiceImpl extends ServiceImpl<UploadTaskMapper, UploadTask> implements UploadTaskService {

}
