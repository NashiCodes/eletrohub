package com.jp.eletrohub.security;

import com.jp.eletrohub.service.UsuarioService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest,
            @NonNull HttpServletResponse httpServletResponse,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authorization = httpServletRequest.getHeader("Authorization");

        if (authorization != null && authorization.startsWith(BEARER_PREFIX)) {
            String token = authorization.substring(BEARER_PREFIX.length()).trim();
            if (!token.isEmpty()) {
                Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
                if (currentAuth == null) {
                    try {
                        boolean isValid = jwtService.tokenValido(token);
                        if (isValid) {
                            String loginUsuario = jwtService.obterLoginUsuario(token);
                            UserDetails usuario = usuarioService.loadUserByUsername(loginUsuario);
                            UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(
                                    usuario, null, usuario.getAuthorities());
                            user.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                            SecurityContextHolder.getContext().setAuthentication(user);
                        }
                    } catch (Exception ignored) {
                        // deliberately ignore to proceed unauthenticated
                    }
                }
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}