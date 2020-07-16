package org.com.miaoshaproject.controller;


import org.com.miaoshaproject.error.BussinessException;
import org.com.miaoshaproject.repondse.CommonReturnType;
import org.com.miaoshaproject.service.UserService;
import org.com.miaoshaproject.service.model.AudioModel;
import org.com.miaoshaproject.service.model.ImageModel;
import org.com.miaoshaproject.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller("audio")
@RequestMapping("/audio")
@CrossOrigin(value="*",maxAge=3600,allowedHeaders="*")
public class AudioController {

    @Autowired
    private UserService userService;


    //seting for uploading and loading imgs
    @Value("${upload.pathAudio}") String filePath;


    @RequestMapping(value = "/uploadMultiAudio", method = {RequestMethod.POST})
    @ResponseBody
    @Transactional
    public CommonReturnType uploadMultiImage(@RequestParam("audioFile") MultipartFile[] files,
                                             @RequestParam("user_id") Integer id) throws BussinessException, IOException {
        if(files.length==0) return CommonReturnType.create(null,"empty files");

        for(int i=0; i<files.length; i++) {
            if(files[i].isEmpty()){
                return CommonReturnType.create(null,"an empty files was found");
            }


            String filename = files[i].getOriginalFilename();

            if (!BaseController.checkValidChar(filename)) {
                return CommonReturnType.create(null,"invalid file name: " + filename);
            }

            if (!BaseController.checkValidAudioType(filename)) {
                return CommonReturnType.create(null,"invalid file type: " + filename);
            }
        }

        for(int i=0; i<files.length; i++){
            String filename = files[i].getOriginalFilename();

            File dest = new File(filePath + filename);
            try {
                files[i].transferTo(dest);
                System.out.println("uploading success");
            }catch (IOException e){
                e.printStackTrace();
            }

            //ImageModel imageModel = new ImageModel(files[i].getOriginalFilename(), files[i].getContentType(), compressBytes(files[i].getBytes()));
            String accessPath="http://localhost:8090/foraudio/";
            AudioModel audioModel = new AudioModel(files[i].getOriginalFilename(),files[i].getContentType(),accessPath+filename,new Date().toString());
            audioModel.setId(id);

            //System.out.println("Compressed Image ByteSize - " + imageModel.getImage().length);
            //System.out.println("Compressed Image ByteSize - " + imageModel.getImage());
            int currentAudioId = userService.getUserById(id).getAudioNum();
            audioModel.setAudioId(currentAudioId+1);

            UserModel newModel = userService.getUserById(id);
            newModel.setAudioNum(currentAudioId+1);
            userService.updateData(newModel);

            userService.storeAudio(audioModel);
        }
        return CommonReturnType.create(null);
    }


    @RequestMapping("/getAudio")
    @ResponseBody
   public CommonReturnType getAudio() throws IOException, BussinessException {
    //public CommonReturnType getAudio() throws IOException, BussinessException{
        //@RequestParam("user_Id") Integer imageId
        //调用service层获取对应id对象并返回给前端
        Integer id = 1;
        List<AudioModel> audioModelList = userService.listAudio(id);

        for (AudioModel audioModel : audioModelList) {
            // imageModel.setImage(decompressBytes(imageModel.getImage()));
            audioModel.setAudio(audioModel.getAudio());
        }

        return CommonReturnType.create(audioModelList);


    }


}
