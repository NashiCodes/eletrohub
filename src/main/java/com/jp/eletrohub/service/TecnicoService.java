package com.jp.eletrohub.service;

import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Tecnico;
import com.jp.eletrohub.model.repository.TecnicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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
    public Tecnico save(Tecnico tecnico) {
        validate(tecnico);
        return tecnicoRepository.save(tecnico);
    }

    @Transactional
    public void delete(Tecnico tecnico) {
        Objects.requireNonNull(tecnico.getId());
        tecnicoRepository.delete(tecnico);
    }

    public void validate(Tecnico tecnico) {
        if (tecnico.getNome() == null || tecnico.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("Nome do técnico inválido");
        }
    }
}
