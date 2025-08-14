package com.jp.eletrohub.service;

import com.jp.eletrohub.api.dto.funcionarios.GerenteDTO;
import com.jp.eletrohub.model.entity.Gerente;
import com.jp.eletrohub.model.repository.GerenteRepository;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GerenteService {
    private final GerenteRepository gerenteRepository;

    public List<Gerente> list() {
        return gerenteRepository.findAll();
    }

    public Optional<Gerente> findById(Long id) {
        return gerenteRepository.findById(id);
    }


    @Transactional
    public Gerente saveOrCreate(@Nullable Long id, @NotNull GerenteDTO dto) {
        if (id == null) {
            return gerenteRepository.save(new Gerente(dto.getNome()));
        }
        var existingGerente = gerenteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Gerente não encontrado com o ID: " + id));
        existingGerente.setNome(dto.getNome());
        return gerenteRepository.save(existingGerente);
    }

    @Transactional
    public void delete(@NotBlank Long id) {
        var gerente = findById(id);
        if (gerente.isEmpty()) {
            throw new IllegalArgumentException("Gerente não encontrado com o ID: " + id);
        }
        gerenteRepository.delete(gerente.get());
    }
}
