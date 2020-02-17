package com.crawler.exception;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class ErrorResponse {

    private final int status;
    private final String message;
    private final LocalDateTime timeStamp;
}
