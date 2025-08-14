package com.jp.eletrohub.service;

import com.jp.eletrohub.api.dto.venda.CategoriaDTO;
import com.jp.eletrohub.model.entity.Categoria;
import com.jp.eletrohub.model.repository.CategoriaRepository;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public List<Categoria> list() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> findById(Long id) {
        return categoriaRepository.findById(id);
    }

    @Transactional
    public Categoria saveOrCreate(@Nullable Long id, @NotNull CategoriaDTO dto) {
        if (id == null) {
            return categoriaRepository.save(CategoriaDTO.toEntity(dto));
        }
        var categoria = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada com o ID: " + id));
        categoria.setNome(dto.getNome());
        return categoriaRepository.save(categoria);
    }

    @Transactional
    public void delete(@NotNull Long id) {
        var categoria = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada com o ID: " + id));
        categoriaRepository.delete(categoria);
    }

}