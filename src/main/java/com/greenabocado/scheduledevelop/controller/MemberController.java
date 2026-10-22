package com.greenabocado.scheduledevelop.controller;

import com.greenabocado.scheduledevelop.common.SessionConst;
import com.greenabocado.scheduledevelop.dto.member.*;
import com.greenabocado.scheduledevelop.entity.member.Role;
import com.greenabocado.scheduledevelop.service.MemberService;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
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

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session, HttpServletResponse response) {
        expireSessionAndCookie(session, response);
        return ResponseEntity.ok().build();
    }

    private void expireSessionAndCookie(HttpSession session, HttpServletResponse response) {
        session.invalidate();   // 세션 저장소에서 엔트리 삭제
        Cookie cookie = new Cookie(SessionConst.JSESSIONID_KEY, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie); // 안 해도 문제는 없지만 클라이언트측 쿠키 삭제를 위함
    }

    private void initSession(HttpServletRequest request, Long id, Role role) {
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.MEMBER_ID, id);
        session.setAttribute(SessionConst.MEMBER_ROLE, role);
    }
}
