package com.jp.eletrohub.api.dto;

import com.jp.eletrohub.model.entity.Produto;
import org.modelmapper.ModelMapper;

public class ProdutoDTO {
    private Long id;

    private String nome;
    private double valor;
    private double quantidade;
    private Long idCategoria;
    private String nomeCategoria;

    public static ProdutoDTO create(Produto produto) {
        ModelMapper modelMapper = new ModelMapper();
        ProdutoDTO dto = modelMapper.map(produto, ProdutoDTO.class);
        dto.nomeCategoria = produto.getCategoria().getNome();
        return dto;
    }
}
