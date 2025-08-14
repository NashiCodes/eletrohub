package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.cliente.ClienteDTO;
import com.jp.eletrohub.exception.ResponseException;
import com.jp.eletrohub.model.entity.Cliente;
import com.jp.eletrohub.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
@Tag(name = "Cliente", description = "Operações de gerenciamento de clientes")
public class ClienteController {
    private final ClienteService clienteService;

    @GetMapping
    @Operation(summary = "Listar clientes", description = "Retorna todos os clientes cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<Cliente>> list() {
        var clientes = clienteService.list().stream().toList();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter cliente", description = "Busca um cliente pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
    })
    public ResponseEntity<Cliente> get(
            @Parameter(description = "ID do cliente", required = true) @PathVariable("id") Long id) {
        var cliente = clienteService.findById(id);
        return cliente.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    @Operation(summary = "Criar cliente", description = "Cadastra um novo cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente criado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<?> create(
            @Parameter(description = "Dados do cliente", required = true) @Valid @RequestBody ClienteDTO dto) {
        try {
            var cliente = clienteService.saveOrCreate(null, dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
        } catch (Exception e) {
            return new ResponseException(e).toResponseEntity();
        }
    }


    @PutMapping("{id}")
    @Operation(summary = "Atualizar cliente", description = "Atualiza um cliente existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente atualizado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<?> update(
            @Parameter(description = "ID do cliente", required = true) @PathVariable("id") Long id,
            @Parameter(description = "Dados do cliente", required = true) @Valid @RequestBody ClienteDTO dto) {
        try {
            var cliente = clienteService.saveOrCreate(id, dto);
            return ResponseEntity.ok(cliente);
        } catch (Exception e) {
            return new ResponseException(e).toResponseEntity();
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Excluir cliente", description = "Remove um cliente pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente removido"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro ao remover")
    })
    public ResponseEntity<?> delete(
            @Parameter(description = "ID do cliente", required = true) @PathVariable("id") Long id) {
        try {
            clienteService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseException(e).toResponseEntity();
        }
    }
}
