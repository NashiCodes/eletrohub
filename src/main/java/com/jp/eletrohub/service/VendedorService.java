package com.jp.eletrohub.service;

import com.jp.eletrohub.api.dto.funcionarios.VendedorDTO;
import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Vendedor;
import com.jp.eletrohub.model.repository.VendedorRepository;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
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
    public Vendedor saveOrCreate(@Nullable Long id, @NotNull VendedorDTO dto) {
        if (id == null) {
            return vendedorRepository.save(new Vendedor(dto.getNome()));
        }
        var existingVendedor = vendedorRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Técnico não encontrado com o ID: " + id));
        existingVendedor.setNome(dto.getNome());
        return vendedorRepository.save(existingVendedor);
    }

    @Transactional
    public void delete(Vendedor vendedor) {
        Objects.requireNonNull(vendedor.getId());
        vendedorRepository.delete(vendedor);
    }

    public void delete(@NotNull Long id) {
        var tecnico = vendedorRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Vendedor não encontrado com o ID: " + id));
        vendedorRepository.delete(tecnico);
    }
}
