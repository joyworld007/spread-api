package com.example.sprinkling.domain.common;

import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SuppressWarnings("unchecked")
public class CommonResponseEntity {

  public static ResponseEntity ok() {
    CommonResponseDto commonResponseDto = CommonResponseDto.builder()
        .code("SUCCESS")
        .message("OK")
        .build();
    return new ResponseEntity(commonResponseDto, HttpStatus.OK);
  }

  public static ResponseEntity ok(Result result) {
    CommonResponseDto<Result<?>> commonResponseDto = CommonResponseDto.builder()
        .result(result)
        .code("SUCCESS")
        .message("OK")
        .build();
    return new ResponseEntity(commonResponseDto, HttpStatus.OK);
  }

  public static ResponseEntity fail(String code, String message) {
    CommonResponseDto commonResponseDto = CommonResponseDto.builder()
        .code(code)
        .message(message)
        .build();
    return new ResponseEntity(commonResponseDto, HttpStatus.OK);
  }

  public static ResponseEntity created(String location) {
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.setLocation(URI.create(location));
    return new ResponseEntity(responseHeaders, HttpStatus.CREATED);
  }

  public static ResponseEntity created() {
    return new ResponseEntity(HttpStatus.CREATED);
  }

  public static ResponseEntity badRequest(String code, String message) {
    CommonResponseDto commonResponseDto = CommonResponseDto.builder()
        .code(code)
        .message(message)
        .build();
    return new ResponseEntity(commonResponseDto, HttpStatus.BAD_REQUEST);
  }

  public static ResponseEntity badRequest() {
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  public static ResponseEntity notFound() {
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

}
