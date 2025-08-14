package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.CredenciaisDTO;
import com.jp.eletrohub.api.dto.RegisterDTO;
import com.jp.eletrohub.api.dto.TokenDTO;
import com.jp.eletrohub.exception.SenhaInvalidaException;
import com.jp.eletrohub.model.entity.Usuario;
import com.jp.eletrohub.security.AuthenticationService;
import com.jp.eletrohub.security.JwtService;
import com.jp.eletrohub.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuário", description = "Autenticação e gerenciamento de usuários")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar usuário", description = "Cria um novo usuário com senha criptografada")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public Usuario salvar(
            @Parameter(description = "Dados do usuário", required = true) @RequestBody RegisterDTO usuario) {
        String senhaCriptografada = authenticationService.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        return usuarioService.saveOrCreate(usuario);
    }

    @PostMapping("/auth")
    @Operation(summary = "Autenticar usuário", description = "Gera um token JWT para acesso às APIs protegidas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticado com sucesso", content = @Content(schema = @Schema(implementation = TokenDTO.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content)
    })
    public TokenDTO autenticar(
            @Parameter(description = "Credenciais de login", required = true) @RequestBody CredenciaisDTO credenciais) {
        try {
            Usuario usuario = Usuario.builder()
                    .login(credenciais.getLogin())
                    .senha(credenciais.getSenha()).build();
            UserDetails usuarioAutenticado = authenticationService.authenticate(usuario);
            String token = jwtService.gerarToken(usuario);
            return new TokenDTO(usuario.getLogin(), token);
        } catch (UsernameNotFoundException | SenhaInvalidaException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
