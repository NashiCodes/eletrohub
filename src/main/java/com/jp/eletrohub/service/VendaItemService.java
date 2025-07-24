package com.jp.eletrohub.service;

import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.VendaItem;
import com.jp.eletrohub.model.repository.VendaItemRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VendaItemService {
    private final VendaItemRepository repository;

    public VendaItemService(VendaItemRepository repository) {
        this.repository = repository;
    }

    public List<VendaItem> getVendaItens() {
        return repository.findAll();
    }

    public Optional<VendaItem> getVendaItemById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public VendaItem salvar(VendaItem vendaItem) {
        validar(vendaItem);
        return repository.save(vendaItem);
    }

    @Transactional
    public void excluir(@Nonnull VendaItem vendaItem) {
        repository.delete(vendaItem);
    }

    public void validar(@Nonnull VendaItem vendaItem) {
        if (vendaItem.getProduto() == null) {
            throw new RegraNegocioException("Produto do item da venda é obrigatório");
        }
        if (vendaItem.getQuantidade() <= 0) {
            throw new RegraNegocioException("Quantidade inválida para o item da venda");
        }
    }
}
