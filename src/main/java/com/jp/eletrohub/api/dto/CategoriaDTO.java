package com.jp.eletrohub.api.dto;

import com.jp.eletrohub.model.entity.Categoria;
import org.modelmapper.ModelMapper;

public class CategoriaDTO {
    private Long id;

    private String nome;


    public static CategoriaDTO create(Categoria categoria) {
        ModelMapper modelMapper = new ModelMapper();
        CategoriaDTO dto = modelMapper.map(categoria, CategoriaDTO.class);
        return dto;
    }
}
