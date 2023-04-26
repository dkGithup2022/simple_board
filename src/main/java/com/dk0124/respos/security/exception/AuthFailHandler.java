package com.dk0124.respos.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFailHandler implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("Authorization failed : {}", request.getPathInfo());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        HashMap<String, String> body = new HashMap<>();
        body.put("status", String.valueOf(HttpServletResponse.SC_UNAUTHORIZED));
        body.put("error", "unauthorized request");
        body.put("message", authException.getMessage());
        if(request.getPathInfo() != null)
            body.put("path", request.getPathInfo());

        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
