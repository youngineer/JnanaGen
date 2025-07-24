package com.youngineer.backend.utils;

import com.youngineer.backend.dto.responses.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static ResponseEntity<ResponseDto> success(String message, Object content) {
        ResponseDto response = new ResponseDto(message, content);
        return ResponseEntity.ok(response);
    }

    public static ResponseEntity<ResponseDto> badRequest(String message) {
        ResponseDto response = new ResponseDto(message, null);
        return ResponseEntity.badRequest().body(response);
    }

    public static ResponseEntity<ResponseDto> notFound(String message) {
        ResponseDto response = new ResponseDto(message, null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    public static ResponseEntity<ResponseDto> internalServerError(String message) {
        ResponseDto response = new ResponseDto(message, null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    public static ResponseEntity<ResponseDto> unprocessableEntity(String message) {
        ResponseDto response = new ResponseDto(message, null);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }
}

