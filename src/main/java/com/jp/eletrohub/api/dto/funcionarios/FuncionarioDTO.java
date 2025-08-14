package com.jp.eletrohub.api.dto.funcionarios;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class FuncionarioDTO {
    @NotBlank(message = "O nome não pode estar em branco")
    private String nome;
}
