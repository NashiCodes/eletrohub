package com.jp.eletrohub.service;

import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Vendedor;
import com.jp.eletrohub.model.repository.VendedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VendedorService {
    private final VendedorRepository vendedorRepository;

    public List<Vendedor> list() {
        return vendedorRepository.findAll();
    }

    public Optional<Vendedor> findById(Long id) {
        return vendedorRepository.findById(id);
    }

    @Transactional
    public Vendedor save(Vendedor vendedor) {
        validate(vendedor);
        return vendedorRepository.save(vendedor);
    }

    @Transactional
    public void delete(Vendedor vendedor) {
        Objects.requireNonNull(vendedor.getId());
        vendedorRepository.delete(vendedor);
    }

    public void validate(Vendedor vendedor) {
        if (vendedor.getNome() == null || vendedor.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("Nome do vendedor inválido");
        }
    }
}
