package com.jp.eletrohub.api.dto.cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {
    @NotBlank(message = "O nome não pode estar em branco")
    private String nome;
    @NotBlank(message = "O CPF não pode estar em branco")
    @CPF(message = "O CPF deve ser válido")
    private String cpf;
    @NotBlank(message = "O telefone não pode estar em branco")
    private String telefone;
    @NotBlank(message = "O email não pode estar em branco")
    @Email(message = "O email deve ser válido")
    private
    String email;
}
