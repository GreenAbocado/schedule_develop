package com.greenabocado.scheduledevelop.controller;

import com.greenabocado.scheduledevelop.common.SessionConst;
import com.greenabocado.scheduledevelop.dto.member.*;
import com.greenabocado.scheduledevelop.entity.member.Role;
import com.greenabocado.scheduledevelop.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberResponse> register(
            @RequestBody MemberCreateRequest requestDto, HttpServletRequest request) {
        MemberServiceResult result = memberService.register(requestDto);
        initSession(request, result.getId(), result.getRole());
        return ResponseEntity.status(HttpStatus.CREATED).body(result.getMemberResponse());
    }

    @PostMapping("/login")
    public ResponseEntity<MemberResponse> login(
            @RequestBody LoginRequest requestDto, HttpServletRequest request) {
        MemberServiceResult result = memberService.login(requestDto);
        initSession(request, result.getId(), result.getRole());
        return ResponseEntity.ok().body(result.getMemberResponse());
    }

    private void initSession(HttpServletRequest request, Long id, Role role) {
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.MEMBER_ID, id);
        session.setAttribute(SessionConst.MEMBER_ROLE, role);
    }
}
