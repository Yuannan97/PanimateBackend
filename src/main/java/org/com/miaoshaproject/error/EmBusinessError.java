package org.com.miaoshaproject.error;

public enum EmBusinessError implements CommonError{
    //通用参数问题，可以改变msg指明问题
    PARAMETER_VALIDATION_ERROR(10001,"Parameter error"),
    UNKNOWN_ERROR(10002,"Unknown error"),
    //20000开头为用户信息相关错误定义
    USER_NOT_EXIST(20001,"user not exist"),
    USER_LOGIN_FAILED(20003,"user telephone or password can not be found"),
    IMAGE_NOT_EXIST(20002,"image not exist"),
    TEXT_NOT_EXIST(20004,"Text not exist")
    ;

    private EmBusinessError(int errCode, String errMsg){
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    private int errCode;
    private String errMsg;

    @Override
    public int getErrCode() {
        return errCode;
    }

    @Override
    public String getErrMsg() {
        return errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg=errMsg;
        return this;
    }
}
