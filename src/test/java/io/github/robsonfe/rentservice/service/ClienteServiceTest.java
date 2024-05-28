package io.github.robsonfe.rentservice.service;

import io.github.robsonfe.rentservice.model.Cliente;
import io.github.robsonfe.rentservice.repository.ClienteRepository;
import io.github.robsonfe.rentservice.repository.LocacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private LocacaoRepository locacaoRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente clienteExistente;

    @BeforeEach
    void setUp() {

        clienteExistente = new Cliente();
        clienteExistente.setId(1L);
        clienteExistente.setName("Cliente Teste");
        clienteExistente.setLocacaoStatus(Cliente.LocacaoStatus.TEM_LOCACAO);
        clienteExistente.setLocacoes(Collections.emptyList());
    }

    @Test
    void testCadastrarCliente() {

        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteExistente);

        Cliente clienteParaCadastrar = new Cliente();
        clienteParaCadastrar.setName("Novo Cliente");

        Cliente clienteCadastrado = clienteService.cadastrarCliente(clienteParaCadastrar);

        assertNotNull(clienteCadastrado);
        assertEquals(clienteExistente.getId(), clienteCadastrado.getId());
        assertEquals(clienteParaCadastrar.getName(), clienteCadastrado.getName());

        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void testFindByIdClienteExistente() {

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteExistente));

        Cliente clienteConsultado = clienteService.findById(1L);

        assertNotNull(clienteConsultado);
        assertEquals(clienteExistente.getId(), clienteConsultado.getId());
        assertEquals(clienteExistente.getName(), clienteConsultado.getName());

        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdClienteNaoExistente() {

        when(clienteRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.findById(2L);
        });

        verify(clienteRepository, times(1)).findById(2L);
    }

    @Test
    void testUpdateCliente() {

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteExistente);

        Cliente clienteParaAtualizar = new Cliente();
        clienteParaAtualizar.setName("Cliente Atualizado");

        Cliente clienteAtualizado = clienteService.update(1L, clienteParaAtualizar);

        assertNotNull(clienteAtualizado);
        assertEquals(clienteExistente.getId(), clienteAtualizado.getId());
        assertEquals(clienteParaAtualizar.getName(), clienteAtualizado.getName());

        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

}
