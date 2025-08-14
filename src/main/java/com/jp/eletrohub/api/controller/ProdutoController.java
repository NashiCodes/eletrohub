package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.venda.ProdutoDTO;
import com.jp.eletrohub.exception.ResponseException;
import com.jp.eletrohub.model.entity.Produto;
import com.jp.eletrohub.service.ProdutoService;
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
@RequestMapping("/api/v1/produtos")
@RequiredArgsConstructor
@Tag(name = "Produto", description = "Gerenciamento de produtos")
@SecurityRequirement(name = "bearerAuth")
public class ProdutoController {

    private final ProdutoService service;

    @GetMapping
    @Operation(summary = "Listar produtos", description = "Retorna todos os produtos cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista retornada")
    public ResponseEntity<List<Produto>> list() {
        var produtos = service.list().stream().toList();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter produto", description = "Busca um produto pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    })
    public ResponseEntity<Produto> get(
            @Parameter(description = "ID do produto", required = true) @PathVariable("id") Long id) {
        var produto = service.findById(id);
        return produto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    @Operation(summary = "Criar produto", description = "Cadastra um novo produto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto criado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<?> post(
            @Parameter(description = "Dados do produto", required = true) @RequestBody ProdutoDTO dto) {
        try {

            var produto = service.saveOrCreate(null, dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(produto);
        } catch (Exception e) {
            return new ResponseException(e).toResponseEntity();
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualizar produto", description = "Atualiza um produto existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto atualizado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<?> update(
            @Parameter(description = "ID do produto", required = true) @PathVariable("id") Long id,
            @Parameter(description = "Dados do produto", required = true) @RequestBody ProdutoDTO dto) {
        try {
            var produto = service.saveOrCreate(id, dto);
            return ResponseEntity.ok(produto);
        } catch (Exception e) {
            return new ResponseException(e).toResponseEntity();
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Excluir produto", description = "Remove um produto pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto removido"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro ao remover")
    })
    public ResponseEntity<?> delete(
            @Parameter(description = "ID do produto", required = true) @PathVariable("id") Long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseException(e).toResponseEntity();
        }
    }
}
