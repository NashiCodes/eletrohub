package com.jp.eletrohub.service;

import com.jp.eletrohub.api.dto.CategoriaDTO;
import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Categoria;
import com.jp.eletrohub.model.repository.CategoriaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CategoriaService {
    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    public List<CategoriaDTO> list() {
        return repository.findAll()
                .stream()
                .map(CategoriaDTO::create)
                .toList();
    }

    public Optional<Categoria> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Categoria save(@Nonnull CategoriaDTO categoria) {
        if (categoria.getId() != null) {
            var existingCategoria = repository.findById(categoria.getId());
            if (existingCategoria.isEmpty()) {
                throw new RegraNegocioException("Categoria não encontrada");
            }
            validar(categoria.toEntity());
            existingCategoria.get().setNome(categoria.getNome());
            return repository.save(existingCategoria.get());
        } else {
            var categoriaEntity = categoria.toEntity();
            validar(categoriaEntity);

            return repository.save(categoriaEntity);
        }
    }

    @Transactional
    public void delete(@Nonnull Categoria categoria) {
        Objects.requireNonNull(categoria.getId());
        repository.delete(findById(categoria.getId()).orElseThrow(() -> new RegraNegocioException("Categoria não encontrada")));
    }

    public void validar(Categoria categoria) {
        if (categoria.getNome() == null || categoria.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("Nome da categoria inválido");
        }
    }
}