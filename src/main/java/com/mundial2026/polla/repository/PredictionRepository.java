package com.mundial2026.polla.repository;

import com.mundial2026.polla.model.Prediction;
import com.mundial2026.polla.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Long> {
    List<Prediction> findByUser(User user);
    List<Prediction> findByMatchId(Long matchId);
    Optional<Prediction> findByUserIdAndMatchId(Long userId, Long matchId);

    @Query("SELECT p FROM Prediction p WHERE p.user.id = :userId")
    List<Prediction> findByUserId(@Param("userId") Long userId);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("DELETE FROM Prediction p WHERE p.user.id = :userId")
    void deleteByUserId(Long userId);
}
