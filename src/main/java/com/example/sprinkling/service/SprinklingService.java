package com.example.sprinkling.service;

import com.example.sprinkling.domain.common.CommonResponseDto;
import com.example.sprinkling.domain.common.Result;
import com.example.sprinkling.domain.sprinkling.entity.Sprinkling;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public interface SprinklingService {

  long[] createSprinkling(int count, long money);

  @Transactional
  public Sprinkling save(Sprinkling sprinkling);

  @Transactional
  public CommonResponseDto<Sprinkling> receive(Long id, String roomId, String token, Long userNo);

  public Optional<Sprinkling> findbyIdAndUserId(Long id, Long userId);

  public Optional<Sprinkling> findbyIdAndToken(Long id, String token);

  public String createToken();

}
