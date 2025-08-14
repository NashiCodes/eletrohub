package com.jp.eletrohub.security;

import com.jp.eletrohub.api.dto.login.CredenciaisDTO;
import com.jp.eletrohub.exception.SenhaInvalidaException;
import com.jp.eletrohub.service.UsuarioService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UsuarioService usuarioService;
    private final PasswordEncoder encoder;

    public void authenticate(CredenciaisDTO dto) {
        UserDetails user = usuarioService.loadUserByUsername(dto.getLogin());
        boolean senhasBatem = encoder.matches(dto.getSenha(), user.getPassword());

        if (senhasBatem) {
            return;
        }
        throw new SenhaInvalidaException();
    }

    public String encode(@NotBlank(message = "A senha não pode estar em branco") String senha) {
        return encoder.encode(senha);
    }
}
