package com.example.sprinkling.repository;

import com.example.sprinkling.domain.sprinkling.entity.Sprinkling;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface SprinklingJpaRepository extends JpaRepository<Sprinkling, Long> {

  /**
   * 뿌리기 본인이 단순히 조회 용도
   *
   * @param id
   * @param userId
   * @return
   */
  Optional<Sprinkling> findByIdAndUserIdAndCreateDateBetween(Long id, Long userId,
      LocalDateTime startDate, LocalDateTime endDate);

  /**
   * 뿌리기 금액을 받기 위한 조회 용도 Lock PESSIMISTIC_WRITE
   *
   * @param id
   * @param token
   * @return
   */
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
  Optional<Sprinkling> findByIdAndToken(Long id, String token);
}
