package com.example.sprinkling.domain.sprinkling.converter;

import com.example.sprinkling.domain.common.BaseConverter;
import com.example.sprinkling.domain.sprinkling.dto.SprinklingDto;
import com.example.sprinkling.domain.sprinkling.entity.Sprinkling;

public class SprinklingConverter extends BaseConverter<SprinklingDto, Sprinkling> {

  public SprinklingConverter() {
    super(Sprinkling::ofDto, SprinklingDto::ofEntity);
  }
}
