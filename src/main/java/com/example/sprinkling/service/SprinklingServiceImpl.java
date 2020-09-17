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
  public CommonResponseDto receive(Long id, String roomId, String token, Long userNo) {
    Optional<Sprinkling> sprinkling = sprinklingJpaRepository.findByIdAndToken(id, token);

    //뿌리기 정보를 체크
    if (!sprinkling.isPresent()) {
      return CommonResponseDto.builder().code(ResultCode.NOT_FOUND.toString())
          .message("Not Found").build();
    }

    //유효기간 만료 체크
    if (sprinkling.get().getExpireDate().isBefore(LocalDateTime.now())) {
      return CommonResponseDto.builder().code(ResultCode.EXPIRED.toString())
          .message("EXPIRED").build();
    }

    //룸 아이디 체크
    if (!sprinkling.get().getRoomId().equals(roomId)) {
      return CommonResponseDto.builder().code(ResultCode.ROOM_ID_IS_NOT_INVALID.toString())
          .message("Room Id Invalid").build();
    }

    //모든 뿌리기가 완료 되었는지 체크
    List<Receive> receives = sprinkling.get().getReceives().stream()
        .collect(Collectors.toList());
    if (receives.stream().filter(t -> t.getStatus().equals(SprinklingStatus.READY)).count() == 0) {
      return CommonResponseDto.builder().code(ResultCode.FINISH.toString()).build();
    }

    //이미 지급 된 유저인지 체크
    if (receives.stream()
        .filter(t -> t.getUserNo() != null && t.getUserNo().equals(userNo)).count() > 0) {
      return CommonResponseDto.builder().code(ResultCode.ALREADY_ACCEPTED.toString()).build();
    }

    //랜덤하게 당첨 될 index를 구
    int winIndex = -1;
    while (winIndex < 0) {
      Random rd = new Random();
      int index = rd.nextInt(sprinkling.get().getReceives().size());
      if (!receives.get(index).getStatus().equals(SprinklingStatus.READY)) {
        continue;
      }
      winIndex = index;
    }

    //뿌리기 받기
    receives.get(winIndex).setStatus(SprinklingStatus.COMPLETE);
    receives.get(winIndex).setUserNo(userNo);
    receives.get(winIndex).setReceiveDate(LocalDateTime.now());
    sprinkling.get().getReceives().clear();
    sprinkling.get().getReceives().addAll(receives);

    return CommonResponseDto.builder().code(ResultCode.SUCCESS.toString()).build();
  }

  @Override
  public Optional<Sprinkling> findbyIdAndUserId(Long id, Long userId) {
    return sprinklingJpaRepository.findByIdAndUserId(id, userId);
  }

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
