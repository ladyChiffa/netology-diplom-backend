package com.example.filecloud.exception;

import java.io.IOException;

public class DeleteFileException extends IOException {
    public DeleteFileException(String msg) {
        super(msg);
    }
}
