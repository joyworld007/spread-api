package com.example.sprinkling.service;

import com.example.sprinkling.domain.common.CommonResponseDto;
import com.example.sprinkling.domain.common.ResultCode;
import com.example.sprinkling.domain.sprinkling.SprinklingStatus;
import com.example.sprinkling.domain.sprinkling.dto.ReceiveDto;
import com.example.sprinkling.domain.sprinkling.entity.Receive;
import com.example.sprinkling.domain.sprinkling.entity.Sprinkling;
import com.example.sprinkling.repository.SprinklingJpaRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class SprinklingServiceImpl implements SprinklingService {

  private final SprinklingJpaRepository sprinklingJpaRepository;

  //최소값
  final static int percentage[] = {30, 25, 18, 17};
  //추가 가중치 Min/Max
  final static int weight2[] = {10, 20};
  final static int weight3[] = {3, 8};
  final static int weight4[] = {3, 7};
  final static int weight5[] = {2, 3};

  public long[] createSprinkling(int count, long money) {
    long result[] = new long[count];
    //퍼센티지 별 금액을 구한다.
    for (int i = 0; i < count; i++) {
      int addWeight = getAddWeight(count);
      result[i] = (money * ((percentage[count - 2] + addWeight))) / 100;
    }
    //오름차순으로 정렬
    Arrays.sort(result);
    //남은 금액을 1등에게 할당
    result[count - 1] = result[count - 1] +
        money - Arrays.stream(result).sum();

    return result;
  }

  @Override
  @Transactional
  public Sprinkling save(Sprinkling sprinkling) {
    long[] receive = createSprinkling(sprinkling.getCount(),
        sprinkling.getMoney());
    sprinkling.setToken(createToken());
    Collection<Receive> receives = new ArrayList<>();
    Arrays.stream(receive)
        .forEach(s ->
            receives.add(Receive.ofDto(ReceiveDto.builder()
                .amount(s).status(SprinklingStatus.READY).build())));
    for (Receive r : receives) {
      r.setSprinkling(sprinkling);
    }
    sprinkling.setReceives(receives);
    sprinkling.setMax(receive[receive.length - 1]);
    return sprinklingJpaRepository.save(sprinkling);
  }

  @Override
  @Transactional
  public CommonResponseDto receive(Long id, String roomId, String token, Long userId) {
    Optional<Sprinkling> sprinkling = sprinklingJpaRepository.findByIdAndToken(id, token);

    //뿌리기 정보를 체크
    if (!sprinkling.isPresent()) {
      return CommonResponseDto.builder().code(ResultCode.NOT_FOUND.toString())
          .message("Not found").build();
    }

    //유효기간 만료 체크
    if (sprinkling.get().getExpireDate().isBefore(LocalDateTime.now())) {
      return CommonResponseDto.builder().code(ResultCode.EXPIRED.toString())
          .message("Expired").build();
    }

    //룸 아이디 체크
    if (!sprinkling.get().getRoomId().equals(roomId)) {
      return CommonResponseDto.builder().code(ResultCode.ROOM_ID_IS_NOT_INVALID.toString())
          .message("Room id is not Invalid").build();
    }

    //모든 뿌리기가 완료 되었는지 체크
    List<Receive> receives = sprinkling.get().getReceives().stream()
        .collect(Collectors.toList());
    if (receives.stream().filter(t -> t.getStatus().equals(SprinklingStatus.READY)).count() == 0) {
      return CommonResponseDto.builder().code(ResultCode.FINISHED.toString())
          .message("sprinkling is finished").build();
    }

    //이미 지급 된 유저인지 체크
    if (receives.stream()
        .filter(t -> t.getUserId() != null && t.getUserId().equals(userId)).count() > 0) {
      return CommonResponseDto.builder().code(ResultCode.ALREADY_ACCEPTED.toString())
          .message("Already accepted")
          .build();
    }

    //랜덤하게 당첨 될 index를 구함
    int winIndex = -1;
    while (winIndex < 0) {
      Random rd = new Random();
      int index = rd.nextInt(sprinkling.get().getReceives().size());
      if (!receives.get(index).getStatus().equals(SprinklingStatus.READY)) {
        continue;
      }
      winIndex = index;
    }

    //뿌리기 금액 받기
    receives.get(winIndex).setStatus(SprinklingStatus.COMPLETE);
    receives.get(winIndex).setUserId(userId);
    receives.get(winIndex).setReceiveDate(LocalDateTime.now());
    sprinkling.get().getReceives().clear();
    sprinkling.get().getReceives().addAll(receives);

    return CommonResponseDto.builder().code(ResultCode.SUCCESS.toString()).message("OK").build();
  }

  /**
   * 뿌리기 데이터 조회
   * 자신이 생성한 뿌리기와 7일이내 생성된 뿌리기만 조회 가능
   * @param id
   * @param userId
   * @return
   */
  @Override
  public Optional<Sprinkling> findbyIdAndUserId(Long id, Long userId) {
    return sprinklingJpaRepository.findByIdAndUserIdAndCreateDateBetween(id, userId,
        LocalDateTime.now().minusDays(7), LocalDateTime.now());
  }

  /**
   * 뿌리기 금액 받기를 위한 조회
   * 분산 환경과 동시성을 고려한 Lock 기법 사용 (PESSIMISTIC_WRITE)
   * @param id
   * @param token
   * @return
   */
  @Override
  public Optional<Sprinkling> findbyIdAndToken(Long id, String token) {
    return sprinklingJpaRepository.findByIdAndToken(id, token);
  }

  @Override
  public String createToken() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 3; i++) {
      Random rand = new Random();
      char uc = (char) (rand.nextInt(26) + 'A');
      sb.append(uc);
    }
    return sb.toString();
  }

  public int getAddWeight(int count) {
    Random rd = new Random();
    switch (count) {
      case 3:
        return rd.nextInt(weight3[0]) + (weight3[1] - weight3[0]);
      case 4:
        return rd.nextInt(weight4[0]) + (weight4[1] - weight4[0]);
      case 5:
        return rd.nextInt(weight5[0]) + (weight5[1] - weight5[0]);
      default:
        return rd.nextInt(weight2[0]) + (weight2[1] - weight2[0]);
    }
  }

}
