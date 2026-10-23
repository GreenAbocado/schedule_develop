package com.greenabocado.scheduledevelop.controller;

import com.greenabocado.scheduledevelop.dto.member.MemberResponse;
import com.greenabocado.scheduledevelop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final MemberService memberService;

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponse>> getAllMembers() {
        return ResponseEntity.ok()
                .body(memberService.getAllMembersByAdmin());
    }
}
