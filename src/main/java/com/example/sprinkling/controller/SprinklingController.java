package com.example.sprinkling.controller;

import com.example.sprinkling.domain.common.CommonResponseDto;
import com.example.sprinkling.domain.common.CommonResponseEntity;
import com.example.sprinkling.domain.common.Result;
import com.example.sprinkling.domain.common.ResultCode;
import com.example.sprinkling.domain.sprinkling.converter.SprinklingConverter;
import com.example.sprinkling.domain.sprinkling.dto.SprinklingDto;
import com.example.sprinkling.domain.sprinkling.entity.Sprinkling;
import com.example.sprinkling.service.SprinklingService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/sprinkling")
public class SprinklingController {

  private final SprinklingService sprinklingService;

  @PostMapping
  public ResponseEntity createSprinkling(
      @RequestHeader(value = "X-USER-ID") Long userId
      , @RequestHeader(value = "X-ROOM-ID") String roomId
      , @RequestBody SprinklingDto sprinklingDto) throws Exception {

    Sprinkling sprinkling = new SprinklingConverter().convertFromDto(sprinklingDto);
    sprinkling.setUserId(userId);
    sprinkling.setRoomId(roomId);
    sprinklingService.save(sprinkling);
    return CommonResponseEntity.ok(Result.builder().entry(
        SprinklingDto.ofEntity(sprinkling)
    ).build());
  }

  @GetMapping("/{id}")
  public ResponseEntity getSprinklingInfo(
      @RequestHeader(value = "X-USER-ID") Long userId
      , @RequestHeader(value = "X-ROOM-ID") String roomId
      , @PathVariable(value = "id") Long id) throws Exception {
    Optional<Sprinkling> sprinkling = sprinklingService.findbyIdAndUserId(id, userId);
    if (sprinkling.isPresent()) {
      return CommonResponseEntity.ok(Result.builder().entry(
          SprinklingDto.ofEntity(sprinkling.get())
      ).build());
    }
    return CommonResponseEntity.notFound();
  }

  @PutMapping("/{id}")
  public ResponseEntity receiveMoney(
      @RequestHeader(value = "X-USER-ID") Long userId
      , @RequestHeader(value = "X-ROOM-ID") String roomId
      , @PathVariable(value = "id") Long id
      , @RequestParam(value = "token") String token
  ) throws Exception {
    CommonResponseDto<Sprinkling> commonResponseDto =
        sprinklingService.receive(id, roomId, token, userId);
    if(!commonResponseDto.getCode().equals(ResultCode.SUCCESS)) {
      return CommonResponseEntity.fail(commonResponseDto.getCode(), commonResponseDto.getMessage());
    }
    return CommonResponseEntity.ok();
  }

}
