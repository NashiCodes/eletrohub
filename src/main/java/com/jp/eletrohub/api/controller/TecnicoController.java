package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.funcionarios.TecnicoDTO;
import com.jp.eletrohub.exception.ResponseException;
import com.jp.eletrohub.model.entity.Tecnico;
import com.jp.eletrohub.service.TecnicoService;
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
@RequestMapping("/api/v1/tecnicos")
@RequiredArgsConstructor
@Tag(name = "Técnico", description = "Gerenciamento de técnicos")
@SecurityRequirement(name = "bearerAuth")
public class TecnicoController {

    private final TecnicoService tecnicoService;

    @GetMapping
    @Operation(summary = "Listar técnicos")
    @ApiResponse(responseCode = "200", description = "Lista retornada")
    public ResponseEntity<List<Tecnico>> list() {
        var tecnicos = tecnicoService.list().stream().toList();
        return ResponseEntity.ok(tecnicos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter técnico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Técnico encontrado"),
            @ApiResponse(responseCode = "404", description = "Técnico não encontrado", content = @Content)
    })
    public ResponseEntity<Tecnico> get(@Parameter(description = "ID do técnico", required = true) @PathVariable("id") Long id) {
        var tecnico = tecnicoService.findById(id);
        return tecnico.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    @Operation(summary = "Criar técnico")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Técnico criado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<?> post(@Parameter(description = "Dados do técnico", required = true) @RequestBody TecnicoDTO dto) {
        try {
            var tecnico = tecnicoService.saveOrCreate(null, dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(tecnico);
        } catch (Exception e) {
            return new ResponseException(e).toResponseEntity();
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualizar técnico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Técnico atualizado"),
            @ApiResponse(responseCode = "404", description = "Técnico não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<?> update(@Parameter(description = "ID do técnico", required = true) @PathVariable("id") Long id, @Parameter(description = "Dados do técnico", required = true) @RequestBody TecnicoDTO dto) {
        if (tecnicoService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            var tecnico = tecnicoService.saveOrCreate(id, dto);
            return ResponseEntity.ok(tecnico);
        } catch (Exception e) {
            return new ResponseException(e).toResponseEntity();
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Excluir técnico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Técnico removido"),
            @ApiResponse(responseCode = "404", description = "Técnico não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro ao remover")
    })
    public ResponseEntity<?> update(@Parameter(description = "ID do técnico", required = true) @PathVariable("id") Long id) {
        try {
            tecnicoService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseException(e).toResponseEntity();
        }
    }
}
