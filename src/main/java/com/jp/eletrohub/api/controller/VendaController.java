package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.ClienteDTO;
import com.jp.eletrohub.api.dto.GerenteDTO;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/vendas")
@RequiredArgsConstructor
public class VendaController {

    private final VendaService service;
    private final ClienteService clienteService;
    private final VendedorService vendedorService;

    @GetMapping
    public ResponseEntity get() {
        List<Venda> vendas = service.getVendas();
        return ResponseEntity.ok(vendas.stream().map(VendaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Venda> venda = service.getVendaById(id);
        if (!venda.isPresent()) {
            return new ResponseEntity("Venda não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(venda.map(VendaDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(VendaDTO dto) {
        try {
            Venda venda = converter(dto);
            venda = service.salvar(venda);
            return new ResponseEntity(venda, HttpStatus.CREATED);
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

        if (dto.getIdVendedor() != null) {
            Optional<Vendedor> vendedor = vendedorService.getVendedorById(dto.getIdVendedor());
            if (!vendedor.isPresent()) {
                venda.setVendedor(null);
            } else {
                venda.setVendedor(vendedor.get());
            }
        }

        if (dto.getIdCliente() != null) {
            Optional<Cliente> cliente = clienteService.getClienteById(dto.getIdCliente());
            if (!cliente.isPresent()) {
                venda.setCliente(null);
            } else {
                venda.setCliente(cliente.get());
            }
        }

        return venda;
    }
}
