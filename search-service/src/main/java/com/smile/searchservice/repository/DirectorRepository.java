package com.smile.searchservice.repository;

import com.smile.searchservice.entity.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DirectorRepository extends JpaRepository<Director, Long> {
    List<Director> findByName(String name);  // 감독 이름으로 검색
}