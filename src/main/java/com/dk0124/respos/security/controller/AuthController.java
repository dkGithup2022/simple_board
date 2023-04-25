package com.dk0124.respos.security.controller;

import com.dk0124.respos.member.Member;
import com.dk0124.respos.member.dao.MemberRepository;
import com.dk0124.respos.role.ERole;
import com.dk0124.respos.role.Role;
import com.dk0124.respos.security.dto.JoinRequestDto;
import com.dk0124.respos.security.dto.LoginRequestDto;
import com.dk0124.respos.security.dto.MessageResponse;
import com.dk0124.respos.security.jwt.JwtUtils;
import com.dk0124.respos.security.userDetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {


    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<MessageResponse> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        // authenticate with reqInfo
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie cookie = JwtUtils.generateTokenFromUserDetails(userDetails); // fix

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("succful login ! , you are good to go "));
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(@Valid @RequestBody JoinRequestDto joinRequestDto) {
        if (memberRepository.findMemberByEmail(joinRequestDto.getEmail()).isPresent())
            throw new RuntimeException("Email already exists");
        if (memberRepository.findMemberByNickName(joinRequestDto.getNickName()).isPresent())
            throw new RuntimeException("NickName already exists");

        Member newOne = buildMember(joinRequestDto);
        memberRepository.save(newOne);
        return ResponseEntity.ok(new MessageResponse("User registered succses"));
    }

    private Member buildMember(JoinRequestDto joinRequestDto) {
        Set<Role> roles = new HashSet<>();
        joinRequestDto.getRoles().stream().forEach(
                e -> {
                    if (ERole.contains(e))
                        roles.add(new Role(ERole.getRole(e)));
                }
        );
        return new Member(joinRequestDto.getEmail(), joinRequestDto.getNickName(), passwordEncoder.encode(joinRequestDto.getPassword()), roles);
    }
}
