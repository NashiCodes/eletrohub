package com.jp.eletrohub.api.dto;

import com.jp.eletrohub.model.entity.Gerente;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GerenteDTO {
    private Long id;
    private String nome;

    public static GerenteDTO create(@Nonnull Gerente gerente) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(gerente, GerenteDTO.class);
    }

    public static Gerente toEntity(@Nonnull GerenteDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Gerente.class);
    }
}
