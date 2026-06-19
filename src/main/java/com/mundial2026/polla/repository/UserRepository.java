package com.mundial2026.polla.repository;

import com.mundial2026.polla.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    long countByPaid(boolean paid);

    @Query("SELECT SUM(CASE WHEN u.entryFee IS NOT NULL THEN u.entryFee ELSE 0 END) FROM User u WHERE u.paid = true")
    Double getTotalIncomeForPaidUsers();

    @Query("SELECT SUM(CASE WHEN u.totalPoints IS NOT NULL THEN u.totalPoints ELSE 0 END) FROM User u")
    Long getTotalPointsSum();
}
