package com.example.sprinkling;

import com.example.sprinkling.service.SprinklingServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@EnableAutoConfiguration
@ActiveProfiles("local")
public class SprinklingServiceTest {

  @InjectMocks
  private SprinklingServiceImpl sprinklingService;

  @DisplayName("test")
  @Test
  public void test() {
    long result[] = sprinklingService.calculationSprinkling(5, 10000);
    int i = 0;
    for (long value : result) {
      System.out.println(i + " : " + result[i++]);
    }
  }
}
