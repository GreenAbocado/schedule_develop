package com.greenabocado.scheduledevelop.repository;

import com.greenabocado.scheduledevelop.dto.schedule.ScheduleSummaryResponse;
import com.greenabocado.scheduledevelop.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("""
            SELECT new com.greenabocado.scheduledevelop.dto.schedule.ScheduleSummaryResponse
            (s.id, s.title, s.updatedAt, m.name)
            FROM Schedule s JOIN s.member m
            """)
    List<ScheduleSummaryResponse> findAllWithMemberName();

    List<Schedule> findAllByMemberId(Long memberId);

    @Modifying /* 사용 시점에 1차캐시에 아무 엔티티도 없으므로 따로 clearAutomatically 적용 X */
    @Query("DELETE FROM Schedule s WHERE s.member.id = :memberId")
    long deleteAllByMemberIdInBulk(@Param("memberId") Long memberId);
}