package com.jp.eletrohub.api.dto;

import com.jp.eletrohub.model.entity.Tecnico;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class TecnicoDTO {
    private Long id;
    private String nome;

    public static TecnicoDTO create(Tecnico tecnico) {
        ModelMapper modelMapper = new ModelMapper();
        TecnicoDTO dto = modelMapper.map(tecnico, TecnicoDTO.class);
        return dto;
    }
}
