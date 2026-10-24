package com.greenabocado.scheduledevelop.repository;

import com.greenabocado.scheduledevelop.dto.schedule.ScheduleSummaryResponse;
import com.greenabocado.scheduledevelop.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("SELECT new com.greenabocado.scheduledevelop.dto.schedule.ScheduleSummaryResponse" +
            "(s.id, s.title, s.updatedAt, m.name) " +
            "FROM Schedule s JOIN s.member m")
    List<ScheduleSummaryResponse> findAllWithMember();
}
