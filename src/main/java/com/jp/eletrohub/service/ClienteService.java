package com.jp.eletrohub.service;

import com.jp.eletrohub.api.dto.cliente.ClienteDTO;
import com.jp.eletrohub.exception.NotFound;
import com.jp.eletrohub.model.entity.Cliente;
import com.jp.eletrohub.model.repository.ClienteRepository;
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
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public List<Cliente> list() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }

    @Transactional
    protected Cliente create(@NotNull ClienteDTO dto) {
        return clienteRepository.save(new Cliente(
                dto.getNome(),
                dto.getCpf(),
                dto.getTelefone(),
                dto.getEmail()
        ));
    }

    @Transactional
    public Cliente saveOrCreate(@Nullable Long id, @NotNull ClienteDTO dto) {
        if (id == null) {
            return create(dto);
        }
        var cliente = findById(id)
                .orElseThrow(() -> new NotFound("Cliente não encontrado com o ID: " + id));

        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEmail(dto.getEmail());

        return clienteRepository.save(cliente);
    }

    @Transactional
    public void delete(@NotBlank Long id) {
        var cliente = findById(id)
                .orElseThrow(() -> new NotFound("Cliente não encontrado com o ID: " + id));
        clienteRepository.delete(cliente);
    }
}
