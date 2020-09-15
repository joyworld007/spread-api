package com.example.sprinkling.repository;

import com.example.sprinkling.domain.sprinkling.entity.Sprinkling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SprinklingJpaRepository extends JpaRepository<Sprinkling, Long> {

}
