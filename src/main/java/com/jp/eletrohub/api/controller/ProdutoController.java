package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.ProdutoDTO;
import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Categoria;
import com.jp.eletrohub.model.entity.Produto;
import com.jp.eletrohub.service.CategoriaService;
import com.jp.eletrohub.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService service;
    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity get() {
        List<Produto> produtos = service.getProdutos();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Produto> produto = service.getProdutoById(id);
        if (produto.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(produto.get());
    }

    @PostMapping
    public ResponseEntity post(@RequestBody ProdutoDTO dto) {
        try {
            Produto produto = converter(dto);
            produto = service.salvar(produto);
            return ResponseEntity.status(201).body(produto);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, ProdutoDTO dto) {
        if (!service.getProdutoById(id).isPresent()) {
            return new ResponseEntity("Produto não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Produto produto = converter(dto);
            produto.setId(id);
            Categoria categoria = categoriaService.save(produto.getCategoria());
            produto.setCategoria(categoria);
            service.salvar(produto);
            return ResponseEntity.ok(produto);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Produto> produto = service.getProdutoById(id);
        if (!produto.isPresent()) {
            return new ResponseEntity("Produto não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(produto.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    private Produto converter(ProdutoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Produto produto = modelMapper.map(dto, Produto.class);
        Categoria categoria = modelMapper.map(dto, Categoria.class);
        produto.setCategoria(categoria);
        return produto;
    }
}
