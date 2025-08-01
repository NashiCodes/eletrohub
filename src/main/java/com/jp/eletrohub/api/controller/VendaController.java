package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.VendaDTO;
import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Venda;
import com.jp.eletrohub.service.ClienteService;
import com.jp.eletrohub.service.VendaService;
import com.jp.eletrohub.service.VendedorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vendas")
@RequiredArgsConstructor
public class VendaController {

    private final VendaService vendaService;
    private final ClienteService clienteService;
    private final VendedorService vendedorService;

    @GetMapping
    public ResponseEntity<List<VendaDTO>> get() {
        var vendas = vendaService.getVendas().stream().map(VendaDTO::create).toList();
        return ResponseEntity.ok(vendas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendaDTO> get(@PathVariable("id") Long id) {
        var venda = vendaService.getVendaById(id).map(VendaDTO::create).orElseThrow(() -> new RegraNegocioException("Venda não encontrada"));
        return ResponseEntity.ok(venda);
    }

    @PostMapping()
    public ResponseEntity<Object> post(@RequestBody VendaDTO dto) {
        try {
            Venda venda = converter(dto);
            venda = vendaService.salvar(venda);
            return ResponseEntity.status(HttpStatus.CREATED).body(VendaDTO.create(venda));
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody VendaDTO dto) {
        if (vendaService.getVendaById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            Venda venda = converter(dto);
            venda.setId(id);
            vendaService.salvar(venda);
            return ResponseEntity.ok(venda);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        var venda = vendaService.getVendaById(id);
        if (venda.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            vendaService.delete(venda.get());
            return ResponseEntity.ok().build();
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Venda converter(VendaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Venda venda = modelMapper.map(dto, Venda.class);

        if (dto.getIdVendedor() != null) {
            var vendedor = vendedorService.findById(dto.getIdVendedor());
            if (vendedor.isEmpty()) {
                venda.setVendedor(null);
            } else {
                venda.setVendedor(vendedor.get());
            }
        }

        if (dto.getIdCliente() != null) {
            var cliente = clienteService.findById(dto.getIdCliente());
            if (cliente.isEmpty()) {
                venda.setCliente(null);
            } else {
                venda.setCliente(cliente.get());
            }
        }

        return venda;
    }
}
