package com.zs.captcha.controller;

import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import cloud.tianai.captcha.spring.application.ImageCaptchaApplication;
import cloud.tianai.captcha.spring.vo.CaptchaResponse;
import cloud.tianai.captcha.spring.vo.ImageCaptchaVO;
import cloud.tianai.captcha.validator.common.model.dto.ImageCaptchaTrack;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/slider")
    public String slider() {
        return "slider";
    }

    @GetMapping("/rotate")
    public String rotate() {
        return "rotate";
    }

    @GetMapping("/concat")
    public String concat() {
        return "concat";
    }

    @GetMapping("/word-click")
    public String wordClick() {
        return "word-click";
    }

    @Autowired
    private ImageCaptchaApplication imageCaptchaApplication;

    @GetMapping("/gen")
    @ResponseBody
    public CaptchaResponse<ImageCaptchaVO> genCaptcha(@RequestParam(value = "type", required = false) String type) {
        if (StringUtils.isBlank(type)) {
            type = CaptchaTypeConstant.SLIDER;
        }
        CaptchaResponse<ImageCaptchaVO> response = imageCaptchaApplication.generateCaptcha(type);
        return response;
    }

    @PostMapping("/check")
    @ResponseBody
    public boolean checkCaptcha(@RequestParam("id") String id,
                                @RequestBody ImageCaptchaTrack imageCaptchaTrack,
                                HttpServletRequest request) {
        return imageCaptchaApplication.matching(id, imageCaptchaTrack).isSuccess();
    }


}