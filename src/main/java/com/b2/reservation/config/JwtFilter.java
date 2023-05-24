package com.b2.reservation.config;

import com.b2.reservation.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Generated;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Generated
public class JwtFilter extends OncePerRequestFilter {
    private final RestTemplate restTemplate;
    private final JwtUtils jwtUtils;
    private static final String JWT_HEADER = "Authorization";
    private static final String JWT_TOKEN_PREFIX = "Bearer";
    @Override
    protected void doFilterInternal
            (@NonNull HttpServletRequest request,
             @NonNull HttpServletResponse response,
             @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(JWT_HEADER);
        final String jwtToken;
        final String userEmail;
        final String role;

        if (authHeader == null || !authHeader.startsWith(JWT_TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }
        try{
            jwtToken = authHeader.substring(7);
            userEmail =jwtUtils.extractUsername(jwtToken);
            role = jwtUtils.extractClaim(jwtToken, claims ->  (String) claims.get("role"));

            if(!isUserExist(jwtToken)){
                filterChain.doFilter(request, response);
                return;
            }

            if(userEmail != null
                    && SecurityContextHolder.getContext().getAuthentication() == null && role != null
                    && !jwtUtils.isTokenExpired(jwtToken)){
                var authenticationToken = new UsernamePasswordAuthenticationToken(
                        userEmail,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + role))
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }else{
                filterChain.doFilter(request, response);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        filterChain.doFilter(request, response);
    }

    private boolean isUserExist(String jwtToken){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(jwtToken);
        HttpEntity<Void> httpEntity = new HttpEntity<>(httpHeaders);
        try {
            restTemplate.exchange("http://34.142.212.224:100/authorization/user-checking", HttpMethod.GET, httpEntity, Object.class);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
