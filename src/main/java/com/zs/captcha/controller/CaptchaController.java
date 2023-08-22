package com.rq.captcha.controller;

import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import cloud.tianai.captcha.common.response.ApiResponse;
import cloud.tianai.captcha.common.response.ApiResponseStatusConstant;
import cloud.tianai.captcha.generator.common.model.dto.GenerateParam;
import cloud.tianai.captcha.spring.application.ImageCaptchaApplication;
import cloud.tianai.captcha.spring.vo.CaptchaResponse;
import cloud.tianai.captcha.spring.vo.ImageCaptchaVO;
import cloud.tianai.captcha.validator.common.model.dto.ImageCaptchaTrack;
import com.google.gson.Gson;
import com.zs.captcha.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping("captcha")
@Slf4j
public class CaptchaController {
    @Autowired
    private ImageCaptchaApplication captchaApplication;

    // 添加限流
    // 目前只支持slider
    @RateLimiter(count = 10000000)
    @GetMapping("/gen")
    public ApiResponse<CaptchaResponse<ImageCaptchaVO>>  genCaptcha(
                               @RequestParam(value = "type", required = false) String type)  {
        if (StringUtils.isBlank(type)) {
            type = CaptchaTypeConstant.SLIDER;
        }
        type = StringUtils.upperCase(type);
        //判断前端输入的 type 是否规范
        if (StringUtils.equals(type, CaptchaTypeConstant.SLIDER)) {
            return ApiResponse.ofError("type类型不正确,只支持滑动验证码");
        }
        GenerateParam param = new GenerateParam();
        param.setType(type);
        param.setBackgroundImageTag(null);
        param.setTemplateImageTag(null);
        // 图片是否重叠
        //  param.setShuffleImage(true);
        CaptchaResponse<ImageCaptchaVO> responseData = captchaApplication.generateCaptcha(param);
        return ApiResponse.ofSuccess(responseData);
    }

    @PostMapping("/check")
    @RateLimiter(count = 500, interval = 120000)
    public  ApiResponse<?>  checkCaptcha(@RequestParam("id") String id,
                                @RequestBody ImageCaptchaTrack imageCaptchaTrack) {
        return captchaApplication.matching(id, imageCaptchaTrack);
    }


    /**
     * 压缩imageCaptchaTrack形式
     */
    @PostMapping("/check")
    @RateLimiter(count = 10000000)
    public ApiResponse<?> checkCaptcha2(@RequestParam("id") String id,
                                        @RequestParam("data") String data) {
        ImageCaptchaTrack sliderCaptchaTrack;
        try {
            String base64Decode = new String(Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
            sliderCaptchaTrack = new Gson().fromJson(base64Decode, ImageCaptchaTrack.class);
        } catch (Exception e) {
            // 解密失败
            return ApiResponse.ofMessage(ApiResponseStatusConstant.NOT_VALID_PARAM);
        }
        return captchaApplication.matching(id, sliderCaptchaTrack);
    }

}
