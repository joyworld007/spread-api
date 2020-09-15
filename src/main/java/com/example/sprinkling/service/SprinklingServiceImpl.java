package com.example.sprinkling.service;

import java.util.Arrays;
import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class SprinklingServiceImpl implements SprinklingService {

  //최소값
  final static int percentage[] = {30, 25, 18, 17};
  //추가 가중치 Min/Max
  final static int weight2[] = {10, 20};
  final static int weight3[] = {3, 8};
  final static int weight4[] = {3, 7};
  final static int weight5[] = {2, 3};

  public long[] calculationSprinkling(int count, long money) {
    long result[] = new long[count];
    //퍼센티지 별 금액을 구한다.
    for (int i = 0; i < count; i++) {
      int addWeight = getAddWeight(count);
      result[i] = (money * ((percentage[count - 2] + addWeight))) / 100;
    }
    //오름차순으로 정렬 한뒤
    Arrays.sort(result);
    //남은 금액을 1등에게 몰아 준다
    result[count - 1] = result[count - 1] +
        money - Arrays.stream(result).sum();

    return result;
  }

  //추가 가중치 값을 구한다
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
