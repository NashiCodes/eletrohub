package com.jp.eletrohub.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Tecnico;
import com.jp.eletrohub.model.repository.TecnicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TecnicoService {
    private final TecnicoRepository repository;

    public TecnicoService(TecnicoRepository repository) {
        this.repository = repository;
    }

    public List<Tecnico> getTecnicos() {
        return repository.findAll();
    }

    public Optional<Tecnico> getTecnicoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Tecnico salvar(Tecnico tecnico) {
        validar(tecnico);
        return repository.save(tecnico);
    }

    @Transactional
    public void excluir(Tecnico tecnico) {
        Objects.requireNonNull(tecnico.getId());
        repository.delete(tecnico);
    }

    public void validar(Tecnico tecnico) {
        if (tecnico.getNome() == null || tecnico.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("Nome do técnico inválido");
        }
    }
}
