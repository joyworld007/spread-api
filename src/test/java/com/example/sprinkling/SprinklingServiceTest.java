package com.example.sprinkling;

import static org.mockito.Mockito.when;

import com.example.sprinkling.domain.common.CommonResponseDto;
import com.example.sprinkling.domain.sprinkling.SprinklingStatus;
import com.example.sprinkling.domain.sprinkling.dto.ReceiveDto;
import com.example.sprinkling.domain.sprinkling.dto.SprinklingDto;
import com.example.sprinkling.domain.sprinkling.entity.Receive;
import com.example.sprinkling.domain.sprinkling.entity.Sprinkling;
import com.example.sprinkling.repository.SprinklingJpaRepository;
import com.example.sprinkling.service.SprinklingServiceImpl;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@ExtendWith(MockitoExtension.class)
@EnableAutoConfiguration
public class SprinklingServiceTest {

  @InjectMocks
  private SprinklingServiceImpl sprinklingService;

  @Mock
  private SprinklingJpaRepository sprinklingJpaRepository;

  private Sprinkling sprinkling;

  @BeforeEach
  public void setup(TestInfo testInfo) {
    rawSetup(testInfo);
  }

  private void rawSetup(TestInfo testInfo) {
    ReceiveDto receiveDto1 = ReceiveDto.builder()
        .id(1L)
        .receiveDate(LocalDateTime.now())
        .amount(3000L)
        .status(SprinklingStatus.READY)
        .build();

    ReceiveDto receiveDto2 = ReceiveDto.builder()
        .id(2L)
        .receiveDate(LocalDateTime.now())
        .amount(3000L)
        .status(SprinklingStatus.READY)
        .build();

    ReceiveDto receiveDto3 = ReceiveDto.builder()
        .id(3L)
        .receiveDate(LocalDateTime.now())
        .amount(4000L)
        .status(SprinklingStatus.READY)
        .build();

    ArrayList<ReceiveDto> receiveDtos = new ArrayList<>();
    receiveDtos.add(receiveDto1);
    receiveDtos.add(receiveDto2);
    receiveDtos.add(receiveDto3);

    SprinklingDto sprinklingDto = SprinklingDto.builder()
        .id(1L)
        .count(3)
        .money(10000L)
        .max(4000L)
        .createDate(LocalDateTime.now())
        .roomId("ROOM_10000000001")
        .token("ABC")
        .expireDate(LocalDateTime.now().plusMinutes(10L))
        .userId(1000000000L)
        .build();

    sprinkling = Sprinkling.ofDto(sprinklingDto);
    Collection<Receive> receives = receiveDtos.stream()
        .map(t -> Receive.ofDto(t))
        .collect(Collectors.toList());
    sprinkling.setReceives(receives);
  }

  @DisplayName("setSprinkling is ok test")
  @Test
  public void setSprinkling_is_ok_test() {
    when(sprinklingJpaRepository.findByIdAndToken(1L, "ABC"))
        .thenReturn(Optional.of(sprinkling));
    CommonResponseDto commonResponseDto = sprinklingService.receive(1L,
        "ROOM_10000000001", "ABC", 1000000001L);
    Assertions.assertEquals(commonResponseDto.getCode(), "SUCCESS");
  }

  @DisplayName("setSprinkling already accepted test")
  @Test
  public void setSprinkling_already_accepted_test() {
    sprinkling.getReceives().forEach(
        t -> {
          if (t.getId() == 1L) {
            t.setStatus(SprinklingStatus.COMPLETE);
            t.setUserId(1000000001L);
          }
        }
    );
    when(sprinklingJpaRepository.findByIdAndToken(1L, "ABC"))
        .thenReturn(Optional.of(sprinkling));
    CommonResponseDto commonResponseDto = sprinklingService.receive(1L,
        "ROOM_10000000001", "ABC", 1000000001L);
    Assertions.assertEquals(commonResponseDto.getCode(), "ALREADY_ACCEPTED");
  }

  @DisplayName("setSprinkling finished test")
  @Test
  public void setSprinkling_finished_test() {
    sprinkling.getReceives().forEach(
        t -> t.setStatus(SprinklingStatus.COMPLETE)
    );
    when(sprinklingJpaRepository.findByIdAndToken(1L, "ABC")).thenReturn(
        Optional.of(sprinkling));
    CommonResponseDto commonResponseDto = sprinklingService.receive(1L,
        "ROOM_10000000001", "ABC", 1000000001L);
    Assertions.assertEquals(commonResponseDto.getCode(), "FINISHED");
  }

  @DisplayName("room id is not valid test")
  @Test
  public void room_id_is_not_valid_test() {
    sprinkling.setRoomId("ROOM_10000000000");
    when(sprinklingJpaRepository.findByIdAndToken(1L, "ABC")).thenReturn(
        Optional.of(sprinkling));
    CommonResponseDto commonResponseDto = sprinklingService.receive(1L,
        "ROOM_10000000001", "ABC", 1000000001L);
    Assertions.assertEquals(commonResponseDto.getCode(), "ROOM_ID_IS_NOT_INVALID");
  }

  @DisplayName("setSprinkling expired test")
  @Test
  public void setSprinkling_expired_test() {
    sprinkling.setExpireDate(LocalDateTime.now().minusMinutes(10L));
    when(sprinklingJpaRepository.findByIdAndToken(1L, "ABC")).thenReturn(
        Optional.of(sprinkling));
    CommonResponseDto commonResponseDto = sprinklingService.receive(1L,
        "ROOM_10000000001", "ABC", 1000000001L);
    Assertions.assertEquals(commonResponseDto.getCode(), "EXPIRED");
  }

  @DisplayName("setSprinkling not found test")
  @Test
  public void setSprinkling_not_found_test() {
    when(sprinklingJpaRepository.findByIdAndToken(1L, "ABC")).thenReturn(Optional.empty());
    CommonResponseDto commonResponseDto = sprinklingService.receive(1L,
        "ROOM_10000000001", "ABC", 1000000001L);
    Assertions.assertEquals(commonResponseDto.getCode(), "NOT_FOUND");
  }

  @DisplayName("random money sum test")
  @Test
  public void random_money_sum_test() {
    int money = 10000;
    long result[];
    result = sprinklingService.createSprinkling(2, money);
    Assertions.assertEquals(money, Arrays.stream(result).sum());
    result = sprinklingService.createSprinkling(3, money);
    Assertions.assertEquals(money, Arrays.stream(result).sum());
    result = sprinklingService.createSprinkling(4, money);
    Assertions.assertEquals(money, Arrays.stream(result).sum());
    result = sprinklingService.createSprinkling(5, money);
    Assertions.assertEquals(money, Arrays.stream(result).sum());
  }
  
}
