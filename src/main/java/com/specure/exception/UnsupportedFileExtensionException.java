package com.specure.exception;

import com.specure.constant.ErrorMessage;

public class UnsupportedFileExtensionException extends RuntimeException {

    public UnsupportedFileExtensionException(String fileExtension) {
        super(String.format(ErrorMessage.UNSUPPORTED_FILE_EXTENSION, fileExtension));
    }
}
