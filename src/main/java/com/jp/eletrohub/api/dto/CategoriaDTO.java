package com.jp.eletrohub.api.dto;

import com.jp.eletrohub.model.entity.Categoria;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class CategoriaDTO {
    private Long id;
    private String nome;

    public static CategoriaDTO create(Categoria categoria) {
        ModelMapper modelMapper = new ModelMapper();
        CategoriaDTO dto = modelMapper.map(categoria, CategoriaDTO.class);
        return dto;
    }

    public Categoria toEntity() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, Categoria.class);
    }
}
