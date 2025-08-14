package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.funcionarios.VendedorDTO;
import com.jp.eletrohub.exception.ResponseException;
import com.jp.eletrohub.model.entity.Vendedor;
import com.jp.eletrohub.service.VendedorService;
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
@RequestMapping("/api/v1/vendedores")
@RequiredArgsConstructor
@Tag(name = "Vendedor", description = "Gerenciamento de vendedores")
@SecurityRequirement(name = "bearerAuth")
public class VendedorController {

    private final VendedorService vendedorService;

    @GetMapping
    @Operation(summary = "Listar vendedores")
    @ApiResponse(responseCode = "200", description = "Lista retornada")
    public ResponseEntity<List<Vendedor>> get() {
        var vendedores = vendedorService.list().stream().toList();
        return ResponseEntity.ok(vendedores);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter vendedor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vendedor encontrado"),
            @ApiResponse(responseCode = "404", description = "Vendedor não encontrado", content = @Content)
    })
    public ResponseEntity<Vendedor> get(
            @Parameter(description = "ID do vendedor", required = true) @PathVariable("id") Long id) {
        var vendedor = vendedorService.findById(id);
        return vendedor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    @Operation(summary = "Criar vendedor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Vendedor criado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<?> post(
            @Parameter(description = "Dados do vendedor", required = true) @Valid @RequestBody VendedorDTO dto) {
        try {
            var vendedor = vendedorService.saveOrCreate(null, dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(vendedor);
        } catch (Exception e) {
            return new ResponseException(e).toResponseEntity();
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualizar vendedor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vendedor atualizado"),
            @ApiResponse(responseCode = "404", description = "Vendedor não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<?> update(
            @Parameter(description = "ID do vendedor", required = true) @PathVariable("id") Long id,
            @Parameter(description = "Dados do vendedor", required = true) @Valid @RequestBody VendedorDTO dto) {
        try {
            var vendedor = vendedorService.saveOrCreate(id, dto);
            return ResponseEntity.ok(vendedor);
        } catch (Exception e) {
            return new ResponseException(e).toResponseEntity();
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Excluir vendedor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vendedor removido"),
            @ApiResponse(responseCode = "404", description = "Vendedor não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro ao remover")
    })
    public ResponseEntity<?> delete(
            @Parameter(description = "ID do vendedor", required = true) @PathVariable("id") Long id) {
        try {
            vendedorService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseException(e).toResponseEntity();
        }
    }
}
