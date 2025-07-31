package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.CategoriaDTO;
import com.jp.eletrohub.api.dto.ClienteDTO;
import com.jp.eletrohub.api.dto.GerenteDTO;
import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Categoria;
import com.jp.eletrohub.model.entity.Gerente;
import com.jp.eletrohub.service.GerenteService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/gerentes")
@RequiredArgsConstructor
public class GerenteController {

    private final GerenteService service;

    @GetMapping
    public ResponseEntity get() {
        List<Gerente> gerentes = service.getGerentes();
        return ResponseEntity.ok(gerentes.stream().map(GerenteDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Gerente> gerente = service.getGerenteById(id);
        if (!gerente.isPresent()) {
            return new ResponseEntity("Gerente não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(gerente.map(GerenteDTO::create));
    }

    @PostMapping
    public ResponseEntity post(GerenteDTO dto) {
        try {
            Gerente gerente = converter(dto);
            gerente = service.salvar(gerente);
            return new ResponseEntity(gerente, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, GerenteDTO dto) {
        if (!service.getGerenteById(id).isPresent()) {
            return new ResponseEntity("Gerente não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Gerente gerente = converter(dto);
            gerente.setId(id);
            service.salvar(gerente);
            return ResponseEntity.ok(gerente);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Gerente> gerente = service.getGerenteById(id);
        if (!gerente.isPresent()) {
            return new ResponseEntity("Gerente não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(gerente.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
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
