package com.sparta.preonboardingtask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.preonboardingtask.exception.ErrorCode;
import com.sparta.preonboardingtask.exception.ExceptionResponse;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtTokenizer jwtTokenizer;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthorizationFilter(JwtTokenizer jwtTokenizer, CustomUserDetailsService userDetailsService) {
        this.jwtTokenizer = jwtTokenizer;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String reqToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(reqToken)) {
            String tokenValue = jwtTokenizer.substringToken(reqToken);
            if (!jwtTokenizer.validateToken(tokenValue)) {
                jwtExceptionHandler(response, ErrorCode.TOKEN_EXPIRED);
                return;
            }

            Claims info = jwtTokenizer.getUserInfoFromToken(tokenValue);

            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                jwtExceptionHandler(response, ErrorCode.TOKEN_MISMATCH);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public void jwtExceptionHandler(HttpServletResponse response, ErrorCode error) {
        response.setStatus(error.getStatus());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String json = new ObjectMapper().writeValueAsString(new ExceptionResponse(error.getStatus(), error.getMessage()));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }
}
