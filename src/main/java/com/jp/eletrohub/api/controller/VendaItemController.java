package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.VendaDTO;
import com.jp.eletrohub.api.dto.VendaItemDTO;
import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.*;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/vendaitens")
@RequiredArgsConstructor
public class VendaItemController {

    private final VendaItemService service;
    private final VendaService vendaService;
    private final ProdutoService produtoService;

    @GetMapping
    public ResponseEntity get() {
        List<VendaItem> vendaItens = service.getVendaItens();
        return ResponseEntity.ok(vendaItens.stream().map(VendaItemDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<VendaItem> vendaItens = service.getVendaItemById(id);
        if (!vendaItens.isPresent()) {
            return new ResponseEntity("Venda Item não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(vendaItens.map(VendaItemDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody VendaItemDTO dto) {
        try {
            VendaItem vendaItem = converter(dto);
            vendaItem = service.salvar(vendaItem);
            return new ResponseEntity(vendaItem, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody VendaItemDTO dto) {
        if (!service.getVendaItemById(id).isPresent()) {
            return new ResponseEntity("Venda não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            VendaItem vendaItem = converter(dto);
            vendaItem.setId(id);
            service.salvar(vendaItem);
            return ResponseEntity.ok(vendaItem);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<VendaItem> vendaItem = service.getVendaItemById(id);
        if (!vendaItem.isPresent()) {
            return new ResponseEntity("Venda Item não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(vendaItem.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private VendaItem converter(VendaItemDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        VendaItem vendaItem = modelMapper.map(dto, VendaItem.class);

        if (dto.getIdVenda() != null) {
            Optional<Venda> venda = vendaService.getVendaById(dto.getIdVenda());
            if (!venda.isPresent()) {
                vendaItem.setVenda(null);
            } else {
                vendaItem.setVenda(venda.get());
            }
        }

        if (dto.getIdProduto() != null) {
            Optional<Produto> produto = produtoService.getProdutoById(dto.getIdProduto());
            if (!produto.isPresent()) {
                vendaItem.setProduto(null);
            } else {
                vendaItem.setProduto(produto.get());
            }
        }

        return vendaItem;
    }
}
