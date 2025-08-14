package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.funcionarios.GerenteDTO;
import com.jp.eletrohub.exception.ResponseException;
import com.jp.eletrohub.model.entity.Gerente;
import com.jp.eletrohub.service.GerenteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/gerentes")
@RequiredArgsConstructor
@Tag(name = "Gerente", description = "Gerenciamento de gerentes")
@SecurityRequirement(name = "bearerAuth")
public class GerenteController {

    private final GerenteService gerenteService;

    @GetMapping
    @Operation(summary = "Listar gerentes")
    @ApiResponse(responseCode = "200", description = "Lista retornada")
    public ResponseEntity<List<Gerente>> get() {
        var gerentes = gerenteService.list().stream().toList();
        return ResponseEntity.ok(gerentes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter gerente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Gerente encontrado"),
            @ApiResponse(responseCode = "404", description = "Gerente não encontrado", content = @Content)
    })
    public ResponseEntity<Gerente> get(@Parameter(description = "ID do gerente", required = true) @PathVariable("id") Long id) {
        var gerente = gerenteService.findById(id);
        return gerente.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    @Operation(summary = "Criar gerente")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Gerente criado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<?> post(@Parameter(description = "Dados do gerente", required = true) @RequestBody GerenteDTO dto) {
        try {
            var gerente = gerenteService.saveOrCreate(null, dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(gerente);
        } catch (Exception e) {
            return new ResponseException(e).toResponseEntity();
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualizar gerente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Gerente atualizado"),
            @ApiResponse(responseCode = "404", description = "Gerente não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<?> update(@Parameter(description = "ID do gerente", required = true) @PathVariable("id") Long id,
                                    @Parameter(description = "Dados do gerente", required = true) @RequestBody GerenteDTO dto) {
        try {
            var gerente = gerenteService.saveOrCreate(id, dto);
            return ResponseEntity.ok(gerente);
        } catch (Exception e) {
            return new ResponseException(e).toResponseEntity();
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Excluir gerente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Gerente removido"),
            @ApiResponse(responseCode = "404", description = "Gerente não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro ao remover")
    })
    public ResponseEntity<?> delete(@Parameter(description = "ID do gerente", required = true) @PathVariable("id") Long id) {
        try {
            gerenteService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseException(e).toResponseEntity();
        }
    }
}
