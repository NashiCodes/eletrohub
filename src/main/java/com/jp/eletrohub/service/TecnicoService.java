package com.jp.eletrohub.service;

import com.jp.eletrohub.api.dto.funcionarios.TecnicoDTO;
import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Tecnico;
import com.jp.eletrohub.model.repository.TecnicoRepository;
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
public class TecnicoService {
    private final TecnicoRepository tecnicoRepository;

    public List<Tecnico> list() {
        return tecnicoRepository.findAll();
    }

    public Optional<Tecnico> findById(Long id) {
        return tecnicoRepository.findById(id);
    }

    @Transactional
    public Tecnico saveOrCreate(@Nullable Long id, @NotNull TecnicoDTO dto) {
        if (id == null) {
            return tecnicoRepository.save(new Tecnico(dto.getNome()));
        }
        var existingTecnico = tecnicoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Técnico não encontrado com o ID: " + id));
        existingTecnico.setNome(dto.getNome());
        return tecnicoRepository.save(existingTecnico);
    }

    @Transactional
    public void delete(@NotBlank Long id) {
        var tecnico = tecnicoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Técnico não encontrado com o ID: " + id));
        tecnicoRepository.delete(tecnico);
    }
}
