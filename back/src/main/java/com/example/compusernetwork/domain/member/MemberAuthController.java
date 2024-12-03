package com.example.compusernetwork.domain.member;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class MemberAuthController {

    private final MemberRepository userRepository;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Member member) {
        if (userRepository.existsById(member.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
        userRepository.save(member);
        return ResponseEntity.ok("User registered successfully");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Member loginMember, HttpSession session) {
        Optional<Member> userOpt = userRepository.findByName(loginMember.getName());
        if (userOpt.isPresent()) {
            Member member = userOpt.get();
            if (member.getPassword().equals(loginMember.getPassword())) {
                session.setAttribute("username", member.getName()); // session에 username 저장
                return ResponseEntity.ok("Login successful");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully");
    }
}