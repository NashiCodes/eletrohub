package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.GerenteDTO;
import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Gerente;
import com.jp.eletrohub.service.GerenteService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/gerentes")
@RequiredArgsConstructor
public class GerenteController {

    private final @Nonnull GerenteService service;

    @GetMapping
    public ResponseEntity get() {
        List<Gerente> gerentes = service.getGerentes();
        return ResponseEntity.ok(gerentes);
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Gerente> gerente = service.getGerenteById(id);
        if (!gerente.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(gerente.get());
    }

    @PostMapping
    public ResponseEntity post(@RequestBody GerenteDTO dto) {
        try {
            Gerente gerente = converter(dto);
            gerente = service.salvar(gerente);
            return ResponseEntity.status(201).body(gerente);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Gerente converter(GerenteDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Gerente gerente = modelMapper.map(dto, Gerente.class);
        return gerente;
    }
}
