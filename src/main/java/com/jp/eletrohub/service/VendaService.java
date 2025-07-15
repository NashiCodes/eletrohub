package com.jp.eletrohub.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Venda;
import com.jp.eletrohub.model.repository.VendaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VendaService {
    private final VendaRepository repository;

    public VendaService(VendaRepository repository) {
        this.repository = repository;
    }

    public List<Venda> getVendas() {
        return repository.findAll();
    }

    public Optional<Venda> getVendaById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Venda salvar(Venda venda) {
        validar(venda);
        return repository.save(venda);
    }

    @Transactional
    public void excluir(Venda venda) {
        Objects.requireNonNull(venda.getId());
        repository.delete(venda);
    }

    public void validar(Venda venda) {
        if (venda.getCliente() == null) {
            throw new RegraNegocioException("Cliente da venda é obrigatório");
        }
        if (venda.getItens() == null || venda.getItens().isEmpty()) {
            throw new RegraNegocioException("Venda deve conter ao menos um item");
        }
    }
}
