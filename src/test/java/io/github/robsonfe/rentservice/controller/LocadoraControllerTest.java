package io.github.robsonfe.rentservice.controller;

import io.github.robsonfe.rentservice.model.Cliente;
import io.github.robsonfe.rentservice.model.Locacao;
import io.github.robsonfe.rentservice.repository.ClienteRepository;
import io.github.robsonfe.rentservice.repository.LocacaoRepository;
import io.github.robsonfe.rentservice.service.LocacaoService;
import io.github.robsonfe.rentservice.service.RabbitMQSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;


import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LocadoraController.class)
class LocadoraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocacaoService locacaoService;

    @MockBean
    private ClienteRepository clienteRepository;

    @MockBean
    private LocacaoRepository locacaoRepository;

    @MockBean
    private RabbitMQSender rabbitMQSender;

    private Locacao locacao;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setName("Teste Cliente");
        cliente.setLocacaoStatus(Cliente.LocacaoStatus.SEM_LOCACAO);

        locacao = new Locacao();
        locacao.setId(1L);
        locacao.setCliente(cliente);
        locacao.setDataInicial(LocalDateTime.now().minusDays(1));
        locacao.setDataFinal(LocalDateTime.now().plusDays(1));
    }

    @Test
    void testCadastrarLocacao() throws Exception {
        when(locacaoService.cadastrarLocacao(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(locacao);

        mockMvc.perform(post("/api/v1/locacoes")
                        .param("clienteId", "1")
                        .param("dataInicial", locacao.getDataInicial().toString())
                        .param("dataFinal", locacao.getDataFinal().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.cliente.name", is("Teste Cliente")));

        verify(locacaoService, times(1)).cadastrarLocacao(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void testCadastrarLocacaoBadRequest() throws Exception {
        when(locacaoService.cadastrarLocacao(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenThrow(new IllegalArgumentException("A data inicial não pode ser após a data final"));

        mockMvc.perform(post("/api/v1/locacoes")
                        .param("clienteId", "1")
                        .param("dataInicial", LocalDateTime.now().toString())
                        .param("dataFinal", LocalDateTime.now().minusDays(1).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(locacaoService, times(1)).cadastrarLocacao(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void testListarLocacoes() throws Exception {
        when(locacaoService.listarLocacoes()).thenReturn(Collections.singletonList(locacao));

        mockMvc.perform(get("/api/v1/locacoes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].cliente.name", is("Teste Cliente")));

        verify(locacaoService, times(1)).listarLocacoes();
    }

    @Test
    void testConsultarLocacao() throws Exception {
        when(locacaoService.consultarLocacao(anyLong())).thenReturn(locacao);

        mockMvc.perform(get("/api/v1/locacoes/consultar/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.cliente.name", is("Teste Cliente")));

        verify(locacaoService, times(1)).consultarLocacao(anyLong());
    }

    @Test
    void testConsultarLocacaoNotFound() throws Exception {
        when(locacaoService.consultarLocacao(anyLong())).thenThrow(new IllegalArgumentException("Locação não encontrada"));

        mockMvc.perform(get("/api/v1/locacoes/consultar/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(locacaoService, times(1)).consultarLocacao(anyLong());
    }

    @Test
    void testBuscarPorNome() throws Exception {
        when(locacaoService.buscarPorNome(anyString())).thenReturn(Collections.singletonList(cliente));

        mockMvc.perform(get("/api/v1/locacoes/buscar/nome")
                        .param("nome", "Teste Cliente")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Teste Cliente")));

        verify(locacaoService, times(1)).buscarPorNome(anyString());
    }

    @Test
    void testCancelarLocacao() throws Exception {
        doNothing().when(locacaoService).cancelarLocacao(anyLong());

        mockMvc.perform(delete("/api/v1/locacoes/cancelar/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(locacaoService, times(1)).cancelarLocacao(anyLong());
    }

    @Test
    void testCancelarLocacaoNotFound() throws Exception {
        doThrow(new IllegalArgumentException("Locação não encontrada")).when(locacaoService).cancelarLocacao(anyLong());

        mockMvc.perform(delete("/api/v1/locacoes/cancelar/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(locacaoService, times(1)).cancelarLocacao(anyLong());
    }

    @Test
    void testCalcularDiasRestantes() throws Exception {
        when(locacaoRepository.findAll()).thenReturn(Collections.singletonList(locacao));

        mockMvc.perform(post("/api/v1/locacoes/calcular-dias-restantes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(locacaoRepository, times(1)).findAll();
        verify(rabbitMQSender, times(1)).send(anyString(), any(Cliente.class), anyLong());
    }
}
