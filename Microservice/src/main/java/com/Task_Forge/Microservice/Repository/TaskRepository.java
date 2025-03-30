package com.Task_Forge.Microservice.Repository;

import com.Task_Forge.Microservice.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    @Query("SELECT COUNT(t) FROM Task t WHERE t.assignedTo.id = :employeeId AND t.status = 'COMPLETED' AND t.completedAt >= :sevenDaysAgo")
    int countCompletedTasks(@Param("employeeId") UUID employeeId, @Param("sevenDaysAgo") LocalDate sevenDaysAgo);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.userId AND t.createdAt >= :lastWeek")
    long countTasksLast7Days(@Param("userId") UUID userId, @Param("lastWeek")LocalDateTime lastWeek);
}
