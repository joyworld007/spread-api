package com.example.sprinkling.domain.sprinkling.dto;

import com.example.sprinkling.domain.sprinkling.entity.Sprinkling;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SprinklingDto {

  @Id
  private Long id;

  private String roomId;
  private String token;
  private Long userId;
  private Long money;
  private Long max;
  private int count;
  private LocalDateTime expireDate;
  private LocalDateTime createDate;
  private Collection<ReceiveDto> receives;

  protected SprinklingDto(Sprinkling entity) {
    this.id = entity.getId();
    this.roomId = entity.getRoomId();
    this.token = entity.getToken();
    this.userId = entity.getUserId();
    this.money = entity.getMoney();
    this.max = entity.getMax();
    this.count = entity.getCount();
    this.expireDate = LocalDateTime.now().plusMinutes(10L);
    this.createDate = LocalDateTime.now();
    receives = entity.getReceives().stream().map(s -> ReceiveDto.ofEntity(s))
        .collect(Collectors.toList());
  }

  public static SprinklingDto ofEntity(Sprinkling entity) {
    return new SprinklingDto(entity);
  }
}
