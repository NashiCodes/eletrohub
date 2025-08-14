package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.VendaItemDTO;
import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Produto;
import com.jp.eletrohub.model.entity.Venda;
import com.jp.eletrohub.model.entity.VendaItem;
import com.jp.eletrohub.service.ProdutoService;
import com.jp.eletrohub.service.VendaItemService;
import com.jp.eletrohub.service.VendaService;
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
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/vendaitens")
@RequiredArgsConstructor
@Tag(name = "Venda Item", description = "Gerenciamento de itens de venda")
@SecurityRequirement(name = "bearerAuth")
public class VendaItemController {

    private final VendaItemService vendaItemService;
    private final VendaService vendaService;
    private final ProdutoService produtoService;

    @GetMapping
    @Operation(summary = "Listar itens de venda")
    @ApiResponse(responseCode = "200", description = "Lista retornada")
    public ResponseEntity<List<VendaItemDTO>> list() {
        var vendaItens = vendaItemService.list().stream().map(VendaItemDTO::create).toList();
        return ResponseEntity.ok(vendaItens);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter item de venda")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item encontrado"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado", content = @Content)
    })
    public ResponseEntity<VendaItemDTO> get(@Parameter(description = "ID do item", required = true) @PathVariable("id") Long id) {
        var vendaItens =
                vendaItemService.findById(id).map(VendaItemDTO::create).orElseThrow(() -> new RegraNegocioException("Venda Item não encontrado"));
        return ResponseEntity.ok(vendaItens);
    }

    @PostMapping()
    @Operation(summary = "Criar item de venda")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Item criado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<Object> post(@Parameter(description = "Dados do item", required = true) @RequestBody VendaItemDTO dto) {
        try {
            VendaItem vendaItem = convert(dto);
            vendaItem = vendaItemService.save(vendaItem);
            return ResponseEntity.status(HttpStatus.CREATED).body(VendaItemDTO.create(vendaItem));
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualizar item de venda")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item atualizado"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<Object> update(@Parameter(description = "ID do item", required = true) @PathVariable("id") Long id, @Parameter(description = "Dados do item", required = true) @RequestBody VendaItemDTO dto) {
        if (vendaItemService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            VendaItem vendaItem = convert(dto);
            vendaItem.setId(id);
            vendaItemService.save(vendaItem);
            return ResponseEntity.ok(vendaItem);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Excluir item de venda")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item removido"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro ao remover")
    })
    public ResponseEntity<Object> delete(@Parameter(description = "ID do item", required = true) @PathVariable("id") Long id) {
        var vendaItem = vendaItemService.findById(id);
        if (vendaItem.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            vendaItemService.delete(vendaItem.get());
            return ResponseEntity.ok().build();
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private VendaItem convert(VendaItemDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        VendaItem vendaItem = modelMapper.map(dto, VendaItem.class);

        if (dto.getIdVenda() != null) {
            Optional<Venda> venda = vendaService.getVendaById(dto.getIdVenda());
            vendaItem.setVenda(venda.orElse(null));
        }

        if (dto.getIdProduto() != null) {
            Optional<Produto> produto = produtoService.findById(dto.getIdProduto());
            vendaItem.setProduto(produto.orElse(null));
        }

        return vendaItem;
    }
}
