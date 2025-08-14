package com.jp.eletrohub.service;

import com.jp.eletrohub.api.dto.venda.VendaDTO;
import com.jp.eletrohub.exception.NotFound;
import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Venda;
import com.jp.eletrohub.model.repository.VendaRepository;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VendaService {
    private final VendaRepository vendaRepository;
    private final ClienteService clienteService;
    private final VendedorService vendedorService;

    public List<Venda> list() {
        return vendaRepository.findAll();
    }

    public Optional<Venda> findById(Long id) {
        return vendaRepository.findById(id);
    }


    @Transactional
    public Venda saveOrCreate(@Nullable Long id, @NotNull VendaDTO dto) {

        var idVendedor = dto.getIdVendedor();
        var idCliente = dto.getIdCliente();
        var vendedor = vendedorService.findById(idVendedor).orElseThrow(() -> new NotFound("Vendedor não encontrado com o ID: " + idVendedor));
        var cliente = clienteService.findById(idCliente).orElseThrow(() -> new NotFound("Cliente não encontrado com o ID: " + idCliente));
        if (id == null) {
            return vendaRepository.save(new Venda(
                    dto.getData(),
                    vendedor,
                    cliente
            ));
        }

        Venda existingVenda = vendaRepository.findById(id)
                .orElseThrow(() -> new NotFound("Venda não encontrada com o ID: " + id));
        existingVenda.setData(dto.getData());
        existingVenda.setVendedor(vendedor);
        existingVenda.setCliente(cliente);
        return vendaRepository.save(existingVenda);

    }

    @Transactional
    public void delete(@NotNull Long id) {
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Venda não encontrada com o ID: " + id));
        vendaRepository.delete(venda);
    }
}
