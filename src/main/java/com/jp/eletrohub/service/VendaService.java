package com.jp.eletrohub.service;

import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Venda;
import com.jp.eletrohub.model.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VendaService {
    private final VendaRepository vendaRepository;

    public List<Venda> getVendas() {
        return vendaRepository.findAll();
    }

    public Optional<Venda> getVendaById(Long id) {
        return vendaRepository.findById(id);
    }

    @Transactional
    public Venda salvar(Venda venda) {
        validate(venda);
        return vendaRepository.save(venda);
    }

    @Transactional
    public void delete(Venda venda) {
        Objects.requireNonNull(venda.getId());
        vendaRepository.delete(venda);
    }

    public void validate(Venda venda) {
        if (venda.getData() == null) {
            throw new RegraNegocioException("Data inválida");
        }

        if (venda.getVendedor() == null || venda.getVendedor().getId() == null || venda.getVendedor().getId() == 0) {
            throw new RegraNegocioException("Vendedor inválida");
        }

        if (venda.getCliente() == null || venda.getCliente().getId() == null || venda.getCliente().getId() == 0) {
            throw new RegraNegocioException("Cliente inválido");
        }
    }
}
