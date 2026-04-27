package com.example.filecloud.exception;

import java.io.IOException;

public class SaveFileException extends IOException {
    public SaveFileException(String msg) {
        super(msg);
    }
}
