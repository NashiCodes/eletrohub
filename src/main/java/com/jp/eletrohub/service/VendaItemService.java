package com.jp.eletrohub.service;

import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.VendaItem;
import com.jp.eletrohub.model.repository.VendaItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VendaItemService {
    private final VendaItemRepository vendaItemRepository;

    public List<VendaItem> list() {
        return vendaItemRepository.findAll();
    }

    public Optional<VendaItem> findById(Long id) {
        return vendaItemRepository.findById(id);
    }

    @Transactional
    public VendaItem save(VendaItem vendaItem) {
        validate(vendaItem);
        return vendaItemRepository.save(vendaItem);
    }

    @Transactional
    public void delete(VendaItem vendaItem) {
        Objects.requireNonNull(vendaItem.getId());
        vendaItemRepository.delete(vendaItem);
    }

    public void validate(VendaItem vendaItem) {

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
