package com.jp.eletrohub.service;

import com.jp.eletrohub.api.dto.venda.ProdutoDTO;
import com.jp.eletrohub.exception.NotFound;
import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Produto;
import com.jp.eletrohub.model.repository.ProdutoRepository;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private final CategoriaService categoriaService;

    public List<Produto> list() {
        return repository.findAll();
    }

    public Optional<Produto> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Produto saveOrCreate(@Nullable Long id, @NotNull ProdutoDTO dto) {
        var categoria = categoriaService.findById(dto.getIdCategoria())
                .orElseThrow(() -> new NotFound("Categoria não encontrada com o ID: " + dto.getIdCategoria()));
        if (Objects.isNull(id)) {
            validar(dto);
            return repository.save(new Produto(dto.getNome(), dto.getValor(), dto.getQuantidade(), categoria));
        }
        var existingProduto = repository.findById(id)
                .orElseThrow(() -> new NotFound("Produto não encontrado com o ID: " + id));
        existingProduto.setNome(dto.getNome());
        existingProduto.setValor(dto.getValor());
        existingProduto.setQuantidade(dto.getQuantidade());
        existingProduto.setCategoria(categoria);
        return repository.save(existingProduto);
    }

    @Transactional
    public void delete(@NotBlank Long id) {
        var produto = repository.findById(id)
                .orElseThrow(() -> new NotFound("Produto não encontrado com o ID: " + id));
        repository.delete(produto);
    }

    public void validar(ProdutoDTO produto) {
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("Nome do produto inválido");
        }

        if (produto.getValor() <= 0) {
            throw new RegraNegocioException("Valor do produto inválido");
        }

        if (produto.getQuantidade() < 1) {
            throw new RegraNegocioException("Quantidade do produto inválida");
        }
    }
}