package com.jp.eletrohub.model.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
public class Vendedor extends Funcionario {
    public Vendedor(String nome) {
        super(nome);
    }
}
