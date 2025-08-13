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

import static com.jp.eletrohub.api.dto.CategoriaDTO.toEntity;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> list() {
        return ResponseEntity.ok(categoriaService.list().stream().map(CategoriaDTO::create).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> list(@PathVariable("id") Long id) {
        var categoria = categoriaService.findById(id)
                .map(CategoriaDTO::create);
        return categoria.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Object> post(@RequestBody CategoriaDTO dto) {
        try {
            Categoria categoria = toEntity(dto);
            categoria = categoriaService.save(categoria);
            return ResponseEntity.status(HttpStatus.CREATED).body(categoria);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody CategoriaDTO dto) {
        if (categoriaService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            Categoria categoria = toEntity(dto);
            categoria.setId(id);
            categoriaService.save(categoria);
            return ResponseEntity.ok(categoria);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        var categoria = categoriaService.findById(id);
        if (categoria.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {

            categoriaService.delete(categoria.get());
            return ResponseEntity.noContent().build();
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
