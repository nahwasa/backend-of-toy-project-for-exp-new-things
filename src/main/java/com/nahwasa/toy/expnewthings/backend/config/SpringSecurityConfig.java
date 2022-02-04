package com.nahwasa.toy.expnewthings.backend.config;

import com.nahwasa.toy.expnewthings.backend.security.JwtAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Slf4j
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/**", "/api-docs/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors() // Cors는 CorsConfig에서 이미 설정했으므로 따로 건드리지 않음
                .and()
                    .csrf().disable()   // csrf 사용하지 않음
                    .httpBasic().disable()  // BASIC 인증 사용하지 않음
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // session 기반 아님을 선언
                .and()
                    .authorizeRequests()
                        .antMatchers("/", "/auth/**", "/swagger*/**")
                        .permitAll()   // '/'랑 '/auth/**' 경로는 인증 제외
                    .anyRequest()
                        .authenticated();   // 그 외의 모든 경로는 인증 필요

        // JWT 필터 적용. 매 요청마다 CorsFilter 이후 JwtAuthenticationFilter 실행
        http.addFilterAfter(
                jwtAuthenticationFilter,
                CorsFilter.class
        );
    }
}
