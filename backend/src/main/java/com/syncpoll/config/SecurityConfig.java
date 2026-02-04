package com.syncpoll.config;

import com.syncpoll.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthService authService;

    public SecurityConfig(AuthService authService) {
        this.authService = authService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // public endpoints
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/ws/**").permitAll()
                .requestMatchers("/api/join").permitAll()
                .requestMatchers("/api/sessions/join/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/sessions/*/polls/*/results").permitAll()
                
                // participant endpoints - need participant header, not auth
                .requestMatchers(HttpMethod.POST, "/api/sessions/*/polls/*/answer").permitAll()
                
                // host endpoints - require authentication
                .requestMatchers("/api/sessions/**").authenticated()
                .requestMatchers("/api/users/**").authenticated()
                
                .anyRequest().permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(oauth2UserService())
                )
                .successHandler((request, response, authentication) -> {
                    response.sendRedirect("/api/auth/success");
                })
                .failureHandler((request, response, exception) -> {
                    response.sendRedirect("/api/auth/failure");
                })
            )
            .logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_OK);
                })
            );

        return http.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        
        return request -> {
            OAuth2User oauth2User = delegate.loadUser(request);
            
            String email = oauth2User.getAttribute("email");
            String name = oauth2User.getAttribute("name");
            String googleId = oauth2User.getAttribute("sub");
            String picture = oauth2User.getAttribute("picture");
            
            // create or update user in our database
            authService.findOrCreateUser(email, name, googleId, picture);
            
            return oauth2User;
        };
    }
}
