package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.TecnicoDTO;
import com.jp.eletrohub.exception.RegraNegocioException;
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
import org.modelmapper.ModelMapper;
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
    public ResponseEntity<List<TecnicoDTO>> list() {
        var tecnicos = tecnicoService.list().stream().map(TecnicoDTO::create).toList();
        return ResponseEntity.ok(tecnicos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter técnico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Técnico encontrado"),
            @ApiResponse(responseCode = "404", description = "Técnico não encontrado", content = @Content)
    })
    public ResponseEntity<TecnicoDTO> get(@Parameter(description = "ID do técnico", required = true) @PathVariable("id") Long id) {
        var tecnico = tecnicoService.findById(id).map(TecnicoDTO::create).orElseThrow(() -> new RegraNegocioException("Técnico não encontrado"));
        return ResponseEntity.ok(tecnico);
    }

    @PostMapping()
    @Operation(summary = "Criar técnico")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Técnico criado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<Object> post(@Parameter(description = "Dados do técnico", required = true) @RequestBody TecnicoDTO dto) {
        try {
            Tecnico tecnico = converter(dto);
            tecnico = tecnicoService.save(tecnico);
            return ResponseEntity.status(HttpStatus.CREATED).body(TecnicoDTO.create(tecnico));
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualizar técnico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Técnico atualizado"),
            @ApiResponse(responseCode = "404", description = "Técnico não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<Object> update(@Parameter(description = "ID do técnico", required = true) @PathVariable("id") Long id, @Parameter(description = "Dados do técnico", required = true) @RequestBody TecnicoDTO dto) {
        if (tecnicoService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            Tecnico tecnico = converter(dto);
            tecnico.setId(id);
            tecnicoService.save(tecnico);
            return ResponseEntity.ok(tecnico);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Excluir técnico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Técnico removido"),
            @ApiResponse(responseCode = "404", description = "Técnico não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro ao remover")
    })
    public ResponseEntity<Object> update(@Parameter(description = "ID do técnico", required = true) @PathVariable("id") Long id) {
        var tecnico = tecnicoService.findById(id);
        if (tecnico.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            tecnicoService.delete(tecnico.get());
            return ResponseEntity.ok().build();
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Tecnico converter(TecnicoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Tecnico.class);
    }
}
