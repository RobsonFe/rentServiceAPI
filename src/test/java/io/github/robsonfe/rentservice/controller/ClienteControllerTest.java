package io.github.robsonfe.rentservice.controller;

import io.github.robsonfe.rentservice.model.Cliente;
import io.github.robsonfe.rentservice.repository.ClienteRepository;
import io.github.robsonfe.rentservice.service.ClienteService;
import io.github.robsonfe.rentservice.service.LocacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private LocacaoService locacaoService;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    private Cliente clienteExistente;

    @BeforeEach
    void setUp() {
        // Inicializar um cliente para testes
        clienteExistente = new Cliente();
        clienteExistente.setId(1L);
        clienteExistente.setName("Cliente Teste");
        clienteExistente.setLocacaoStatus(Cliente.LocacaoStatus.TEM_LOCACAO);
    }

    @Test
    void testCadastrarCliente() {

        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteExistente);

        Cliente clienteParaCadastrar = new Cliente();
        clienteParaCadastrar.setName("Novo Cliente");

        ResponseEntity<Cliente> responseEntity = clienteController.cadastrarCliente(clienteParaCadastrar);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(clienteExistente, responseEntity.getBody());

        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void testListarClientes() {

        when(clienteRepository.findAll()).thenReturn(Arrays.asList(clienteExistente));

        ResponseEntity<List<Cliente>> responseEntity = clienteController.listarClientes();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().size());
        assertEquals(clienteExistente, responseEntity.getBody().get(0));

        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void testConsultarClienteExistente() {

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteExistente));

        ResponseEntity<Cliente> responseEntity = clienteController.consultarClientePorId(1L);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(clienteExistente, responseEntity.getBody());

        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void testConsultarClienteNaoExistente() {

        when(clienteRepository.findById(2L)).thenReturn(Optional.empty());

        ResponseEntity<Cliente> responseEntity = clienteController.consultarClientePorId(2L);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        verify(clienteRepository, times(1)).findById(2L);
    }

}
