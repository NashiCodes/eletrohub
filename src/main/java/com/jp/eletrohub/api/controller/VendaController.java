package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.venda.VendaDTO;
import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.exception.ResponseException;
import com.jp.eletrohub.model.entity.Venda;
import com.jp.eletrohub.service.VendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vendas")
@RequiredArgsConstructor
@Tag(name = "Venda", description = "Gerenciamento de vendas")
@SecurityRequirement(name = "bearerAuth")
public class VendaController {

    private final VendaService vendaService;

    @GetMapping
    @Operation(summary = "Listar vendas")
    @ApiResponse(responseCode = "200", description = "Lista retornada")
    public ResponseEntity<List<VendaDTO>> get() {
        var vendas = vendaService.list().stream().map(VendaDTO::create).toList();
        return ResponseEntity.ok(vendas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter venda")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venda encontrada"),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada", content = @Content)
    })
    public ResponseEntity<VendaDTO> get(
            @Parameter(description = "ID da venda", required = true) @PathVariable("id") Long id) {
        var venda = vendaService.findById(id).map(VendaDTO::create).orElseThrow(() -> new RegraNegocioException("Venda não encontrada"));
        return ResponseEntity.ok(venda);
    }

    @PostMapping()
    @Operation(summary = "Criar venda")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Venda criada"),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<?> post(
            @Parameter(description = "Dados da venda", required = true) @Valid @RequestBody VendaDTO dto) {
        try {
            Venda venda = vendaService.saveOrCreate(null, dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(VendaDTO.create(venda));
        } catch (Exception e) {
            return new ResponseException(e).toResponseEntity();
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualizar venda")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venda atualizada"),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<?> update(
            @Parameter(description = "ID da venda", required = true) @PathVariable("id") Long id,
            @Parameter(description = "Dados da venda", required = true) @RequestBody VendaDTO dto) {
        try {
            Venda venda = vendaService.saveOrCreate(id, dto);
            return ResponseEntity.ok(venda);
        } catch (Exception e) {
            return new ResponseException(e).toResponseEntity();
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Excluir venda")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venda removida"),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro ao remover")
    })
    public ResponseEntity<?> delete(
            @Parameter(description = "ID da venda", required = true) @PathVariable("id") Long id) {
        try {
            vendaService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseException(e).toResponseEntity();
        }
    }

}
