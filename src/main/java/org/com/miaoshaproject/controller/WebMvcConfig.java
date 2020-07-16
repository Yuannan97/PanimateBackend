package org.com.miaoshaproject.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    //inject image path

    //@Value("${jdbc.driverclass}")
    //@Value("/fortest/**")

    @Value("${file.staticAccessPathImage}")
    private String staticAccessPathImage;
    @Value("${smas.captrue.image.path}")
    private String captureImagePath;
    @Value("${file.uploadFolderImage}")
    private String uploadFolderImage;

    //inject audio path
    @Value("${file.staticAccessPathAudio}")
    private String staticAccessPathAudio;
    @Value("${smas.captrue.audio.path}")
    private String captureAudioPath;
    @Value("${file.uploadFolderAudio}")
    private String uploadFolderAudio;

    //inject video path
    @Value("${file.staticAccessPathVideo}")
    private String staticAccessPathVideo;
    @Value("${smas.captrue.video.path}")
    private String captureVideoPath;
    @Value("${file.uploadFolderVideo}")
    private String uploadFolderVideo;


    //自动拦截访问如：http://localhost:8090/fortest/test.jpg，并返回test.jpg静态文件
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(staticAccessPathImage).addResourceLocations("file:" + uploadFolderImage + captureImagePath);
        registry.addResourceHandler(staticAccessPathAudio).addResourceLocations("file:" + uploadFolderAudio + captureAudioPath);
        registry.addResourceHandler(staticAccessPathVideo).addResourceLocations("file:" + uploadFolderVideo + captureVideoPath);
    }

}
