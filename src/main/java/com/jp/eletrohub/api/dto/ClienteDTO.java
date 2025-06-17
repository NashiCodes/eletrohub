package com.jp.eletrohub.api.dto;

import com.jp.eletrohub.model.entity.Cliente;
import org.modelmapper.ModelMapper;

public class ClienteDTO {
    private Long id;

    private String nome;
    private String cpf;
    private String telefone;
    private String email;


    public static ClienteDTO create(Cliente cliente) {
        ModelMapper modelMapper = new ModelMapper();
        ClienteDTO dto = modelMapper.map(cliente, ClienteDTO.class);
        return dto;
    }
}
