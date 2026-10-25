package com.greenabocado.scheduledevelop.entity;

import com.greenabocado.scheduledevelop.entity.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Getter
public class Comment extends BaseEntity{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=500)
    private String content;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="member_id", nullable=false)
    private Member member;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="schedule_id", nullable=false)
    private Schedule schedule;

    private Comment(String content, Member member, Schedule schedule) {
        this.content = content;
        this.member = member;
        this.schedule = schedule;
    }

    public static Comment of(String content, Member member, Schedule schedule) {
        return new Comment(content, member, schedule);
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public boolean isWrittenBy(Long memberId) {
        return member.getId().equals(memberId);
    }
}
