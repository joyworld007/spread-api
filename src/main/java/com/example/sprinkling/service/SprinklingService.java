package com.example.sprinkling.service;

import com.example.sprinkling.domain.sprinkling.entity.Sprinkling;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public interface SprinklingService {

  long[] calculationSprinkling(int count, long money);

  @Transactional
  public Sprinkling save(Sprinkling sprinkling);

}
