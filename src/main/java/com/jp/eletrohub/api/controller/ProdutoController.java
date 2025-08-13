package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.ProdutoDTO;
import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Produto;
import com.jp.eletrohub.service.CategoriaService;
import com.jp.eletrohub.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService service;
    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> list() {
        var produtos = service.list().stream().map(ProdutoDTO::create).toList();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> get(@PathVariable("id") Long id) {
        var produto = service.findById(id).map(ProdutoDTO::create).orElseThrow(() -> new RegraNegocioException("Produto não encontrado"));
        return ResponseEntity.ok(produto);
    }

    @PostMapping()
    public ResponseEntity<Object> post(@RequestBody ProdutoDTO dto) {
        try {
            Produto produto = converter(dto);
            produto = service.save(produto);
            return ResponseEntity.status(HttpStatus.CREATED).body(ProdutoDTO.create(produto));
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody ProdutoDTO dto) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            Produto produto = converter(dto);
            produto.setId(id);
            service.save(produto);
            return ResponseEntity.ok(produto);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        var produto = service.findById(id);
        if (produto.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            service.delete(produto.get());
            return ResponseEntity.ok().build();
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Produto converter(ProdutoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Produto produto = modelMapper.map(dto, Produto.class);

        if (dto.getIdCategoria() != null) {
            var categoria = categoriaService.findById(dto.getIdCategoria());
            if (categoria.isEmpty()) {
                produto.setCategoria(null);
            } else {
                produto.setCategoria(categoria.get());
            }
        }

        return produto;
    }
}
