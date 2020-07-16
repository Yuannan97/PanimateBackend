package org.com.miaoshaproject.controller;

import org.com.miaoshaproject.dataobject.UserVideoDO;
import org.com.miaoshaproject.error.BussinessException;
import org.com.miaoshaproject.repondse.CommonReturnType;
import org.com.miaoshaproject.service.UserService;
import org.com.miaoshaproject.service.model.AudioModel;
import org.com.miaoshaproject.service.model.UserModel;
import org.com.miaoshaproject.service.model.VideoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller("video")
@RequestMapping("/video")
@CrossOrigin(value="*",maxAge=3600,allowedHeaders="*")
public class VideoController {

    @Autowired
    private UserService userService;

    @Value("${upload.pathVideo}") String filePath;

    @RequestMapping(value = "/uploadMultiVideo", method = {RequestMethod.POST})
    @ResponseBody
    @Transactional
    public CommonReturnType uploadMultiVideo(@RequestParam("videoFile") MultipartFile[] files,
                                             @RequestParam("user_id") Integer id) throws BussinessException, IOException {
        // if the videoFile is empty

        if (files.length == 0) {
            return CommonReturnType.create(null, "empty file");
        }

        for (int i = 0; i < files.length; i++) {
            if (files[i].isEmpty()) {
                System.out.println(files[i]);
                return CommonReturnType.create(null, "an empty files was found");
            }

            String filename = files[i].getOriginalFilename();

            if (!BaseController.checkValidChar(filename)) {
                return CommonReturnType.create(null,"invalid file name: " + filename);
            }

            if (!BaseController.checkValidVideoType(filename)) {
                return CommonReturnType.create(null,"invalid file type: " + filename);
            }
        }

        // if the file[i] is empty
        for (int i = 0; i < files.length; i++) {
            String filename = files[i].getOriginalFilename();

            File dest = new File(filePath + filename);
           // System.out.println(dest);
            try {
                files[i].transferTo(dest);
                System.out.println("uploading success");
            } catch (IOException e) {
                e.printStackTrace();
            }
            String accessPath="http://localhost:8090/forvideo/";
            VideoModel videoModel = new VideoModel(files[i].getOriginalFilename(),files[i].getContentType(),accessPath+filename,new Date().toString());
            videoModel.setId(id);

            int currentVideoId =  userService.getUserById(id).getVideoNum();
            videoModel.setVideoId(currentVideoId+1);

            UserModel userModel = userService.getUserById(id);
            userModel.setVideoNum(currentVideoId+1);

            userService.updateData(userModel);
            userService.storeVideo(videoModel);
        }
        return CommonReturnType.create(null);
    }
    @RequestMapping("/getVideo")
    @ResponseBody
    public CommonReturnType getVideo() throws IOException, BussinessException {
        //@RequestParam("user_Id") Integer imageId
        //调用service层获取对应id对象并返回给前端
        Integer id = 1;
       List<VideoModel> videoModelList  = userService.listVideo(id);

//        for(VideoModel videoModel: videoModelList)
//        {
//            videoModel.setVideo(videoModel.getVideo());
//        }

        return CommonReturnType.create(videoModelList);


    }
}
