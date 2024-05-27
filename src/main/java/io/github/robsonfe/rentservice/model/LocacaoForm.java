package io.github.robsonfe.rentservice.model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.robsonfe.rentservice.model.Locacao.tipoVeiculo;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LocacaoForm {
    private String name;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataInicial;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataFinal;
    private Cliente.LocacaoStatus locacaoStatus;
    private String veiculo;
    private String descricao;
    private tipoVeiculo tipoVeiculo;
}
