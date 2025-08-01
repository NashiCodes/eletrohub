package com.jp.eletrohub.service;

import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Produto;
import com.jp.eletrohub.model.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProdutoService {
    private final ProdutoRepository repository;

    public List<Produto> list() {
        return repository.findAll();
    }

    public Optional<Produto> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Produto save(Produto produto) {
        validar(produto);
        return repository.save(produto);
    }

    @Transactional
    public void delete(Produto produto) {
        Objects.requireNonNull(produto.getId());
        repository.delete(produto);
    }

    public void validar(Produto produto) {
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("Nome do produto inválido");
        }

        if (produto.getValor() == null || produto.getValor() <= 0) {
            throw new RegraNegocioException("Valor do produto inválido");
        }

        if (produto.getQuantidade() == null || produto.getValor() < 0) {
            throw new RegraNegocioException("Quantidade do produto inválida");
        }

        if (produto.getCategoria() == null || produto.getCategoria().getId() == null || produto.getCategoria().getId() == 0) {
            throw new RegraNegocioException("Categoria inválida");
        }
    }
}