package com.specure.common.exception;


import com.specure.common.constant.ErrorMessage;

public class PackageTypeException extends RuntimeException {

    public PackageTypeException(String packageTypeName) {
        super(String.format(ErrorMessage.BAD_PACKAGE_TYPE_NAME, packageTypeName));
    }
}
