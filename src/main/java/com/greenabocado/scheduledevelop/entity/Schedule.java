package com.greenabocado.scheduledevelop.entity;

import com.greenabocado.scheduledevelop.entity.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@Getter
public class Schedule extends BaseEntity{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=50)
    private String title;

    @Column(length=1000)
    private String content;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="member_id", nullable=false)
    private Member member;

    private Schedule(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }

    public static Schedule of(String title, String content, Member member) {
        return new Schedule(title, content, member);
    }

    public void updateAll(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public boolean isWrittenBy(Long memberId) {
        return member.getId().equals(memberId);
    }
}