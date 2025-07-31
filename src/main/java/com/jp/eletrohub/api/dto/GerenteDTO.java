package com.jp.eletrohub.api.dto;

import com.jp.eletrohub.model.entity.Gerente;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class GerenteDTO {
    private Long id;
    private String nome;

    public static GerenteDTO create(Gerente gerente) {
        ModelMapper modelMapper = new ModelMapper();
        GerenteDTO dto = modelMapper.map(gerente, GerenteDTO.class);
        return dto;
    }
}
