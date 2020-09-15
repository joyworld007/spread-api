package com.example.sprinkling.controller;

import com.example.sprinkling.domain.common.CommonResponseEntity;
import com.example.sprinkling.domain.sprinkling.dto.SprinklingDto;
import com.example.sprinkling.service.SprinklingService;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/sprinkling")
public class SprinklingController {

  private final SprinklingService sprinklingService;

  @PostMapping
  public ResponseEntity create(@RequestBody SprinklingDto sprinklingDto)
      throws Exception {
    //sprinklingService.save(sprinklingDto);
    return CommonResponseEntity.created();
  }

}
