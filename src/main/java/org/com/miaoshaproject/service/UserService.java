package org.com.miaoshaproject.service;

import org.apache.commons.lang3.StringUtils;
import org.com.miaoshaproject.error.BussinessException;
import org.com.miaoshaproject.service.model.*;

import java.util.List;

public interface UserService {

    UserModel getUserById(Integer id);
    ImageModel getImageById(Integer id);

    void updateData(UserModel userModel) throws BussinessException;
    void register(UserModel userModel) throws BussinessException;
    UserModel validateLogin(String telephone, String password) throws BussinessException;
    UserModel getUserModelBytelephone(String telephone) throws BussinessException;
    //
    void storeImage(ImageModel imageModel) throws BussinessException;
    void storeAudio(AudioModel audioModel) throws BussinessException;
    void storeVideo(VideoModel videoModel) throws BussinessException;
    void storeText(TextModel textModel) throws BussinessException;
    List<ImageModel> listImage(Integer id);
    List<AudioModel> listAudio(Integer id);
    List<VideoModel> listVideo(Integer id);
    List<TextModel> listText(Integer id);

    //
    void storeMultipleImage(List<ImageModel> multipleFile) throws BussinessException;
}
