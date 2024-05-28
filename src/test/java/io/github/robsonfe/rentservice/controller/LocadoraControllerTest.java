package io.github.robsonfe.rentservice.controller;
import io.github.robsonfe.rentservice.model.Cliente;
import io.github.robsonfe.rentservice.model.Locacao;
import io.github.robsonfe.rentservice.model.LocacaoDTO;
import io.github.robsonfe.rentservice.service.LocacaoService;
import io.github.robsonfe.rentservice.service.RabbitMQSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocadoraControllerTest {

    @Mock
    private LocacaoService locacaoService;

    @Mock
    private RabbitMQSender rabbitMQSender;

    @InjectMocks
    private LocadoraController locadoraController;

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

        Locacao locacaoCadastrada = new Locacao();
        locacaoCadastrada.setId(1L);
        locacaoCadastrada.setDataInicial(locacaoDTO.getDataInicial());
        locacaoCadastrada.setDataFinal(locacaoDTO.getDataFinal());
        locacaoCadastrada.setVeiculo(locacaoDTO.getVeiculo());
        locacaoCadastrada.setDescricao(locacaoDTO.getDescricao());
        locacaoCadastrada.setTipoVeiculo(locacaoDTO.getTipoVeiculo());
        locacaoCadastrada.setCliente(new Cliente());

        when(locacaoService.cadastrarLocacao(locacaoDTO)).thenReturn(locacaoCadastrada);


        ResponseEntity<Locacao> responseEntity = locadoraController.cadastrarLocacao(locacaoDTO);


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(locacaoCadastrada, responseEntity.getBody());


        verify(locacaoService, times(1)).cadastrarLocacao(locacaoDTO);
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

        when(locacaoService.consultarLocacao(1L)).thenReturn(locacaoExistente);


        ResponseEntity<Locacao> responseEntity = locadoraController.consultarLocacao(1L);


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(locacaoExistente, responseEntity.getBody());


        verify(locacaoService, times(1)).consultarLocacao(1L);
    }

    @Test
    void testConsultarLocacaoNaoExistente() {

        when(locacaoService.consultarLocacao(2L)).thenThrow(new IllegalArgumentException("Locação não encontrada"));


        ResponseEntity<Locacao> responseEntity = locadoraController.consultarLocacao(2L);


        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());


        verify(locacaoService, times(1)).consultarLocacao(2L);
    }

    @Test
    void testAlterarLocacao() {

        Locacao locacaoAtualizada = new Locacao();
        locacaoAtualizada.setId(1L);
        locacaoAtualizada.setDataInicial(LocalDate.of(2024, 6, 2));
        locacaoAtualizada.setDataFinal(LocalDate.of(2024, 6, 6));
        locacaoAtualizada.setVeiculo("Carro Novo");
        locacaoAtualizada.setDescricao("Locação de veículo atualizada");
        locacaoAtualizada.setTipoVeiculo(Locacao.tipoVeiculo.CARRO);

        when(locacaoService.atualizar(1L, locacaoDTO)).thenReturn(locacaoAtualizada);


        ResponseEntity<Locacao> responseEntity = locadoraController.alterar(1L, locacaoDTO);


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(locacaoAtualizada, responseEntity.getBody());


        verify(locacaoService, times(1)).atualizar(1L, locacaoDTO);
    }


}
