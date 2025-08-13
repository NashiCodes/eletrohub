package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.GerenteDTO;
import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Gerente;
import com.jp.eletrohub.service.GerenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/gerentes")
@RequiredArgsConstructor
public class GerenteController {

    private final GerenteService gerenteService;

    @GetMapping
    public ResponseEntity<List<GerenteDTO>> get() {
        var gerentes = gerenteService.list().stream().map(GerenteDTO::create).toList();
        return ResponseEntity.ok(gerentes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GerenteDTO> get(@PathVariable("id") Long id) {
        var gerente = gerenteService.findById(id).map(GerenteDTO::create).orElseThrow(() -> new RegraNegocioException("Gerente não encontrado"));
        return ResponseEntity.ok(gerente);
    }

    @PostMapping()
    public ResponseEntity<Object> post(@RequestBody GerenteDTO dto) {
        try {
            Gerente gerente = GerenteDTO.toEntity(dto);
            gerente = gerenteService.salvar(gerente);
            return ResponseEntity.status(HttpStatus.CREATED).body(GerenteDTO.create(gerente));
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody GerenteDTO dto) {
        if (gerenteService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            Gerente gerente = GerenteDTO.toEntity(dto);
            gerente.setId(id);
            gerenteService.salvar(gerente);
            return ResponseEntity.ok(gerente);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        var gerente = gerenteService.findById(id);
        if (gerente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            gerenteService.delete(gerente.get());
            return ResponseEntity.ok().build();
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
