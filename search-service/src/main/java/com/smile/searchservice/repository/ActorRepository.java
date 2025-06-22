package com.smile.searchservice.repository;

import com.smile.searchservice.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ActorRepository extends JpaRepository<Actor, Long> {
    List<Actor> findByName(String name);  // 배우 이름으로 검색
}