package com.jp.eletrohub.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.VendaItem;
import com.jp.eletrohub.model.repository.VendaItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void excluir(VendaItem vendaItem) {
        Objects.requireNonNull(vendaItem.getId());
        repository.delete(vendaItem);
    }

    public void validar(VendaItem vendaItem) {

        if (vendaItem.getValor() == null || vendaItem.getValor() <= 0) {
            throw new RegraNegocioException("Valor do item inválido");
        }

        if (vendaItem.getQuantidade() == null || vendaItem.getValor() < 0) {
            throw new RegraNegocioException("Quantidade do item inválida");
        }

        if (vendaItem.getVenda() == null || vendaItem.getVenda().getId() == null || vendaItem.getVenda().getId() == 0) {
            throw new RegraNegocioException("Venda inválida");
        }

        if (vendaItem.getProduto() == null || vendaItem.getProduto().getId() == null || vendaItem.getProduto().getId() == 0) {
            throw new RegraNegocioException("Produto inválido");
        }
    }
}
