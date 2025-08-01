package com.jp.eletrohub.service;

import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Gerente;
import com.jp.eletrohub.model.repository.GerenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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
    public Gerente salvar(Gerente gerente) {
        validar(gerente);
        return gerenteRepository.save(gerente);
    }

    @Transactional
    public void delete(Gerente gerente) {
        Objects.requireNonNull(gerente.getId());
        gerenteRepository.delete(gerente);
    }

    public void validar(Gerente gerente) {
        if (gerente.getNome() == null || gerente.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("Nome do gerente inválido");
        }
    }
}
