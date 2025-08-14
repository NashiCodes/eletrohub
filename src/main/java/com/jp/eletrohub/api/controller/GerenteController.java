package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.GerenteDTO;
import com.jp.eletrohub.exception.RegraNegocioException;
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
    public ResponseEntity<List<GerenteDTO>> get() {
        var gerentes = gerenteService.list().stream().map(GerenteDTO::create).toList();
        return ResponseEntity.ok(gerentes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter gerente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Gerente encontrado"),
            @ApiResponse(responseCode = "404", description = "Gerente não encontrado", content = @Content)
    })
    public ResponseEntity<GerenteDTO> get(@Parameter(description = "ID do gerente", required = true) @PathVariable("id") Long id) {
        var gerente = gerenteService.findById(id).map(GerenteDTO::create).orElseThrow(() -> new RegraNegocioException("Gerente não encontrado"));
        return ResponseEntity.ok(gerente);
    }

    @PostMapping()
    @Operation(summary = "Criar gerente")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Gerente criado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<Object> post(@Parameter(description = "Dados do gerente", required = true) @RequestBody GerenteDTO dto) {
        try {
            Gerente gerente = GerenteDTO.toEntity(dto);
            gerente = gerenteService.salvar(gerente);
            return ResponseEntity.status(HttpStatus.CREATED).body(GerenteDTO.create(gerente));
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualizar gerente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Gerente atualizado"),
            @ApiResponse(responseCode = "404", description = "Gerente não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<Object> update(@Parameter(description = "ID do gerente", required = true) @PathVariable("id") Long id, @Parameter(description = "Dados do gerente", required = true) @RequestBody GerenteDTO dto) {
        if (gerenteService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            Gerente gerente = GerenteDTO.toEntity(dto);
            gerente.setId(id);
            gerenteService.salvar(gerente);
            return ResponseEntity.ok(gerente);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Excluir gerente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Gerente removido"),
            @ApiResponse(responseCode = "404", description = "Gerente não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro ao remover")
    })
    public ResponseEntity<Object> delete(@Parameter(description = "ID do gerente", required = true) @PathVariable("id") Long id) {
        var gerente = gerenteService.findById(id);
        if (gerente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            gerenteService.delete(gerente.get());
            return ResponseEntity.ok().build();
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
