package com.specure.common.exception;


import com.specure.common.constant.ErrorMessage;

public class WrongNameOfUserExperienceParameterException extends RuntimeException {

    public WrongNameOfUserExperienceParameterException(String name) {
        super(String.format(ErrorMessage.WRONG_NAME_OF_USER_EXPERIENCE_PARAMETER, name));
    }

}
