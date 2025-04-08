package com.Task_Forge.Microservice.Repository;

import com.Task_Forge.Microservice.Entity.TaskActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskActivityRepository extends JpaRepository<TaskActivity, Long> {

    // Count how many tasks were updated in the last 7 days
    @Query("SELECT COUNT(t) FROM TaskActivity t Where t.updatedAt >= :sevenDaysAgo") // manually counts query
    long countUpdatesInLast7Days(LocalDateTime sevenDaysAgo);

    //Count how many tasks are due in last 7 days
    @Query("SELECT COUNT(t) FROM TaskActivity t Where t.dueDate >= sevenDaysAgo AND t.dueDate <= :now")
    long countDueSoonTasks(@Param("sevenDaysAgo") LocalDateTime sevenDaysAgo, @Param("now") LocalDateTime now);




}