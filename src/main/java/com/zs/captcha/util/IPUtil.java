package com.zs.captcha.util;

import cloud.tianai.captcha.validator.common.model.dto.ImageCaptchaTrack;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
public class IPUtil {

    public static void main(String[] args) {
        String data = "eyJiZ0ltYWdlV2lkdGgiOjYwMCwiYmdJbWFnZUhlaWdodCI6MzYwLCJzbGlkZXJJbWFnZVdpZHRoIjoxMTAsInNsaWRlckltYWdlSGVpZ2h0IjozNjAsInN0YXJ0U2xpZGluZ1RpbWUiOiIyMDIzLTA3LTIxVDA3OjI5OjI2LjgyOVoiLCJlbmRTbGlkaW5nVGltZSI6IjIwMjMtMDctMjFUMDc6MzE6MjIuODcyWiIsInRyYWNrTGlzdCI6W3sieCI6MCwieSI6MCwidHlwZSI6ImRvd24iLCJ0IjoxMTQ4OTF9LHsieCI6Mjk4LCJ5IjoxMSwidHlwZSI6InVwIiwidCI6MTE2MDQzfV19";
        String base64Decode = new String(Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        ImageCaptchaTrack sliderCaptchaTrack = new Gson().fromJson(base64Decode, ImageCaptchaTrack.class);
        System.out.println(sliderCaptchaTrack);
    }

    private final static String UNKNOWN = "unknown";
    private final static int MAX_LENGTH = 15;

    /**
     * 获取IP地址
     * 使用 Nginx 等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
     */
    public static String getIpAddr() {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            log.error("IPUtils ERROR ", e);
        }
        // 使用代理，则获取第一个IP地址
        if (!StringUtils.isEmpty(ip) && ip.length() > MAX_LENGTH) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

    public static String getRequestURI(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getRequestURI();
    }
}
