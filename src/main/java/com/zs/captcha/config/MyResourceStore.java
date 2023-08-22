package com.zs.captcha.config;

import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import cloud.tianai.captcha.generator.common.constant.SliderCaptchaConstant;
import cloud.tianai.captcha.generator.impl.StandardSliderImageCaptchaGenerator;
import cloud.tianai.captcha.resource.common.model.dto.Resource;
import cloud.tianai.captcha.resource.common.model.dto.ResourceMap;
import cloud.tianai.captcha.resource.impl.DefaultResourceStore;
import cloud.tianai.captcha.resource.impl.provider.ClassPathResourceProvider;
import cloud.tianai.captcha.resource.impl.provider.URLResourceProvider;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zs.captcha.model.entity.UploadTask;
import com.zs.captcha.service.UploadTaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;

import static cloud.tianai.captcha.generator.impl.StandardSliderImageCaptchaGenerator.DEFAULT_SLIDER_IMAGE_TEMPLATE_PATH;

/**
 *  负责模板和背景图存储的地方
 */
@Component
@ConditionalOnClass({MinioConfig.class})
public class MyResourceStore extends DefaultResourceStore {

    public MyResourceStore() {

        // 滑块验证码 模板 (系统内置)
        ResourceMap template1 = new ResourceMap(4);
        template1.put(SliderCaptchaConstant.TEMPLATE_ACTIVE_IMAGE_NAME, new Resource(ClassPathResourceProvider.NAME, DEFAULT_SLIDER_IMAGE_TEMPLATE_PATH.concat("/1/active.png")));
        template1.put(SliderCaptchaConstant.TEMPLATE_FIXED_IMAGE_NAME, new Resource(ClassPathResourceProvider.NAME, DEFAULT_SLIDER_IMAGE_TEMPLATE_PATH.concat("/1/fixed.png")));
        ResourceMap template2 = new ResourceMap(4);
        template2.put(SliderCaptchaConstant.TEMPLATE_ACTIVE_IMAGE_NAME, new Resource(ClassPathResourceProvider.NAME, DEFAULT_SLIDER_IMAGE_TEMPLATE_PATH.concat("/2/active.png")));
        template2.put(SliderCaptchaConstant.TEMPLATE_FIXED_IMAGE_NAME, new Resource(ClassPathResourceProvider.NAME, DEFAULT_SLIDER_IMAGE_TEMPLATE_PATH.concat("/2/fixed.png")));
        // 旋转验证码 模板 (系统内置)
        ResourceMap template3 = new ResourceMap(4);
        template3.put(SliderCaptchaConstant.TEMPLATE_ACTIVE_IMAGE_NAME, new Resource(ClassPathResourceProvider.NAME, StandardSliderImageCaptchaGenerator.DEFAULT_SLIDER_IMAGE_TEMPLATE_PATH.concat("/3/active.png")));
        template3.put(SliderCaptchaConstant.TEMPLATE_FIXED_IMAGE_NAME, new Resource(ClassPathResourceProvider.NAME, StandardSliderImageCaptchaGenerator.DEFAULT_SLIDER_IMAGE_TEMPLATE_PATH.concat("/3/fixed.png")));

        // 1. 添加一些模板
        addTemplate(CaptchaTypeConstant.SLIDER, template1);
        addTemplate(CaptchaTypeConstant.SLIDER, template2);
        addTemplate(CaptchaTypeConstant.ROTATE, template3);
    }

    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private UploadTaskService uploadTaskService;

    @PostConstruct
    public void init(){
        // 添加图片逻辑
        addPic();
    }

    public String getUrlPath(String objectKey) {
        return minioConfig.getEndpoint() + "/" + minioConfig.getBucketName() + "/" + objectKey;
    }

    private void addPic() {
        List<UploadTask> uploadTaskList = uploadTaskService.list(new QueryWrapper<UploadTask>()
                .eq("bucket_name", minioConfig.getBucketName()));
        if (!CollectionUtils.isEmpty(uploadTaskList)) {
            for (UploadTask uploadTask : uploadTaskList) {
                String url = getUrlPath(uploadTask.getObjectKey());
                addResource(CaptchaTypeConstant.SLIDER,new Resource(URLResourceProvider.NAME, url));
            }
        }else{
            throw new RuntimeException("没有背景图,验证码项目启动失败");
        }
    }

}
