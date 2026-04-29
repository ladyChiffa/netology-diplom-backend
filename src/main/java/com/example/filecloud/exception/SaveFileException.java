package com.example.filecloud.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class SaveFileException extends IOException {
    private static final Logger logger = LogManager.getLogger(SaveFileException.class);

    public SaveFileException(String msg) {
        super(msg);
        logger.error(msg);
    }
}
