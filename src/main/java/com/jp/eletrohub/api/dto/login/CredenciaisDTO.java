package com.jp.eletrohub.api.dto.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CredenciaisDTO {
    @NotBlank(message = "O login não pode estar em branco")
    @Email(message = "O login deve ser um email válido")
    private String login;
    @NotBlank(message = "A senha não pode estar em branco")
    private String senha;
}
