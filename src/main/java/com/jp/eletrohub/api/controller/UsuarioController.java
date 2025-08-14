package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.login.CredenciaisDTO;
import com.jp.eletrohub.api.dto.login.RegisterDTO;
import com.jp.eletrohub.api.dto.login.TokenDTO;
import com.jp.eletrohub.exception.ResponseException;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> salvar(
            @Parameter(description = "Dados do usuário", required = true) @Valid @RequestBody RegisterDTO dto) throws ResponseException {
        try {
            String senhaCriptografada = authenticationService.encode(dto.getSenha());
            dto.setSenha(senhaCriptografada);
            var usuario = usuarioService.saveOrCreate(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
        } catch (Exception e) {
            return new ResponseException(e).toResponseEntity();
        }
    }

    @PostMapping("/auth")
    @Operation(summary = "Autenticar usuário", description = "Gera um token JWT para acesso às APIs protegidas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticado com sucesso", content = @Content(schema = @Schema(implementation = TokenDTO.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content)
    })
    public ResponseEntity<?> autenticar(
            @Parameter(description = "Credenciais de login", required = true) @Valid @RequestBody CredenciaisDTO dto) throws ResponseException {
        try {
            var usuario = Usuario.builder()
                    .login(dto.getLogin())
                    .senha(dto.getSenha()).build();
            authenticationService.authenticate(dto);
            var token = jwtService.gerarToken(usuario);
            var tokenDTO = new TokenDTO(usuario.getLogin(), token);
            return ResponseEntity.ok(tokenDTO);
        } catch (Exception e) {
            return new ResponseException(e).toResponseEntity();
        }
    }
}
