package com.jp.eletrohub.config;

import com.jp.eletrohub.security.JwtAuthFilter;
import com.jp.eletrohub.security.JwtService;
import com.jp.eletrohub.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtService jwtService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OncePerRequestFilter jwtFilter() {
        return new JwtAuthFilter(jwtService, usuarioService);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/categorias/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/clientes/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/gerentes/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/produtos/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/tecnicos/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/vendas/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/vendaitens/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/vendedores/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/usuarios/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}