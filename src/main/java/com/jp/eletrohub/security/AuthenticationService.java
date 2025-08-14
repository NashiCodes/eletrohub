package com.jp.eletrohub.security;

import com.jp.eletrohub.exception.SenhaInvalidaException;
import com.jp.eletrohub.model.entity.Usuario;
import com.jp.eletrohub.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UsuarioService usuarioService;
    private final PasswordEncoder encoder;

    public UserDetails authenticate(Usuario usuario) {
        UserDetails user = usuarioService.loadUserByUsername(usuario.getLogin());
        boolean senhasBatem = encoder.matches(usuario.getSenha(), user.getPassword());

        if (senhasBatem) {
            return user;
        }
        throw new SenhaInvalidaException();
    }

    public String encode(String senha) {
        return encoder.encode(senha);
    }
}
