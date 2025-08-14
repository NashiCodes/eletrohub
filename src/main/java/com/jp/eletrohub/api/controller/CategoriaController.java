package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.venda.CategoriaDTO;
import com.jp.eletrohub.exception.ResponseException;
import com.jp.eletrohub.model.entity.Categoria;
import com.jp.eletrohub.service.CategoriaService;
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
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
@Tag(name = "Categoria", description = "API para gerenciamento de categorias de produtos")
@SecurityRequirement(name = "bearerAuth")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    @Operation(summary = "Listar todas as categorias", description = "Retorna uma lista com todas as categorias cadastradas")
    @ApiResponse(responseCode = "200", description = "Lista de categorias retornada com sucesso")
    public ResponseEntity<List<Categoria>> list() {
        return ResponseEntity.ok(categoriaService.list().stream().toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar categoria por ID", description = "Retorna uma categoria específica pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content)
    })
    public ResponseEntity<Categoria> list(
            @Parameter(description = "ID da categoria", required = true)
            @PathVariable("id") Long id) {
        var categoria = categoriaService.findById(id);
        return categoria.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    @Operation(summary = "Cadastrar nova categoria", description = "Cria uma nova categoria no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para criação da categoria")
    })
    public ResponseEntity<?> post(
            @Parameter(description = "Dados da categoria", required = true)
            @Valid
            @RequestBody CategoriaDTO dto) {
        try {
            Categoria categoria = categoriaService.saveOrCreate(null, dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(categoria);
        } catch (Exception e) {
            return new ResponseException(e).toResponseEntity();
        }
    }


    @PutMapping("{id}")
    @Operation(summary = "Atualizar categoria", description = "Atualiza os dados de uma categoria existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização da categoria")
    })
    public ResponseEntity<?> update(
            @Parameter(description = "ID da categoria", required = true)
            @PathVariable("id") Long id,
            @Parameter(description = "Novos dados da categoria", required = true)
            @Valid
            @RequestBody CategoriaDTO dto) {
        try {
            var categoria = categoriaService.saveOrCreate(id, dto);
            return ResponseEntity.ok(categoria);
        } catch (Exception e) {
            return new ResponseException(e).toResponseEntity();
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Excluir categoria", description = "Remove uma categoria do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
            @ApiResponse(responseCode = "400", description = "Não é possível excluir a categoria")
    })
    public ResponseEntity<?> delete(
            @Parameter(description = "ID da categoria", required = true)
            @PathVariable("id") Long id) {
        try {
            categoriaService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseException(e).toResponseEntity();
        }
    }
}
