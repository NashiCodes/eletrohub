package com.jp.eletrohub.api.dto;

import com.jp.eletrohub.model.entity.Cliente;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ClienteDTO {
    private Long id;
    private String nome;
    private String telefone;
    private String email;

    public static ClienteDTO create(Cliente cliente) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(cliente, ClienteDTO.class);
    }
}
