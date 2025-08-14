package com.jp.eletrohub.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    @NotNull(message = "A data da venda não pode ser nula")
    private Date data;

    @ManyToOne
    @JoinColumn(name = "vendedor_id", nullable = false)
    @NotNull(message = "O vendedor não pode ser nulo")
    private Vendedor vendedor;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    @NotNull(message = "O cliente não pode ser nulo")
    private Cliente cliente;

    public Venda(Date data, Vendedor vendedor, Cliente cliente) {
        this.data = data;
        this.vendedor = vendedor;
        this.cliente = cliente;
    }
}
