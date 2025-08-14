package com.jp.eletrohub.service;

import com.jp.eletrohub.api.dto.login.RegisterDTO;
import com.jp.eletrohub.model.entity.Usuario;
import com.jp.eletrohub.model.repository.UsuarioRepository;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {
    private final UsuarioRepository repository;

    @Transactional
    public Usuario saveOrCreate(RegisterDTO dto) {
        if (repository.findByLogin(dto.getLogin()).isPresent()) {
            throw new IllegalArgumentException("Usuário já existe com o login: " + dto.getLogin());
        }
        var newUser = Usuario.builder()
                .login(dto.getLogin())
                .senha(dto.getSenha())
                .admin(dto.isAdmin())
                .build();
        return repository.save(newUser);
    }

    @Override
    public UserDetails loadUserByUsername(@Email(
            message = "O email deve ser válido"
    ) String username) {

        var usuario = repository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        var roles = usuario.isAdmin()
                ? new String[]{"ADMIN", "USER"}
                : new String[]{"USER"};

        return User
                .builder()
                .username(usuario.getLogin())
                .password(usuario.getSenha())
                .roles(roles)
                .build();
    }
}