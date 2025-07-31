package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.VendedorDTO;
import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Vendedor;
import com.jp.eletrohub.service.VendedorService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/vendedores")
@RequiredArgsConstructor
public class VendedorController {

    private final VendedorService service;

    @GetMapping
    public ResponseEntity get() {
        List<Vendedor> vendedores = service.getVendedores();
        return ResponseEntity.ok(vendedores);
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Vendedor> vendedor = service.getVendedorById(id);
        if (vendedor.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vendedor.get());
    }

    @PostMapping
    public ResponseEntity post(@RequestBody VendedorDTO dto) {
        try {
            Vendedor vendedor = converter(dto);
            vendedor = service.salvar(vendedor);
            return ResponseEntity.status(201).body(vendedor);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, VendedorDTO dto) {
        if (!service.getVendedorById(id).isPresent()) {
            return new ResponseEntity("Vendedor não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Vendedor vendedor = converter(dto);
            vendedor.setId(id);
            service.salvar(vendedor);
            return ResponseEntity.ok(vendedor);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Vendedor> vendedor = service.getVendedorById(id);
        if (!vendedor.isPresent()) {
            return new ResponseEntity("Vendedor não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(vendedor.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Vendedor converter(VendedorDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Vendedor vendedor = modelMapper.map(dto, Vendedor.class);
        return vendedor;
    }
}
