package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.VendaDTO;
import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Categoria;
import com.jp.eletrohub.model.entity.Cliente;
import com.jp.eletrohub.model.entity.Venda;
import com.jp.eletrohub.model.entity.Vendedor;
import com.jp.eletrohub.service.CategoriaService;
import com.jp.eletrohub.service.ClienteService;
import com.jp.eletrohub.service.VendaService;
import com.jp.eletrohub.service.VendedorService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/vendas")
@RequiredArgsConstructor
public class VendaController {

    private final @Nonnull VendaService service;
    private final @Nonnull CategoriaService categoriaService;
    private final @Nonnull ClienteService clienteService;
    private final @Nonnull VendedorService vendedorService;

    @GetMapping
    public ResponseEntity get() {
        List<Venda> vendas = service.getVendas();
        return ResponseEntity.ok(vendas);
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Venda> venda = service.getVendaById(id);
        if (venda.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(venda.get());
    }

    @PostMapping
    public ResponseEntity post(VendaDTO dto) {
        try {
            Venda venda = converter(dto);
            venda = service.salvar(venda);

            Categoria categoria = categoriaService.save(produto.getCategoria());
            categoria.setCategoria(categoria);

            return ResponseEntity.status(201).body(venda);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, VendaDTO dto) {
        if (!service.getVendaById(id).isPresent()) {
            return new ResponseEntity("Venda não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Venda venda = converter(dto);
            venda.setId(id);

            Cliente cliente = clienteService.save(venda.getCliente());
            venda.setCliente(cliente);

            Vendedor vendedor = vendedorService.salvar(venda.getVendedor());
            venda.setVendedor(vendedor);

            service.salvar(venda);
            return ResponseEntity.ok(venda);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Venda> venda = service.getVendaById(id);
        if (!venda.isPresent()) {
            return new ResponseEntity("Venda não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(venda.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Venda converter(VendaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Venda venda = modelMapper.map(dto, Venda.class);
        return venda;
    }
}
