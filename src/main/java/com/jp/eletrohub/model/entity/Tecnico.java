package com.jp.eletrohub.model.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class Tecnico extends Funcionario {
    public Tecnico(String nome) {
        super(nome);
    }
}
