package com.jp.eletrohub.api.dto;

import com.jp.eletrohub.model.entity.Venda;
import org.modelmapper.ModelMapper;

import java.util.Date;

public class VendaDTO {
    private Long id;
    private Date data;

    public static VendaDTO create(Venda venda) {
        ModelMapper modelMapper = new ModelMapper();
        VendaDTO dto = modelMapper.map(venda, VendaDTO.class);
        dto.id = venda.getId();
        dto.data = venda.getData();
        return dto;
    }
}
