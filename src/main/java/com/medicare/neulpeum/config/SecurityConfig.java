package com.medicare.neulpeum.config;


import com.medicare.neulpeum.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                // 로그인 등 인증이 필요없는 요청에 대해서는 전체허용 (permitAll)
                // 그 외 모든 요청 (any) 에 대해서는 인증 요구
                .authorizeHttpRequests((authorizeRequest) ->
                        authorizeRequest
                                .requestMatchers("/api/login", "/api/**").permitAll()
                                .anyRequest().authenticated()
                )

                // Rest 방식으로 로그인을 할 것이므로 form 로그인 사용 안함
                .formLogin(AbstractHttpConfigurer::disable)

                // logout url 설정. 인증을 위해 사용하는 쿠키인 JSESSIONID를 삭제하여 로그아웃 처리
                .logout((configurer) ->
                        configurer
                                .logoutUrl("/api/logout")
                                .deleteCookies("JSESSIONID")
                )

                //인증되지 않은 자원에 접근했을 때
                .exceptionHandling((configurer) ->
                        configurer
                                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.FORBIDDEN)));


        return http.build();
    }


    //인증 작업을 해줄
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // security 에서 제공하는 암호화 알고리즘
        return new BCryptPasswordEncoder();
    }
}
