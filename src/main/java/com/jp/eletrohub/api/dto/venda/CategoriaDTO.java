package com.jp.eletrohub.api.dto.venda;

import com.jp.eletrohub.model.entity.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDTO {

    @NotBlank(message = "O nome da categoria não pode estar em branco")
    private String nome;


    public static CategoriaDTO create(@NotNull Categoria categoria) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(categoria, CategoriaDTO.class);
    }

    public static Categoria toEntity(@NotNull CategoriaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Categoria.class);
    }
}
