package com.sparta.preonboardingtask.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class ExceptionResponse {
    Integer statusCode;
    String message;
}
