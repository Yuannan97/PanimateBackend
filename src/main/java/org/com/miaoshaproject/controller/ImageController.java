package org.com.miaoshaproject.controller;

import org.com.miaoshaproject.error.BussinessException;
import org.com.miaoshaproject.repondse.CommonReturnType;
import org.com.miaoshaproject.service.UserService;
import org.com.miaoshaproject.service.model.ImageModel;
import org.com.miaoshaproject.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Controller("image")
@RequestMapping("/image")
@CrossOrigin(value="*",maxAge=3600,allowedHeaders="*")
public class ImageController {


    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    //seting for uploading and loading imgs
    @Value("${upload.pathImage}") String filePath;



    @RequestMapping(value = "/uploadPicture", method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType uploadImage(@RequestParam("imageFile") MultipartFile file,
                                        @RequestParam("id") Integer id) throws IOException, BussinessException {


        String filename = file.getOriginalFilename();

        if (!BaseController.checkValidChar(filename)) {
            return CommonReturnType.create(null,"invalid file name: " + filename);
        }

        System.out.println("fileName!!!" + filename);
        if (!BaseController.checkValidImageType(filename)) {
            return CommonReturnType.create(null,"invalid file type: " + filename);
        }

        System.out.println("Original Image ByteSize - " + file.getBytes().length);
        ImageModel imageModel = new ImageModel(file.getOriginalFilename(), file.getContentType(), filePath + filename);
//        ImageModel imageModel = new ImageModel(file.getOriginalFilename(), file.getContentType(), file.getBytes());
        imageModel.setId(id);
        userService.storeImage(imageModel);


        File dest = new File(filePath + filename);
        try {
            file.transferTo(dest);
            System.out.println("uploading success");
        }catch (IOException e){
            e.printStackTrace();
        }


        return CommonReturnType.create(imageModel);
    }



    @RequestMapping(value = "/uploadMultiPicture", method = {RequestMethod.POST})
    @ResponseBody
    @Transactional
    public CommonReturnType uploadMultiImage(@RequestParam("imageFile") MultipartFile[] files,
                                             @RequestParam("user_id") Integer id) throws BussinessException, IOException {
        if(files.length==0) return CommonReturnType.create(null,"empty files");

        // 检测不合法情况
        for(int i=0; i<files.length; i++) {
            if(files[i].isEmpty()) {
                return CommonReturnType.create(null, "an empty files was found");
            }

            String filename = files[i].getOriginalFilename();

            if (!BaseController.checkValidImageType(filename)) {
                return CommonReturnType.create(null,"invalid file type: " + filename);
            }
        }

        for(int i=0; i<files.length; i++){
            String filename = files[i].getOriginalFilename();

            if (!BaseController.checkValidImageType(filename)) {
                return CommonReturnType.create(null,"invalid file type: " + filename);
            }

            File dest = new File(filePath + filename);
            try {
                files[i].transferTo(dest);
                System.out.println("uploading success");
            }catch (IOException e){
                e.printStackTrace();
            }


            //ImageModel imageModel = new ImageModel(files[i].getOriginalFilename(), files[i].getContentType(), compressBytes(files[i].getBytes()));
            String accessPath="http://localhost:8090/fortest/";
            ImageModel imageModel = new ImageModel(files[i].getOriginalFilename(), files[i].getContentType(), accessPath+filename);
            imageModel.setId(id);

            //System.out.println("Compressed Image ByteSize - " + imageModel.getImage().length);
            //System.out.println("Compressed Image ByteSize - " + imageModel.getImage());
            int currentImageId = userService.getUserById(id).getImageNum();
            imageModel.setImageId(currentImageId+1);

            UserModel newModel = userService.getUserById(id);
            newModel.setImageNum(currentImageId+1);
            userService.updateData(newModel);

            userService.storeImage(imageModel);
        }
        return CommonReturnType.create(null);
    }

    @RequestMapping("/getPicture")
    @ResponseBody
    public CommonReturnType getImage() throws IOException, BussinessException {
        //@RequestParam("user_Id") Integer imageId
        //调用service层获取对应id对象并返回给前端
        Integer imageId = 1;
        List<ImageModel> imageModelList = userService.listImage(imageId);

        for (ImageModel imageModel : imageModelList) {
            // imageModel.setImage(decompressBytes(imageModel.getImage()));
            imageModel.setImage(imageModel.getImage());
        }

        return CommonReturnType.create(imageModelList);


    }

    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }

        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }

    public static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];

        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
        } catch (DataFormatException e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }


}

