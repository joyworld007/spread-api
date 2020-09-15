package com.example.sprinkling.domain.sprinkling.entity;

import com.example.sprinkling.domain.sprinkling.dto.SprinklingDto;
import java.time.LocalDateTime;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "sprinkling")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Sprinkling {

  //뿌리기 아이디
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //룸 아이디
  @Column(name = "room_id")
  private String roomId;

  //토큰
  @Column(name = "token")
  private String token;

  //유저넘버
  @Column(name = "user_no")
  private Long userNo;

  //금액
  @Column(name = "money")
  private Long money;

  //받을사람수
  @Column(name = "count")
  private int count;

  //만료일
  @Column(name = "expire_date")
  private LocalDateTime expireDate;

  //생성일
  @Column(name = "create_date")
  private LocalDateTime createDate;

  //받는 사람 리스트
  @OneToMany(mappedBy = "sprinkling", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  private Collection<Receive> receives;

  protected Sprinkling(SprinklingDto dto) {
    this.id = dto.getId();
    this.roomId = dto.getRoomId();
    this.token = dto.getToken();
    this.userNo = dto.getUserNo();
    this.money = dto.getMoney();
    this.count = dto.getCount();
    this.expireDate = LocalDateTime.now().plusMinutes(10L);
    this.createDate = LocalDateTime.now();
  }

  public static Sprinkling ofDto(SprinklingDto dto) {
    return new Sprinkling(dto);
  }

}
