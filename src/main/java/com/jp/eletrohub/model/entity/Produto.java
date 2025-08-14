package com.jp.eletrohub.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do produto não pode estar em branco")
    private String nome;

    @NotNull(message = "O valor do produto não pode estar em branco")
    @Min(value = 0, message = "O valor não pode ser negativo")
    @Column(nullable = false)
    private double valor;

    @NotNull(message = "A quantidade do produto não pode estar em branco")
    @Column(nullable = false)
    @Min(value = 0, message = "A quantidade não pode ser negativa")
    private double quantidade;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    @NotNull(message = "A categoria do produto não pode ser nula")
    private Categoria categoria;

    public Produto(String nome, double valor, double quantidade, Categoria categoria) {
        this.nome = nome;
        this.valor = valor;
        this.quantidade = quantidade;
        this.categoria = categoria;
    }
}
