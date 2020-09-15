package com.example.sprinkling;

import com.example.sprinkling.service.SprinklingServiceImpl;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
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

  @DisplayName("random money sum test")
  @Test
  public void readom_money_sum_test() {
    int money = 10000;
    long result[];
    result = sprinklingService.calculationSprinkling(2, money);
    Assertions.assertEquals(money, Arrays.stream(result).sum());
    result = sprinklingService.calculationSprinkling(3, money);
    Assertions.assertEquals(money, Arrays.stream(result).sum());
    result = sprinklingService.calculationSprinkling(4, money);
    Assertions.assertEquals(money, Arrays.stream(result).sum());
    result = sprinklingService.calculationSprinkling(5, money);
    Assertions.assertEquals(money, Arrays.stream(result).sum());
  }
}
