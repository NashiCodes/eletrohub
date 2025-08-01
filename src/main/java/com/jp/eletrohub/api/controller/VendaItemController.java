package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.VendaItemDTO;
import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Produto;
import com.jp.eletrohub.model.entity.Venda;
import com.jp.eletrohub.model.entity.VendaItem;
import com.jp.eletrohub.service.ProdutoService;
import com.jp.eletrohub.service.VendaItemService;
import com.jp.eletrohub.service.VendaService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/vendaitens")
@RequiredArgsConstructor
public class VendaItemController {

    private final VendaItemService vendaItemService;
    private final VendaService vendaService;
    private final ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<List<VendaItemDTO>> list() {
        var vendaItens = vendaItemService.list().stream().map(VendaItemDTO::create).toList();
        return ResponseEntity.ok(vendaItens);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendaItemDTO> get(@PathVariable("id") Long id) {
        var vendaItens =
                vendaItemService.findById(id).map(VendaItemDTO::create).orElseThrow(() -> new RegraNegocioException("Venda Item não encontrado"));
        return ResponseEntity.ok(vendaItens);
    }

    @PostMapping()
    public ResponseEntity<Object> post(@RequestBody VendaItemDTO dto) {
        try {
            VendaItem vendaItem = convert(dto);
            vendaItem = vendaItemService.save(vendaItem);
            return ResponseEntity.status(HttpStatus.CREATED).body(VendaItemDTO.create(vendaItem));
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody VendaItemDTO dto) {
        if (vendaItemService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            VendaItem vendaItem = convert(dto);
            vendaItem.setId(id);
            vendaItemService.save(vendaItem);
            return ResponseEntity.ok(vendaItem);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        var vendaItem = vendaItemService.findById(id);
        if (vendaItem.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            vendaItemService.delete(vendaItem.get());
            return ResponseEntity.ok().build();
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private VendaItem convert(VendaItemDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        VendaItem vendaItem = modelMapper.map(dto, VendaItem.class);

        if (dto.getIdVenda() != null) {
            Optional<Venda> venda = vendaService.getVendaById(dto.getIdVenda());
            if (venda.isEmpty()) {
                vendaItem.setVenda(null);
            } else {
                vendaItem.setVenda(venda.get());
            }
        }

        if (dto.getIdProduto() != null) {
            Optional<Produto> produto = produtoService.findById(dto.getIdProduto());
            if (produto.isEmpty()) {
                vendaItem.setProduto(null);
            } else {
                vendaItem.setProduto(produto.get());
            }
        }

        return vendaItem;
    }
}
