package com.jp.eletrohub.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Categoria;
import com.jp.eletrohub.model.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaService {
    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    public List<Categoria> getCategorias() {
        return repository.findAll();
    }

    public Optional<Categoria> getCategoriaById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Categoria salvar(Categoria categoria) {
        validar(categoria);
        return repository.save(categoria);
    }

    @Transactional
    public void excluir(Categoria categoria) {
        Objects.requireNonNull(categoria.getId());
        repository.delete(categoria);
    }

    public void validar(Categoria categoria) {
        if (categoria.getNome() == null || categoria.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("Nome da categoria inválido");
        }
    }
}