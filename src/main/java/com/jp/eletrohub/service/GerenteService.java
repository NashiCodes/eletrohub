package com.jp.eletrohub.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Gerente;
import com.jp.eletrohub.model.repository.GerenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GerenteService {
    private final GerenteRepository repository;

    public GerenteService(GerenteRepository repository) {
        this.repository = repository;
    }

    public List<Gerente> getGerentes() {
        return repository.findAll();
    }

    public Optional<Gerente> getGerenteById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Gerente salvar(Gerente gerente) {
        validar(gerente);
        return repository.save(gerente);
    }

    @Transactional
    public void excluir(Gerente gerente) {
        Objects.requireNonNull(gerente.getId());
        repository.delete(gerente);
    }

    public void validar(Gerente gerente) {
        if (gerente.getNome() == null || gerente.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("Nome do gerente inválido");
        }
    }
}
