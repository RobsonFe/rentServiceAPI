package io.github.robsonfe.rentservice.model;
import io.github.robsonfe.rentservice.model.Locacao.tipoVeiculo;
import java.time.LocalDateTime;

public class LocacaoForm {
    private String nomeCliente;
    private LocalDateTime dataInicial;
    private LocalDateTime dataFinal;
    private Cliente.LocacaoStatus locacaoStatus;
    private String veiculo;
    private String descricao;
    private tipoVeiculo tipoVeiculo;

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public LocalDateTime getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(LocalDateTime dataInicial) {
        this.dataInicial = dataInicial;
    }

    public LocalDateTime getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(LocalDateTime dataFinal) {
        this.dataFinal = dataFinal;
    }

    public Cliente.LocacaoStatus getLocacaoStatus() {
        return locacaoStatus;
    }

    public void setLocacaoStatus(Cliente.LocacaoStatus locacaoStatus) {
        this.locacaoStatus = locacaoStatus;
    }

    public String getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(String veiculo) {
        this.veiculo = veiculo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Locacao.tipoVeiculo getTipoVeiculo() {
        return tipoVeiculo;
    }

    public void setTipoVeiculo(Locacao.tipoVeiculo tipoVeiculo) {
        this.tipoVeiculo = tipoVeiculo;
    }
}
