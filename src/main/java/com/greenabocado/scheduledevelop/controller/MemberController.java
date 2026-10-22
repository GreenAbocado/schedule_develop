package com.greenabocado.scheduledevelop.controller;

import com.greenabocado.scheduledevelop.common.SessionConst;
import com.greenabocado.scheduledevelop.dto.member.*;
import com.greenabocado.scheduledevelop.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
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
        request.getSession().setAttribute(SessionConst.MEMBER_ID, result.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(result.getMemberResponse());
    }
}
