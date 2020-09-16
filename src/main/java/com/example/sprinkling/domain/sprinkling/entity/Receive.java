package com.example.sprinkling.domain.sprinkling.entity;

import com.example.sprinkling.domain.sprinkling.SprinklingStatus;
import com.example.sprinkling.domain.sprinkling.dto.ReceiveDto;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "receive")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Receive {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "sprinkling_id")
  private Sprinkling sprinkling;

  //받은 금액
  @Column(name = "amount")
  private Long amount;

  //상태
  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private SprinklingStatus status;

  //받은 유저
  @Column(name = "user_no")
  private Long userNo;

  //받은 날자
  @Column(name = "receive_date")
  private LocalDateTime receiveDate;

  protected Receive(ReceiveDto dto) {
    this.id = dto.getId();
    this.amount = dto.getAmount();
    this.status = dto.getStatus();
    this.userNo = dto.getUserId();
    this.receiveDate = dto.getReceiveDate();
  }

  public static Receive ofDto(ReceiveDto dto) {
    return new Receive(dto);
  }

}
