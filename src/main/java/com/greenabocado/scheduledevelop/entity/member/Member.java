package com.greenabocado.scheduledevelop.entity.member;

import com.greenabocado.scheduledevelop.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@Getter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=50)
    private String name;

    @Column(nullable=false, unique=true, length=100)
    private String email;

    @Column(nullable=false, length=20)
    @Enumerated(EnumType.STRING)
    private Role role;

    // 해시 함수 변경을 대비해 255자로 넓게 설정
    @Column(nullable=false)
    private String password;

    // String 3개라 빌더 사용
    @Builder
    private Member(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = Role.USER;
    }
}