package com.greenabocado.scheduledevelop.repository;

import com.greenabocado.scheduledevelop.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
