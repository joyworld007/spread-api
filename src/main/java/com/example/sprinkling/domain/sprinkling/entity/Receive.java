package com.example.sprinkling.domain.sprinkling.entity;

import com.example.sprinkling.domain.sprinkling.SprinklingStatus;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "receive")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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
  private SprinklingStatus status;

  //받은 유저
  @Column(name = "user_id")
  private long receiveUserId;

  //받은 날자
  @Column(name = "receive_Date")
  private LocalDateTime receiveDate;



}
