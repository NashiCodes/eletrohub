package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.ProdutoDTO;
import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Produto;
import com.jp.eletrohub.service.CategoriaService;
import com.jp.eletrohub.service.ProdutoService;
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
@RequestMapping("/api/v1/produtos")
@RequiredArgsConstructor
@Tag(name = "Produto", description = "Gerenciamento de produtos")
@SecurityRequirement(name = "bearerAuth")
public class ProdutoController {

    private final ProdutoService service;
    private final CategoriaService categoriaService;

    @GetMapping
    @Operation(summary = "Listar produtos", description = "Retorna todos os produtos cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista retornada")
    public ResponseEntity<List<ProdutoDTO>> list() {
        var produtos = service.list().stream().map(ProdutoDTO::create).toList();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter produto", description = "Busca um produto pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    })
    public ResponseEntity<ProdutoDTO> get(
            @Parameter(description = "ID do produto", required = true) @PathVariable("id") Long id) {
        var produto = service.findById(id).map(ProdutoDTO::create).orElseThrow(() -> new RegraNegocioException("Produto não encontrado"));
        return ResponseEntity.ok(produto);
    }

    @PostMapping()
    @Operation(summary = "Criar produto", description = "Cadastra um novo produto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto criado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<Object> post(
            @Parameter(description = "Dados do produto", required = true) @RequestBody ProdutoDTO dto) {
        try {
            Produto produto = converter(dto);
            produto = service.save(produto);
            return ResponseEntity.status(HttpStatus.CREATED).body(ProdutoDTO.create(produto));
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualizar produto", description = "Atualiza um produto existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto atualizado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<Object> update(
            @Parameter(description = "ID do produto", required = true) @PathVariable("id") Long id,
            @Parameter(description = "Dados do produto", required = true) @RequestBody ProdutoDTO dto) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            Produto produto = converter(dto);
            produto.setId(id);
            service.save(produto);
            return ResponseEntity.ok(produto);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Excluir produto", description = "Remove um produto pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto removido"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro ao remover")
    })
    public ResponseEntity<Object> delete(
            @Parameter(description = "ID do produto", required = true) @PathVariable("id") Long id) {
        var produto = service.findById(id);
        if (produto.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            service.delete(produto.get());
            return ResponseEntity.ok().build();
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Produto converter(ProdutoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Produto produto = modelMapper.map(dto, Produto.class);
        if (dto.getIdCategoria() != null) {
            var categoria = categoriaService.findById(dto.getIdCategoria());
            produto.setCategoria(categoria.orElse(null));
        }
        return produto;
    }
}
