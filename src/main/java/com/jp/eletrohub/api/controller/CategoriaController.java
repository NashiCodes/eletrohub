package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.CategoriaDTO;
import com.jp.eletrohub.api.dto.ProdutoDTO;
import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Categoria;
import com.jp.eletrohub.model.entity.Produto;
import com.jp.eletrohub.service.CategoriaService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity get() {
        List<Categoria> categorias = service.getCategorias();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Categoria> categoria = service.getCategoriaById(id);
        if (categoria.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categoria.get());
    }

    @PostMapping
    public ResponseEntity post(@RequestBody CategoriaDTO dto) {
        try {
            Categoria categoria = converter(dto);
            categoria = service.salvar(categoria);
            return ResponseEntity.status(201).body(categoria);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Categoria converter(CategoriaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Categoria categoria = modelMapper.map(dto, Categoria.class);
        return categoria;
    }
}
