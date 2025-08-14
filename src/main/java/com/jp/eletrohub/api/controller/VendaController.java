package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.VendaDTO;
import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Venda;
import com.jp.eletrohub.service.ClienteService;
import com.jp.eletrohub.service.VendaService;
import com.jp.eletrohub.service.VendedorService;
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
@RequestMapping("/api/v1/vendas")
@RequiredArgsConstructor
@Tag(name = "Venda", description = "Gerenciamento de vendas")
@SecurityRequirement(name = "bearerAuth")
public class VendaController {

    private final VendaService vendaService;
    private final ClienteService clienteService;
    private final VendedorService vendedorService;

    @GetMapping
    @Operation(summary = "Listar vendas")
    @ApiResponse(responseCode = "200", description = "Lista retornada")
    public ResponseEntity<List<VendaDTO>> get() {
        var vendas = vendaService.getVendas().stream().map(VendaDTO::create).toList();
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
        var venda = vendaService.getVendaById(id).map(VendaDTO::create).orElseThrow(() -> new RegraNegocioException("Venda não encontrada"));
        return ResponseEntity.ok(venda);
    }

    @PostMapping()
    @Operation(summary = "Criar venda")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Venda criada"),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<Object> post(
            @Parameter(description = "Dados da venda", required = true) @RequestBody VendaDTO dto) {
        try {
            Venda venda = converter(dto);
            venda = vendaService.salvar(venda);
            return ResponseEntity.status(HttpStatus.CREATED).body(VendaDTO.create(venda));
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualizar venda")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venda atualizada"),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<Object> update(
            @Parameter(description = "ID da venda", required = true) @PathVariable("id") Long id,
            @Parameter(description = "Dados da venda", required = true) @RequestBody VendaDTO dto) {
        if (vendaService.getVendaById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            Venda venda = converter(dto);
            venda.setId(id);
            vendaService.salvar(venda);
            return ResponseEntity.ok(venda);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Excluir venda")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venda removida"),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro ao remover")
    })
    public ResponseEntity<Object> delete(
            @Parameter(description = "ID da venda", required = true) @PathVariable("id") Long id) {
        var venda = vendaService.getVendaById(id);
        if (venda.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            vendaService.delete(venda.get());
            return ResponseEntity.ok().build();
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Venda converter(VendaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Venda venda = modelMapper.map(dto, Venda.class);
        if (dto.getIdVendedor() != null) {
            var vendedor = vendedorService.findById(dto.getIdVendedor());
            venda.setVendedor(vendedor.orElse(null));
        }
        if (dto.getIdCliente() != null) {
            var cliente = clienteService.findById(dto.getIdCliente());
            venda.setCliente(cliente.orElse(null));
        }
        return venda;
    }
}
