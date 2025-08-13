package com.jp.eletrohub.api.dto;

import com.jp.eletrohub.model.entity.Tecnico;
import com.jp.eletrohub.model.entity.Vendedor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TecnicoDTO {
    private Long id;
    private String nome;

    public static TecnicoDTO create(Tecnico tecnico) {
        ModelMapper modelMapper = new ModelMapper();
        TecnicoDTO dto = modelMapper.map(tecnico, TecnicoDTO.class);
        return dto;
    }
}
