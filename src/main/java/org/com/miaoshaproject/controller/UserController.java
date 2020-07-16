package org.com.miaoshaproject.controller;

import org.apache.commons.lang3.StringUtils;
import org.com.miaoshaproject.controller.viewobject.UserVO;
import org.com.miaoshaproject.dataobject.UserDO;
import org.com.miaoshaproject.error.BussinessException;
import org.com.miaoshaproject.error.CommonError;
import org.com.miaoshaproject.error.EmBusinessError;
import org.com.miaoshaproject.repondse.CommonReturnType;
import org.com.miaoshaproject.service.UserService;
import org.com.miaoshaproject.service.model.ImageModel;
import org.com.miaoshaproject.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Controller("user")
@RequestMapping("/user")
@CrossOrigin(value="*",maxAge=3600,allowedHeaders="*")

//public class UserController extends BaseController {
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;


    //用户登陆接口 USER LOGIN INTERFACE
    @RequestMapping(value = "/login", method={RequestMethod.POST})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "telephone")String telephone,
                                  @RequestParam(name = "password")String password) throws BussinessException {
        System.out.println("telephone:" + telephone + "  " + "password: " + password);
        //入参较验
        if(StringUtils.isEmpty(telephone)||StringUtils.isEmpty(password)){
            throw new BussinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "empty telephone or password");
        }

        //校验用户登录合法
        UserModel userModel=userService.validateLogin(telephone,password);

        //无异常抛出，将登录凭证加入到用户登录到session中
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);
        UserModel usermodel= userService.getUserModelBytelephone(telephone);
//        int userID = userModel.getId();
        String uName = userModel.getName();
        return CommonReturnType.create(uName);
    }



    //用户获取OTP短信接口
    @RequestMapping(value = "/getotp", method={RequestMethod.POST})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name = "telephone") String telephone){
        //generate the opt code based on some rules
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt+=10000;
        String optCode = String.valueOf(randomInt);


        //make opt code corresponding to user's cell phone (TODO: redis)
        //for now, we just begin with httpsession to connect telephone with otpcode
        httpServletRequest.getSession().setAttribute(telephone,optCode);



        //message opt to user (TODO)

        System.out.println("telephone = "+telephone+" & OtpCode = "+optCode);

        return CommonReturnType.create(null);
    }


    //registeration interface
    @RequestMapping(value = "/register", method={RequestMethod.POST})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name = "telephone")String telphone,
                                     @RequestParam(name = "password") String password,
    @RequestParam(name = "name") String name)throws BussinessException {


//        @RequestParam(name = "otpCode") String otpCode,
//        @RequestParam(name = "name") String name,
//        @RequestParam(name = "gender") Byte gender,
//        @RequestParam(name = "age") Integer age,
        System.out.println("telephone:" + telphone + "  " + "password: " + password + " "+ "username" + name);
        String otpCode="a";
//        String name ="SAM";
        Byte gender=0;
        Integer age=0;

        if (!BaseController.checkValidChar(name)) {
            return CommonReturnType.create(null,"invalid name: " + name);
        }

        if (!BaseController.checkValidChar(telphone)) {
            return CommonReturnType.create(null,"invalid telephone: " + telphone);
        }

        //make sure telphone is corresponding to otp code
        String inSessionOtpCode = (String)this.httpServletRequest.getSession().getAttribute("telphone");
        //内置判空处理
        if(!com.alibaba.druid.util.StringUtils.equals(otpCode,inSessionOtpCode)){
            //throw new BussinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"optcode does not match");
        }
        //begin to register

        UserModel userModel = new UserModel();
//        userModel.setId(1);
        userModel.setName(name);
        userModel.setGender(gender);
        userModel.setAge(age);
        userModel.setTelphone(telphone);
        userModel.setRegisterMode("byphone");
        //TODO: encode method
        userModel.setEncrptPassword(password);

        userService.register(userModel);
        return CommonReturnType.create(userModel);
    }

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BussinessException {
        //调用service层获取对应id对象并返回给前端
        UserModel userModel = userService.getUserById(id);

        //if user info error
        if(userModel ==null){

            //userModel.setEncrptPassword("123");
            throw new BussinessException(EmBusinessError.USER_NOT_EXIST);
        }

        //convert core model user object to viewobject to be uesd in UI
        UserVO userVO= convertFromModel(userModel);

        return CommonReturnType.create(userVO);

    }

    private UserVO convertFromModel(UserModel userModel){
        if(userModel==null){
            return null;
        }
        UserVO userVO= new UserVO();
        BeanUtils.copyProperties(userModel,userVO);
        return userVO;
    }

    @RequestMapping("/getHello")
    @ResponseBody
    public String getHello(){
        return "helloworld";
    }

    @RequestMapping(value = "/getTest" , method={RequestMethod.POST})
    @ResponseBody
    public String getTest(){
        System.out.println("successful");
        return "getText";

    }


}
