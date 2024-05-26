package io.github.robsonfe.rentservice.model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.robsonfe.rentservice.model.Locacao.tipoVeiculo;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


public class LocacaoForm {
    private String nomeCliente;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataInicial;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataFinal;
    private Cliente.LocacaoStatus locacaoStatus;
    private String veiculo;
    private String descricao;
    private tipoVeiculo tipoVeiculo;

    public @NotNull String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(@NotNull String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public @NotNull LocalDate getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(@NotNull LocalDate dataInicial) {
        this.dataInicial = dataInicial;
    }

    public @NotNull LocalDate getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(@NotNull LocalDate dataFinal) {
        this.dataFinal = dataFinal;
    }

    public @NotNull Cliente.LocacaoStatus getLocacaoStatus() {
        return locacaoStatus;
    }

    public void setLocacaoStatus(@NotNull Cliente.LocacaoStatus locacaoStatus) {
        this.locacaoStatus = locacaoStatus;
    }

    public @NotNull String getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(@NotNull String veiculo) {
        this.veiculo = veiculo;
    }

    public @NotNull String getDescricao() {
        return descricao;
    }

    public void setDescricao(@NotNull String descricao) {
        this.descricao = descricao;
    }

    public Locacao.@NotNull tipoVeiculo getTipoVeiculo() {
        return tipoVeiculo;
    }

    public void setTipoVeiculo(Locacao.@NotNull tipoVeiculo tipoVeiculo) {
        this.tipoVeiculo = tipoVeiculo;
    }
}
