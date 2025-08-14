package com.jp.eletrohub.service;

import com.jp.eletrohub.api.dto.venda.VendaItemDTO;
import com.jp.eletrohub.exception.NotFound;
import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.VendaItem;
import com.jp.eletrohub.model.repository.VendaItemRepository;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VendaItemService {
    private final VendaItemRepository vendaItemRepository;
    private final ProdutoService produtoService;
    private final VendaService vendaService;

    public List<VendaItem> list() {
        return vendaItemRepository.findAll();
    }

    public Optional<VendaItem> findById(Long id) {
        return vendaItemRepository.findById(id);
    }

    @Transactional
    public VendaItem saveOrCreate(@Nullable Long id, @NotNull VendaItemDTO dto) {
        validate(dto);

        var idVenda = dto.getIdVenda();
        var idProduto = dto.getIdProduto();
        var venda = vendaService.findById(idVenda)
                .orElseThrow(() -> new NotFound("Venda não encontrada com o ID: " + idVenda));
        var produto = produtoService.findById(idProduto)
                .orElseThrow(() -> new NotFound("Produto não encontrado com o ID: " + idProduto));

        VendaItem vendaItem;

        if (id == null) {
            vendaItem = new VendaItem();
        } else {
            vendaItem = vendaItemRepository.findById(id)
                    .orElseThrow(() -> new RegraNegocioException("Venda Item não encontrado com o ID: " + id));
        }

        vendaItem.setValor(dto.getValor());
        vendaItem.setQuantidade(dto.getQuantidade());
        vendaItem.setVenda(venda);
        vendaItem.setProduto(produto);

        return vendaItemRepository.save(vendaItem);
    }

    @Transactional
    public void delete(@NotNull Long id) {
        VendaItem vendaItem = vendaItemRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Venda Item não encontrado com o ID: " + id));
        vendaItemRepository.delete(vendaItem);
    }

    public void validate(@NotNull VendaItemDTO dto) {
        if (dto.getQuantidade() < 0) {
            throw new RegraNegocioException("Quantidade deve ser maior que zero");
        }
        if (dto.getValor() < 0) {
            throw new RegraNegocioException("Valor deve ser maior que zero");
        }
    }
}
