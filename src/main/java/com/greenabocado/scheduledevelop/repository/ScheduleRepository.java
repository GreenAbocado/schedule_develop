package com.greenabocado.scheduledevelop.repository;

import com.greenabocado.scheduledevelop.dto.schedule.ScheduleSummaryResponse;
import com.greenabocado.scheduledevelop.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query(value="""
            SELECT new com.greenabocado.scheduledevelop.dto.schedule.ScheduleSummaryResponse
            (s.id, s.title, s.content, s.updatedAt, m.name)
            FROM Schedule s JOIN s.member m
            """, countQuery="SELECT COUNT(s) FROM Schedule s") /* 카운트 쿼리는 조인이 불필요하므로 따로 작성 */
    Page<ScheduleSummaryResponse> findAllWithMemberName(Pageable pageable);

    Page<Schedule> findAllByMemberId(Pageable pageable, Long memberId);

    @Modifying /* 사용 시점에 1차캐시에 아무 엔티티도 없으므로 따로 clearAutomatically 적용 X */
    @Query("DELETE FROM Schedule s WHERE s.member.id = :memberId")
    long deleteAllByMemberIdInBulk(@Param("memberId") Long memberId);
}