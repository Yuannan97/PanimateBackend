package org.com.miaoshaproject.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.Null;
import org.com.miaoshaproject.dao.*;
import org.com.miaoshaproject.dataobject.*;
import org.com.miaoshaproject.error.BussinessException;
import org.com.miaoshaproject.error.EmBusinessError;
import org.com.miaoshaproject.repondse.CommonReturnType;
import org.com.miaoshaproject.service.UserService;
import org.com.miaoshaproject.service.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private userPasswordDOMapper userPdMapper;
    @Autowired
    private userImageDOMapper userImageDOMapper;
    @Autowired
    private UserAudioDOMapper userAudioDOMapper;
    @Autowired
    private UserVideoDOMapper userVideoDOMapper;
    @Autowired
    private UserTextDOMapper userTextDOMapper;

    @Override
    public void updateData(UserModel userModel) throws BussinessException {
        if(userModel==null){
            throw new BussinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        userDOMapper.updateByPrimaryKeySelective(converFromModel(userModel));
    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BussinessException {
        if(userModel==null){
            throw new BussinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //apache-common-lang
        if(StringUtils.isEmpty(userModel.getName()) ||
        userModel.getGender()==null ||
        userModel.getAge()==null ||
        StringUtils.isEmpty(userModel.getTelphone())){
            throw new BussinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        //transfer model into dataobject
        UserDO userDo = converFromModel(userModel);
        //使用insertSelective保证dataobject属性为null时不会覆盖数据库的not null default值
        try{
            userDOMapper.insertSelective(userDo);
        }catch (DuplicateKeyException ex){
            throw new BussinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"This telephone number has been used");
        }
        System.out.println("ID:" + userDo.getId());


        userModel.setId(userDo.getId());
        userPasswordDO userPdDo=converPasswordFromModel(userModel);
        userPdMapper.insertSelective(userPdDo);

        return;
    }

    @Override
    public UserModel validateLogin(String telephone, String password) throws BussinessException {
        //通过用户手机获取数据库中的用户信息 get user info from database
        UserDO userDO = userDOMapper.selectByTelephone(telephone);
        if(userDO ==null){
            throw new BussinessException(EmBusinessError.USER_LOGIN_FAILED);
        }
        userPasswordDO userPd= userPdMapper.selectByUserId(userDO.getId());
        UserModel userModel = convertFromDataObject(userDO,userPd);
        //校验密码 match the password
        if(!StringUtils.equals(password,userModel.getEncrptPassword())){
            throw new BussinessException(EmBusinessError.USER_LOGIN_FAILED);
        }else{
            System.out.println("Login successful");
            return userModel;
        }
    }

    @Override
    public UserModel getUserModelBytelephone(String telephone) throws BussinessException {
        UserDO userDO = userDOMapper.selectByTelephone(telephone);
        if(userDO == null)
        {
            System.out.println("null userDo to get");
            return null;
        }
        userPasswordDO userpd = userPdMapper.selectByUserId(userDO.getId());
        return convertFromDataObject(userDO,userpd);
    }

    @Override
    @Transactional
    public void storeMultipleImage(List<ImageModel> multipleFile) throws BussinessException {
        if (multipleFile==null){
            throw new BussinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }


    }

    @Override
    @Transactional
    public void storeAudio(AudioModel audioModel) throws BussinessException {
        if(audioModel==null){
            throw new BussinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        if (audioModel.getAudio()==null ||
                audioModel.getId()==null ||
                audioModel.getName()==null ||
                audioModel.getType()==null ||
                audioModel.getTime()==null){
            throw new BussinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        //transfer model into dataobject
        UserAudioDO userAudioDO = convertFromAudioModel(audioModel);
        //使用insertSelective保证dataobject属性为null时不会覆盖数据库的not null default值
        userAudioDOMapper.insertSelective(userAudioDO);

        return;
    }

    @Override
    @Transactional
    public void storeVideo(VideoModel videoModel) throws BussinessException {
        if(videoModel==null){
            throw new BussinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        if (videoModel.getVideo()==null ||
                videoModel.getId()==null ||
                videoModel.getName()==null ||
                videoModel.getType()==null ||
                videoModel.getTime()==null){
            throw new BussinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        //transfer model into dataobject
        UserVideoDO userVideoDO = convertFromVideoModel(videoModel);
        //使用insertSelective保证dataobject属性为null时不会覆盖数据库的not null default值
        userVideoDOMapper.insertSelective(userVideoDO);

        return;
    }

    @Override
    public void storeText(TextModel textModel) throws BussinessException {
        // 使用insertselective
        if(textModel == null)
        {
            throw new BussinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        if(textModel.getId() == null ||
                textModel.getName() == null||
                textModel.getTime()==null ||
                textModel.getType() == null ||
                textModel.getText() == null||
                textModel.getTextId() ==null)
        {
            throw new BussinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        UserTextDO userTextDO = convertFromTextModel(textModel);
        userTextDOMapper.insertSelective(userTextDO);
    }

    private UserTextDO convertFromTextModel(TextModel textModel) {
        if(textModel == null)
        {
            System.out.println("Null USERTEXTDO");
            return null;
        }
        UserTextDO userTextDO = new UserTextDO();
        BeanUtils.copyProperties(textModel,userTextDO);
        return userTextDO;
    }


    @Override
    @Transactional
    public void storeImage(ImageModel imageModel) throws BussinessException {
        if(imageModel==null){
            throw new BussinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        if (imageModel.getImage()==null ||
                imageModel.getId()==null ||
                imageModel.getName()==null ||
                imageModel.getType()==null){
            throw new BussinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        //transfer model into dataobject
        userImageDO userImageDO = convertFromImageModel(imageModel);
        //使用insertSelective保证dataobject属性为null时不会覆盖数据库的not null default值
        userImageDOMapper.insertSelective(userImageDO);

        return;
    }

    @Override
    public UserModel getUserById(Integer id) {
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if(userDO==null){
            System.out.println("without user information");
            return null;
        }
        userPasswordDO userPd= userPdMapper.selectByUserId(userDO.getId());
        return convertFromDataObject(userDO,userPd);
    }

    @Override
    public ImageModel getImageById(Integer id) {
        userImageDO imageModel = userImageDOMapper.selectByPrimaryKey(id);

        if (imageModel==null) {
            return null;
        }

        return convertFromImageObject(imageModel);
    }

    @Override
    public List<ImageModel> listImage(Integer id) {
        List<userImageDO> imageDOList = userImageDOMapper.listImage(id);

//        for (userImageDO userImageDO : imageDOList)
//            System.out.println(userImageDO.getName());

        List<ImageModel> imageModelList = imageDOList.stream().map(imageDo -> {
            ImageModel imageModel = this.convertFromImageObject(imageDo);
            return imageModel;
        }).collect(Collectors.toList());

        return imageModelList;
    }

    @Override
    public List<AudioModel> listAudio(Integer id) {
        List<UserAudioDO> audioDOList = userAudioDOMapper.listAudio(id);
        List<AudioModel> audioModelList = audioDOList.stream().map(audioDo -> {
            AudioModel audioModel = this.convertFromAudioObject(audioDo);
            return audioModel;
        }).collect(Collectors.toList());
        return audioModelList;
    }

    @Override
    public List<VideoModel> listVideo(Integer id) {
        List<UserVideoDO> videoDOList = userVideoDOMapper.listVideo(id);

        //java stream to get data stream of the List provide function programming -> like a pipe
        //stream like iterator just once from left to right can't come back,
        //stream doesn't save data
        //stream operation delay
        //stream.map return a stream consisting of the results of applying the given function to the element of this stream
        //collect() is to collect the stream in the way of collector.toList()

        List<VideoModel> videoModelList = videoDOList.stream().map(videoDo -> {
            VideoModel videoModel = this.convertFromVideoObject(videoDo);
            return videoModel;
        }).collect(Collectors.toList());
        return videoModelList;
    }

    @Override
    public List<TextModel> listText(Integer id) {
        List<UserTextDO> list = userTextDOMapper.listText(id);

        List<TextModel> TextModelList = list.stream().map(textDo->{
            TextModel textModel = convertFromTextObject(textDo);
            return textModel;
        }).collect(Collectors.toList());
       return TextModelList;
    }

    private TextModel convertFromTextObject(UserTextDO textDo) {
        if(textDo == null)
        {
            System.out.println("null text");
            return null;
        }
        TextModel textModel = new TextModel();
        BeanUtils.copyProperties(textDo,textModel);
        return textModel;
    }

    private UserVideoDO convertFromVideoModel(VideoModel videoModel) {
        if(videoModel == null)
        {
            System.out.println("null video");
            return null;
        }
        UserVideoDO userVideoDO = new UserVideoDO();
        BeanUtils.copyProperties(videoModel,userVideoDO);
        return userVideoDO;
    }

    private VideoModel convertFromVideoObject(UserVideoDO videoDo) {
        if (videoDo==null) {
            return null;
        }

        VideoModel videoModel = new VideoModel();
        BeanUtils.copyProperties(videoDo,videoModel);

        return videoModel;
    }


    //convert from model object to data object
    public UserDO converFromModel(UserModel userModel){
        if(userModel==null){
            return null;
        }
        UserDO userDo = new UserDO();
        BeanUtils.copyProperties(userModel,userDo);

        return userDo;
    }

    public userPasswordDO converPasswordFromModel(UserModel userModel){
        if(userModel==null){
            return null;
        }
        userPasswordDO userPdDo =new  userPasswordDO();
        userPdDo.setEncrptPassword(userModel.getEncrptPassword());
        userPdDo.setUserId(userModel.getId());
        return userPdDo;
    }

    //convert from data object to model object
    public UserModel convertFromDataObject(UserDO userDO, userPasswordDO userPd){
        if(userDO==null){
            return null;
        }

        UserModel userModel=new UserModel();
        BeanUtils.copyProperties(userDO,userModel);
        if(userPd!=null){
            userModel.setEncrptPassword(userPd.getEncrptPassword());
        }
        return userModel;
    }

    public ImageModel convertFromImageObject(userImageDO userImageDO){
        if (userImageDO==null) {
            return null;
        }

        ImageModel imageModel=new ImageModel();
        BeanUtils.copyProperties(userImageDO,imageModel);

        return imageModel;
    }

    public userImageDO convertFromImageModel(ImageModel imageModel){
        if (imageModel==null) {
            return null;
        }

        userImageDO userImageDO=new userImageDO();
        BeanUtils.copyProperties(imageModel,userImageDO);

        return userImageDO;
    }

    public AudioModel convertFromAudioObject(UserAudioDO userAudioDO){
        if (userAudioDO==null) {
            return null;
        }

        AudioModel audioModel=new AudioModel();
        BeanUtils.copyProperties(userAudioDO,audioModel);

        return audioModel;
    }

    public UserAudioDO convertFromAudioModel(AudioModel audioModel){
        if (audioModel==null) {
            return null;
        }
        UserAudioDO userAudioDO = new UserAudioDO();
        BeanUtils.copyProperties(audioModel,userAudioDO);

        return userAudioDO;
    }

}
