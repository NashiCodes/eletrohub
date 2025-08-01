package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.VendedorDTO;
import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Vendedor;
import com.jp.eletrohub.service.VendedorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vendedores")
@RequiredArgsConstructor
public class VendedorController {

    private final VendedorService vendedorService;

    @GetMapping
    public ResponseEntity<List<VendedorDTO>> get() {
        var vendedores = vendedorService.list().stream()
                .map(VendedorDTO::create)
                .toList();
        return ResponseEntity.ok(vendedores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendedorDTO> get(@PathVariable("id") Long id) {
        var vendedor = vendedorService.findById(id)
                .map(VendedorDTO::create)
                .orElseThrow(() -> new RegraNegocioException("Vendedor não encontrado"));
        return ResponseEntity.ok(vendedor);
    }

    @PostMapping()
    public ResponseEntity<Object> post(@RequestBody VendedorDTO dto) {
        try {
            Vendedor vendedor = convert(dto);
            vendedor = vendedorService.save(vendedor);
            return ResponseEntity.status(HttpStatus.CREATED).body(VendedorDTO.create(vendedor));
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody VendedorDTO dto) {
        if (vendedorService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            Vendedor vendedor = convert(dto);
            vendedor.setId(id);
            vendedorService.save(vendedor);
            return ResponseEntity.ok(vendedor);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        var vendedor = vendedorService.findById(id);
        if (vendedor.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            vendedorService.delete(vendedor.get());
            return ResponseEntity.ok().build();
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Vendedor convert(VendedorDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Vendedor.class);
    }
}
