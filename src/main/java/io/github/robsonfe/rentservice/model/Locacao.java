package io.github.robsonfe.rentservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter @Setter @NoArgsConstructor
@Entity
@Table(name = "tb_locacao")
public class Locacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_locacao")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @Column(name = "data_inicial", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime dataInicial;

    @Column(name = "data_final", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime dataFinal;

    @Column(name = "veiculos", length = 100, nullable = false)
    private String veiculo;

    @Column(name = "descricao")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_veiculo", nullable = false , length = 50)
    private tipoVeiculo tipoVeiculo;

    public enum tipoVeiculo{
        MOTO,
        CARRO
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Locacao locacao = (Locacao) o;
        return Objects.equals(id, locacao.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Locacao{" +
                "id=" + id +
                ", cliente=" + (cliente != null ? cliente.getName() : null) +
                ", dataInicial=" + dataInicial +
                ", dataFinal=" + dataFinal +
                ", veiculo='" + veiculo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", tipoVeiculo=" + tipoVeiculo +
                '}';
    }
}
