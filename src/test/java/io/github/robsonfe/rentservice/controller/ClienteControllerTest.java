package io.github.robsonfe.rentservice.controller;

import io.github.robsonfe.rentservice.model.Cliente;
import io.github.robsonfe.rentservice.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteRepository clienteRepository;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setName("Teste Cliente");
        cliente.setLocacaoStatus(Cliente.LocacaoStatus.SEM_LOCACAO);
    }

    @Test
    void testCadastrarCliente() throws Exception {
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(post("/api/v1/locacoes/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Teste Cliente\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Teste Cliente")))
                .andExpect(jsonPath("$.locacaoStatus", is("SEM_LOCACAO")));

        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void testListarClientes() throws Exception {
        when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente));

        mockMvc.perform(get("/api/v1/locacoes/clientes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Teste Cliente")));

        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void testConsultarClientePorId() throws Exception {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/api/v1/locacoes/clientes/consultar/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Teste Cliente")));

        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void testConsultarClientePorIdNotFound() throws Exception {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/locacoes/clientes/consultar/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void testAtualizarCliente() throws Exception {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(put("/api/v1/locacoes/clientes/alterar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Cliente Atualizado\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Cliente Atualizado")));

        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void testAtualizarClienteNotFound() throws Exception {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/locacoes/clientes/alterar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Cliente Atualizado\"}"))
                .andExpect(status().isNotFound());

        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void testDeletarCliente() throws Exception {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(cliente));

        mockMvc.perform(delete("/api/v1/locacoes/clientes/deletar/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).delete(cliente);
    }

    @Test
    void testDeletarClienteNotFound() throws Exception {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/v1/locacoes/clientes/deletar/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, never()).delete(any(Cliente.class));
    }
}
