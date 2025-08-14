package com.jp.eletrohub.api.dto.venda;

import com.jp.eletrohub.model.entity.Venda;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendaDTO {


    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    @NotNull(message = "A data da venda não pode ser nula")
    private Date data;

    @NotNull(message = "O vendedor não pode estar em branco")
    private Long idVendedor;
    @NotNull(message = "O id do cliente não pode estar em branco")
    private Long idCliente;

    public static VendaDTO create(Venda venda) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(venda, VendaDTO.class);
    }
}
