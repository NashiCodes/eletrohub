package com.jp.eletrohub.service;

import com.jp.eletrohub.api.dto.ClienteDTO;
import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Cliente;
import com.jp.eletrohub.model.repository.ClienteRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ClienteService {
    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public List<ClienteDTO> list() {
        return repository.findAll().stream().map(ClienteDTO::create).toList();
    }

    public Optional<Cliente> findById(@Nonnull Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Cliente save(@Nonnull Cliente cliente) {
        validar(cliente);
        return repository.saveAndFlush(cliente);
    }

    @Transactional
    public void excluir(@Nonnull Cliente cliente) {
        Objects.requireNonNull(cliente.getId());
        repository.delete(cliente);
    }

    public void validar(@Nonnull Cliente cliente) throws RegraNegocioException {
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("Nome inválido");
        }
        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
            throw new RegraNegocioException("Email inválido");
        }
        if (cliente.getTelefone() == null || cliente.getTelefone().trim().isEmpty()) {
            throw new RegraNegocioException("Telefone inválido");
        }
        if (cliente.getCpf() == null || cliente.getCpf().trim().isEmpty()) {
            throw new RegraNegocioException("CPF inválido");
        }
    }
}
