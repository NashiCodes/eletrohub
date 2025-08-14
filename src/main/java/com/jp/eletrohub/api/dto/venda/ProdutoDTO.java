package com.jp.eletrohub.api.dto.venda;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDTO {
    @NotBlank(message = "O nome do produto não pode estar em branco")
    private String nome;

    @Min(value = 0, message = "O valor não pode ser negativo")
    @NotNull(message = "O valor do produto não pode estar em branco")
    private double valor;

    @NotNull(message = "A quantidade não pode estar em branco")
    @Min(value = 1, message = "A quantidade deve ser pelo menos 1")
    private int quantidade;

    @NotNull(message = "A categoria não pode ser nula")
    private Long idCategoria;
}
