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
import org.springframework.data.redis.core.RedisHash;

@RedisHash("sprinkling")
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
  private Long userNo;
  private Long money;
  private int count;
  private LocalDateTime expireDate;
  private LocalDateTime createDate;
  private Collection<ReceiveDto> receivesDto;

  protected SprinklingDto(Sprinkling entity) {
    receivesDto = entity.getReceives().stream().map(s -> ReceiveDto.ofEntity(s))
        .collect(Collectors.toList());
  }
}
