package com.example.sprinkling.domain.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(Include.NON_NULL)
public class CommonResponseDto<T> {

  private Result<T> result;
  private String code;
  private String message;
}
