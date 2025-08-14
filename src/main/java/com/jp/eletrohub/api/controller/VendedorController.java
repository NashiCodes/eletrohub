package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.VendedorDTO;
import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Vendedor;
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
@RequestMapping("/api/v1/vendedores")
@RequiredArgsConstructor
@Tag(name = "Vendedor", description = "Gerenciamento de vendedores")
@SecurityRequirement(name = "bearerAuth")
public class VendedorController {

    private final VendedorService vendedorService;

    @GetMapping
    @Operation(summary = "Listar vendedores")
    @ApiResponse(responseCode = "200", description = "Lista retornada")
    public ResponseEntity<List<VendedorDTO>> get() {
        var vendedores = vendedorService.list().stream()
                .map(VendedorDTO::create)
                .toList();
        return ResponseEntity.ok(vendedores);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter vendedor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vendedor encontrado"),
            @ApiResponse(responseCode = "404", description = "Vendedor não encontrado", content = @Content)
    })
    public ResponseEntity<VendedorDTO> get(
            @Parameter(description = "ID do vendedor", required = true) @PathVariable("id") Long id) {
        var vendedor = vendedorService.findById(id)
                .map(VendedorDTO::create)
                .orElseThrow(() -> new RegraNegocioException("Vendedor não encontrado"));
        return ResponseEntity.ok(vendedor);
    }

    @PostMapping()
    @Operation(summary = "Criar vendedor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Vendedor criado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<Object> post(
            @Parameter(description = "Dados do vendedor", required = true) @RequestBody VendedorDTO dto) {
        try {
            Vendedor vendedor = convert(dto);
            vendedor = vendedorService.save(vendedor);
            return ResponseEntity.status(HttpStatus.CREATED).body(VendedorDTO.create(vendedor));
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualizar vendedor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vendedor atualizado"),
            @ApiResponse(responseCode = "404", description = "Vendedor não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<Object> update(
            @Parameter(description = "ID do vendedor", required = true) @PathVariable("id") Long id,
            @Parameter(description = "Dados do vendedor", required = true) @RequestBody VendedorDTO dto) {
        if (vendedorService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            Vendedor vendedor = convert(dto);
            vendedor.setId(id);
            vendedorService.save(vendedor);
            return ResponseEntity.ok(vendedor);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Excluir vendedor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vendedor removido"),
            @ApiResponse(responseCode = "404", description = "Vendedor não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro ao remover")
    })
    public ResponseEntity<Object> delete(
            @Parameter(description = "ID do vendedor", required = true) @PathVariable("id") Long id) {
        var vendedor = vendedorService.findById(id);
        if (vendedor.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            vendedorService.delete(vendedor.get());
            return ResponseEntity.ok().build();
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Vendedor convert(VendedorDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Vendedor.class);
    }
}
