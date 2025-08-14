package com.jp.eletrohub.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Min(value = 0, message = "Valor não pode ser negativo")
    @NotNull(message = "Valor não pode estar em branco")
    private Double valor;

    @Column(nullable = false)
    @Min(value = 0, message = "Quantidade não pode ser negativa")
    @NotNull(message = "Quantidade não pode estar em branco")
    private int quantidade;

    @ManyToOne
    @JoinColumn(name = "venda_id", nullable = false)
    @NotNull(message = "Venda não pode estar em branco")
    private Venda venda;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    @NotNull(message = "Produto não pode estar em branco")
    private Produto produto;
}
