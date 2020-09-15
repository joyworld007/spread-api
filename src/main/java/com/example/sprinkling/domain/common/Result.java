package com.example.sprinkling.domain.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonIgnoreProperties
public class Result<T> {

  private T entry;
}
