package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.venda.VendaItemDTO;
import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.exception.ResponseException;
import com.jp.eletrohub.model.entity.VendaItem;
import com.jp.eletrohub.service.VendaItemService;
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
@RequestMapping("/api/v1/vendaitens")
@RequiredArgsConstructor
@Tag(name = "Venda Item", description = "Gerenciamento de itens de venda")
@SecurityRequirement(name = "bearerAuth")
public class VendaItemController {

    private final VendaItemService vendaItemService;

    @GetMapping
    @Operation(summary = "Listar itens de venda")
    @ApiResponse(responseCode = "200", description = "Lista retornada")
    public ResponseEntity<List<VendaItem>> list() {
        var vendaItens = vendaItemService.list().stream().toList();
        return ResponseEntity.ok(vendaItens);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter item de venda")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item encontrado"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado", content = @Content)
    })
    public ResponseEntity<VendaItem> get(@Parameter(description = "ID do item", required = true) @PathVariable("id") Long id) {
        var vendaItens = vendaItemService.findById(id);
        return vendaItens.map(ResponseEntity::ok).orElseThrow(() -> new RegraNegocioException("Item não encontrado"));
    }

    @PostMapping()
    @Operation(summary = "Criar item de venda")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Item criado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<?> post(@Parameter(description = "Dados do item", required = true) @Valid @RequestBody VendaItemDTO dto) {
        try {
            var vendaItem = vendaItemService.saveOrCreate(null, dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(VendaItemDTO.create(vendaItem));
        } catch (Exception e) {
            return new ResponseException(e).toResponseEntity();
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualizar item de venda")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item atualizado"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<?> update(@Parameter(description = "ID do item", required = true) @PathVariable("id") Long id,
                                    @Parameter(description = "Dados do item", required = true) @Valid @RequestBody VendaItemDTO dto) {
        try {
            VendaItem vendaItem = vendaItemService.saveOrCreate(id, dto);
            return ResponseEntity.ok(vendaItem);
        } catch (Exception e) {
            return new ResponseException(e).toResponseEntity();
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Excluir item de venda")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item removido"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro ao remover")
    })
    public ResponseEntity<?> delete(@Parameter(description = "ID do item", required = true) @PathVariable("id") Long id) {
        try {
            vendaItemService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseException(e).toResponseEntity();
        }
    }

}
