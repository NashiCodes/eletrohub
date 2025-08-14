package com.jp.eletrohub.api.dto.login;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
    @NotBlank(message = "O nome não pode estar em branco")
    @Email(message = "O nome deve ser um email válido")
    private String login;

    @NotBlank(message = "A senha não pode estar em branco")
    private String senha;

    @NotNull
    private boolean admin;
}
