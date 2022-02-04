package com.nahwasa.toy.expnewthings.backend.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.StringTokenizer;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    TokenProvider tokenProvider;

    private String parseBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null)
            return null;

        StringTokenizer st = new StringTokenizer(bearerToken);
        if (st.countTokens() != 2 || !st.nextToken().equals("Bearer"))
            return null;

        String token = st.nextToken();
        if (token.isEmpty())
            return null;

        return token;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            log.info("Filter is running..");

            String token = parseBearerToken(request);
            if (token != null) {
                String userId = tokenProvider.validateAndGetUserId(token);
                log.info("Authenticated user ID : " + userId);

                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId, // 여기 작성한게 Controller에서 '@AuthenticationPrincipal' 부분에 박힘.
                        null,
                        AuthorityUtils.NO_AUTHORITIES
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);

                SecurityContextHolder.setContext(securityContext);

            }
        } catch (Exception e) {
            logger.error("Could not set user authentication in security context.", e);
        }

        filterChain.doFilter(request, response);    // 다음 ServeletFilter 실행 (다른 필터들도 실행될 수 있도록 함)
    }
}
