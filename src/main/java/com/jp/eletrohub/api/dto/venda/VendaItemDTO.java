package com.jp.eletrohub.api.dto.venda;

import com.jp.eletrohub.model.entity.VendaItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendaItemDTO {

    @NotNull(message = "O valor não pode estar em branco")
    @Min(value = 0, message = "O valor não pode ser negativo")
    private double valor;

    @NotNull(message = "A quantidade não pode estar em branco")
    @Min(value = 1, message = "A quantidade deve ser pelo menos 1")
    private int quantidade;

    @NotNull(message = "O ID da venda não pode estar em branco")
    private Long idVenda;
    @NotNull(message = "O ID do produto não pode estar em branco")
    private Long idProduto;

    public static VendaItemDTO create(VendaItem vendaItem) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(vendaItem, VendaItemDTO.class);
    }
}
