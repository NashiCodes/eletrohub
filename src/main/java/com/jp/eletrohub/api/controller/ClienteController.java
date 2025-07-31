package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.ClienteDTO;
import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Cliente;
import com.jp.eletrohub.model.entity.Gerente;
import com.jp.eletrohub.service.ClienteService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {
    private final @Nonnull ClienteService clienteService;

    @GetMapping
    @Transactional
    public ResponseEntity<List<ClienteDTO>> get() {
        var clientes = clienteService.list();
        return clientes.isEmpty() ?
                ResponseEntity.status(HttpStatus.NO_CONTENT).build() :
                ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> get(@PathVariable("id") Long id) {
        var cliente = clienteService.findById(id);
        return cliente
                .map(value -> ResponseEntity.ok(ClienteDTO.create(value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity post(ClienteDTO dto) {
        try {
            Gerente gerente = converter(dto);
            gerente = service.salvar(gerente);
            return ResponseEntity.status(201).body(dto);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("{id}")
    public ResponseEntity<Object> atualizar(@PathVariable("id") Long id, Cliente cliente) {
        if (clienteService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            cliente.setId(id);
            var dto = ClienteDTO.create(clienteService.save(cliente));
            return ResponseEntity.ok(dto);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> excluir(@PathVariable("id") Long id) {
        var cliente = clienteService.findById(id);
        if (cliente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            clienteService.excluir(cliente.get());
            return ResponseEntity.noContent().build();
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
