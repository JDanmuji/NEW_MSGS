package com.msgs.msgs.jwt.controller;

import com.msgs.msgs.dto.TokenInfo;
import com.msgs.msgs.dto.UserLoginRequestDto;
import com.msgs.msgs.jwt.JwtTokenProvider;
import com.msgs.msgs.jwt.service.UserJwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController2 {

    private final UserJwtService userJwtService;
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public TokenInfo login(@RequestBody UserLoginRequestDto userLoginRequestDto) {

        String userEmail = userLoginRequestDto.getEmail();
        String password = userLoginRequestDto.getPassword();

        TokenInfo tokenInfo = userJwtService.login(userEmail, password);

        return tokenInfo;
    }
    
    
    
    @PostMapping("/info")
    public ResponseEntity<?> getUserInfo(@RequestParam String accessToken) {
        JSONObject userInfo = userJwtService.getUserInfo(accessToken);
        return ResponseEntity.ok().body(userInfo.toString());
    }

}


