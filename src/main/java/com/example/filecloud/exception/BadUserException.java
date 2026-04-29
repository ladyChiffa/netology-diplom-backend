package com.example.filecloud.exception;

import com.example.filecloud.controller.AuthorizationController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BadUserException extends RuntimeException {
    private static final Logger logger = LogManager.getLogger(BadUserException.class);

    public BadUserException(String msg) {
        super(msg);
        logger.error(msg);
    }
}
