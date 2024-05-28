package io.github.robsonfe.rentservice.service;

import io.github.robsonfe.rentservice.model.Cliente;
import io.github.robsonfe.rentservice.model.Locacao;
import io.github.robsonfe.rentservice.model.LocacaoDTO;
import io.github.robsonfe.rentservice.repository.ClienteRepository;
import io.github.robsonfe.rentservice.repository.LocacaoRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocacaoServiceTest {

    @Mock
    private LocacaoRepository locacaoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private LocacaoService locacaoService;

    private LocacaoDTO locacaoDTO;

    @BeforeEach
    void setUp() {

        locacaoDTO = new LocacaoDTO();
        locacaoDTO.setName("Cliente Teste");
        locacaoDTO.setLocacaoStatus(Cliente.LocacaoStatus.TEM_LOCACAO);
        locacaoDTO.setDataInicial(LocalDate.of(2024, 6, 1));
        locacaoDTO.setDataFinal(LocalDate.of(2024, 6, 5));
        locacaoDTO.setVeiculo("Carro");
        locacaoDTO.setDescricao("Locação de veículo");
        locacaoDTO.setTipoVeiculo(Locacao.tipoVeiculo.CARRO);
    }

    @Test
    void testCadastrarLocacao() {

        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setName(locacaoDTO.getName());
        cliente.setLocacaoStatus(locacaoDTO.getLocacaoStatus());

        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        Locacao locacao = new Locacao();
        locacao.setId(1L);
        locacao.setDataInicial(locacaoDTO.getDataInicial());
        locacao.setDataFinal(locacaoDTO.getDataFinal());
        locacao.setVeiculo(locacaoDTO.getVeiculo());
        locacao.setDescricao(locacaoDTO.getDescricao());
        locacao.setTipoVeiculo(locacaoDTO.getTipoVeiculo());
        locacao.setCliente(cliente);

        when(locacaoRepository.save(any(Locacao.class))).thenReturn(locacao);


        Locacao novaLocacao = locacaoService.cadastrarLocacao(locacaoDTO);


        assertNotNull(novaLocacao);
        assertEquals(1L, novaLocacao.getId());
        assertEquals(locacaoDTO.getDataInicial(), novaLocacao.getDataInicial());
        assertEquals(locacaoDTO.getDataFinal(), novaLocacao.getDataFinal());
        assertEquals(locacaoDTO.getVeiculo(), novaLocacao.getVeiculo());
        assertEquals(locacaoDTO.getDescricao(), novaLocacao.getDescricao());
        assertEquals(locacaoDTO.getTipoVeiculo(), novaLocacao.getTipoVeiculo());
        assertEquals(cliente, novaLocacao.getCliente());


        verify(clienteRepository, times(1)).save(any(Cliente.class));
        verify(locacaoRepository, times(1)).save(any(Locacao.class));
    }

    @Test
    void testConsultarLocacaoExistente() {

        Locacao locacaoExistente = new Locacao();
        locacaoExistente.setId(1L);
        locacaoExistente.setDataInicial(LocalDate.of(2024, 6, 1));
        locacaoExistente.setDataFinal(LocalDate.of(2024, 6, 5));
        locacaoExistente.setVeiculo("Carro");
        locacaoExistente.setDescricao("Locação de veículo");
        locacaoExistente.setTipoVeiculo(Locacao.tipoVeiculo.CARRO);

        when(locacaoRepository.findById(1L)).thenReturn(Optional.of(locacaoExistente));


        Locacao locacaoConsultada = locacaoService.consultarLocacao(1L);


        assertNotNull(locacaoConsultada);
        assertEquals(1L, locacaoConsultada.getId());
        assertEquals(locacaoExistente.getDataInicial(), locacaoConsultada.getDataInicial());
        assertEquals(locacaoExistente.getDataFinal(), locacaoConsultada.getDataFinal());
        assertEquals(locacaoExistente.getVeiculo(), locacaoConsultada.getVeiculo());
        assertEquals(locacaoExistente.getDescricao(), locacaoConsultada.getDescricao());
        assertEquals(locacaoExistente.getTipoVeiculo(), locacaoConsultada.getTipoVeiculo());


        verify(locacaoRepository, times(1)).findById(1L);
    }

    @Test
    void testAtualizarLocacao() {

        Locacao locacaoExistente = new Locacao();
        locacaoExistente.setId(1L);
        locacaoExistente.setDataInicial(LocalDate.of(2024, 6, 1));
        locacaoExistente.setDataFinal(LocalDate.of(2024, 6, 5));
        locacaoExistente.setVeiculo("Carro");
        locacaoExistente.setDescricao("Locação de veículo");
        locacaoExistente.setTipoVeiculo(Locacao.tipoVeiculo.CARRO);

        when(locacaoRepository.findById(1L)).thenReturn(Optional.of(locacaoExistente));

        LocacaoDTO locacaoDTOAtualizada = new LocacaoDTO();
        locacaoDTOAtualizada.setDataInicial(LocalDate.of(2024, 6, 2));
        locacaoDTOAtualizada.setDataFinal(LocalDate.of(2024, 6, 6));
        locacaoDTOAtualizada.setVeiculo("Carro Novo");
        locacaoDTOAtualizada.setDescricao("Locação de veículo atualizada");
        locacaoDTOAtualizada.setTipoVeiculo(Locacao.tipoVeiculo.CARRO);


        Locacao locacaoAtualizada = locacaoService.atualizar(1L, locacaoDTOAtualizada);


        assertNotNull(locacaoAtualizada);
        assertEquals(1L, locacaoAtualizada.getId());
        assertEquals(locacaoDTOAtualizada.getDataInicial(), locacaoAtualizada.getDataInicial());
        assertEquals(locacaoDTOAtualizada.getDataFinal(), locacaoAtualizada.getDataFinal());
        assertEquals(locacaoDTOAtualizada.getVeiculo(), locacaoAtualizada.getVeiculo());
        assertEquals(locacaoDTOAtualizada.getDescricao(), locacaoAtualizada.getDescricao());
        assertEquals(locacaoDTOAtualizada.getTipoVeiculo(), locacaoAtualizada.getTipoVeiculo());


        verify(locacaoRepository, times(1)).save(any(Locacao.class));
    }


}
