package com.greenabocado.scheduledevelop.init;

import com.greenabocado.scheduledevelop.config.PasswordEncoder;
import com.greenabocado.scheduledevelop.entity.member.Member;
import com.greenabocado.scheduledevelop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@RequiredArgsConstructor
/* 개발 환경에서만 테스트 목적으로 관리자 초기화 */
public class AdminInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Member admin = Member.builder()
                .name("admin")
                .email("admin@admin.com")
                .password(passwordEncoder.encode("admin@123")).build();

        admin.upgradeToAdmin();
        memberRepository.save(admin);
    }
}
