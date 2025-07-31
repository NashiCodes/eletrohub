package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.CategoriaDTO;
import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Categoria;
import com.jp.eletrohub.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService service;

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> get() {
        var categorias = service.list();
        return categorias.isEmpty() ?
                ResponseEntity.notFound().build() : ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> get(@PathVariable("id") Long id) {
        var categoria = service.findById(id);
        return categoria
                .map(value -> ResponseEntity.ok(CategoriaDTO.create(value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Object> post(@RequestBody CategoriaDTO dto) {
        try {
            Categoria categoria = service.save(dto);
            return ResponseEntity.status(201).body(categoria);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, CategoriaDTO dto) {
        if (!service.findById(id).isPresent()) {
            return new ResponseEntity("Categoria não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Categoria categoria = dto.toEntity();
            categoria.setId(id);
            service.save(categoria);
            return ResponseEntity.ok(categoria);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Categoria> categoria = service.findById(id);
        if (!categoria.isPresent()) {
            return new ResponseEntity("Aluno não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.delete(categoria.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
