package com.jp.eletrohub.api.dto;

import com.jp.eletrohub.model.entity.Produto;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDTO {
    private Long id;

    private String nome;
    private double valor;
    private double quantidade;
    private Long idCategoria;
    private String nomeCategoria;

    public static ProdutoDTO create(@Nonnull Produto produto) {
        ModelMapper modelMapper = new ModelMapper();
        ProdutoDTO dto = modelMapper.map(produto, ProdutoDTO.class);
        dto.nomeCategoria = produto.getCategoria().getNome();
        return dto;
    }
}
