package com.jp.eletrohub.api.dto;


import com.jp.eletrohub.model.entity.Vendedor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VendedorDTO {

    private Long id;
    private String nome;

    public static VendedorDTO create(Vendedor vendedor) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(vendedor, VendedorDTO.class);
    }
}
