package org.com.miaoshaproject.controller;

import org.com.miaoshaproject.error.BussinessException;
import org.com.miaoshaproject.error.EmBusinessError;
import org.com.miaoshaproject.repondse.CommonReturnType;
import org.com.miaoshaproject.service.UserService;
import org.com.miaoshaproject.service.model.TextModel;
import org.com.miaoshaproject.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller("text")
@RequestMapping("/text")
@CrossOrigin(value="*",maxAge=3600,allowedHeaders="*")
public class TextController extends BaseController{

    @Value("${upload.pathText}") String filePath;

    @Autowired
    private UserService userservice;

    @RequestMapping("/uploadText")
    @ResponseBody
    public CommonReturnType uploadText(@RequestParam("text") MultipartFile[] files,
                                       @RequestParam("user_id") Integer id) throws BussinessException
    {


        if(files.length == 0) return CommonReturnType.create(null,"empty files");
        //validate if the any of the file is empty
        for(int i = 0; i < files.length; i++) {
            if (files[i].isEmpty()) {
                return CommonReturnType.create(null, "an empty file is found, please check");
            }

            //构造文件路径并新建文件
            String originalName = files[i].getOriginalFilename();
            File dest = new File(filePath+originalName);

            //将文件存到指定路径下， 后端显示上传成功
            try {
                files[i].transferTo(dest);
                System.out.println("Text Uploaded Successfully");
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 利用拦截器可以获取文件的特性
            //构造一个新的text model 将所有元素尝试加进去
            String accessPath = "http://localhost:8090/fortext/";
            TextModel textModel = new TextModel(files[i].getOriginalFilename(),files[i].getContentType(),accessPath+originalName,new Date().toString());
            textModel.setId(id);
            int currentTextId =userservice.getUserById(id).getTextNum();
            textModel.setTextId(currentTextId+1);

            UserModel usermodel = userservice.getUserById(id);
            usermodel.setTextNum(usermodel.getTextNum()+1);

            // 利用userservice 去将model 加入数据库，并且更新主表信息
            userservice.updateData(usermodel);
            userservice.storeText(textModel);
        }
        return CommonReturnType.create(null);
    }
    @RequestMapping("/getText")
    @ResponseBody
    public CommonReturnType getText(@RequestParam("user_Id") Integer id) throws BussinessException
    {
        // 用userservice 里的listText list audio

        //利用UserTExtDOmapper 读取数据 获取userDO
        //然后把userDO 转换成TextModel
        List<TextModel> list = userservice.listText(id);

        if(list == null)
        {
            throw new BussinessException(EmBusinessError.TEXT_NOT_EXIST);
        }

        return CommonReturnType.create(list);

    }
}
