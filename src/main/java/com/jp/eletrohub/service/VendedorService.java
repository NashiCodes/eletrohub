package com.jp.eletrohub.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Vendedor;
import com.jp.eletrohub.model.repository.VendedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VendedorService {
    private final VendedorRepository repository;

    public VendedorService(VendedorRepository repository) {
        this.repository = repository;
    }

    public List<Vendedor> getVendedores() {
        return repository.findAll();
    }

    public Optional<Vendedor> getVendedorById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Vendedor salvar(Vendedor vendedor) {
        validar(vendedor);
        return repository.save(vendedor);
    }

    @Transactional
    public void excluir(Vendedor vendedor) {
        Objects.requireNonNull(vendedor.getId());
        repository.delete(vendedor);
    }

    public void validar(Vendedor vendedor) {
        if (vendedor.getNome() == null || vendedor.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("Nome do vendedor inválido");
        }
    }
}
