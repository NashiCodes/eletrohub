package com.jp.eletrohub.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Produto;
import com.jp.eletrohub.model.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoService {
    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public List<Produto> getProdutos() {
        return repository.findAll();
    }

    public Optional<Produto> getProdutoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Produto salvar(Produto produto) {
        validar(produto);
        return repository.save(produto);
    }

    @Transactional
    public void excluir(Produto produto) {
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