package com.jp.eletrohub.api.dto;

import com.jp.eletrohub.model.entity.Venda;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendaDTO {
    private Long id;
    private Date data;
    private Long idVendedor;
    private String nomeVendedor;
    private Long idCliente;
    private String nomeCliente;

    public static VendaDTO create(Venda venda) {
        ModelMapper modelMapper = new ModelMapper();
        VendaDTO dto = modelMapper.map(venda, VendaDTO.class);
        dto.nomeVendedor = venda.getVendedor().getNome();
        dto.nomeCliente = venda.getCliente().getNome();
        return dto;
    }
}
