package com.zs.captcha.enums;


import lombok.Getter;

@Getter
public enum CaptchaType {

    /** 滑块. */
    SLIDER(0, "SLIDER"),
    /** 旋转. */
     ROTATE(1, "ROTATE"),
    /** 旋转角度.*/
     ROTATE_DEGREE(2,"ROTATE_DEGREE"),
    /** 拼接.*/
     CONCAT(3, "CONCAT"),
    /** 拼图.*/
     JIGSAW(4, "JIGSAW"),
    /** 图片点选.*/
     IMAGE_CLICK(5, "IMAGE_CLICK"),
    /** 文字图片点选.*/
     WORD_IMAGE_CLICK(6, "WORD_IMAGE_CLICK"),
    /** 语序点选.*/
     WORD_ORDER_IMAGE_CLICK(7, "WORD_ORDER_IMAGE_CLICK")
    ;

    private final Integer type;
    private final String desc;

    CaptchaType(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }


}
