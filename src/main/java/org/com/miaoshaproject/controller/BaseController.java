package org.com.miaoshaproject.controller;

import org.com.miaoshaproject.error.BussinessException;
import org.com.miaoshaproject.error.EmBusinessError;
import org.com.miaoshaproject.repondse.CommonReturnType;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BaseController {

    private static char[] invalidChar = {'/', '<', '>', '(', ')', ';', '"', '$', '|'};
    private static String[] validImageType = {".bmp", ".jpg", ".jpeg", ".png", ".gif"};
    private static String[] validVideoType = {".avi", ".mov", ".rmvb", ".rm", ".flv", ".mp4", ".3gp"};
    private static String[] validAudioType = {".mp3", ".wma", ".wav", ".midi", ".wma", ".m4a"};

    public static final String CONTENT_TYPE_FORMED="application/x-www-form-urlencoded";
    //exception handler
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception ex){
        Map<String,Object> respondseData = new HashMap<>();
        if(ex instanceof BussinessException){
            BussinessException bussinessException = (BussinessException)ex;

            respondseData.put("errCode",bussinessException.getErrCode());
            respondseData.put("errMsg",bussinessException.getErrMsg());

        }else{
            respondseData.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrCode());
            respondseData.put("errMsg",EmBusinessError.UNKNOWN_ERROR.getErrMsg());

        }
        return CommonReturnType.create(respondseData,"fail");
    }

    /**
     * 文件上传配置
     *
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //  单个数据大小
        factory.setMaxFileSize(DataSize.parse("-1")); // KB,MB
        /// 总上传数据大小
        factory.setMaxRequestSize(DataSize.parse("-1"));
        return factory.createMultipartConfig();
    }


    public static boolean checkValidChar(String input) {

        for (char c : invalidChar) {
            if (input.contains(String.valueOf(c)))
                return false;
        }

        return true;
    }

    public static boolean checkValidImageType(String input) {

        for (String postfix : validImageType) {
            if (input.toLowerCase().endsWith(postfix))
                return true;
        }

        return false;
    }

    public static boolean checkValidAudioType(String input) {

        for (String postfix : validAudioType) {
            if (input.toLowerCase().endsWith(postfix))
                return true;
        }

        return false;
    }

    public static boolean checkValidVideoType(String input) {

        for (String postfix : validVideoType) {
            if (input.toLowerCase().endsWith(postfix))
                return true;
        }

        return false;
    }
}
