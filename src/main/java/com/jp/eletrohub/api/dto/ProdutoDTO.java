package com.jp.eletrohub.api.dto;

import com.jp.eletrohub.model.entity.Produto;
import org.modelmapper.ModelMapper;

public class ProdutoDTO {
    private Long id;

    private String nome;
    private double valor;
    private double quantidade;

    public static ProdutoDTO create(Produto produto) {
        ModelMapper modelMapper = new ModelMapper();
        ProdutoDTO dto = modelMapper.map(produto, ProdutoDTO.class);
        dto.id = produto.getId();
        dto.nome = produto.getNome();
        dto.valor = produto.getValor();
        dto.quantidade = produto.getQuantidade();
        return dto;
    }
}
