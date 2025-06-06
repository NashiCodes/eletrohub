package com.jp.eletrohub.api.dto;

import com.jp.eletrohub.model.entity.VendaItem;
import org.modelmapper.ModelMapper;

public class VendaItemDTO {
    private Long id;
    private double valor;
    private double quantidade;

    public static VendaItemDTO create(VendaItem vendaItem) {
        ModelMapper modelMapper = new ModelMapper();
        VendaItemDTO dto = modelMapper.map(vendaItem, VendaItemDTO.class);
        dto.id = vendaItem.getId();
        dto.valor = vendaItem.getValor();
        dto.quantidade = vendaItem.getQuantidade();
        return dto;
    }
}
