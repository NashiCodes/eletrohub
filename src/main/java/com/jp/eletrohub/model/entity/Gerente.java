package com.jp.eletrohub.model.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class Gerente extends Funcionario {

    public Gerente(String nome) {
        super(nome);
    }
}
