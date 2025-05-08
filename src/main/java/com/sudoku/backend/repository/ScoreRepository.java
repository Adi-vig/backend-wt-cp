package com.sudoku.backend.repository;

import com.sudoku.backend.model.Score;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    List<Score> findByEmail(String email);
}
