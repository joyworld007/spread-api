package com.example.sprinkling.service;

import org.springframework.stereotype.Service;

@Service
public interface SprinklingService {

  long[] calculationSprinkling(int count, long money);
}
