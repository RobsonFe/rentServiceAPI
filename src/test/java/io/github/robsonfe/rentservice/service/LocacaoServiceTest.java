package io.github.robsonfe.rentservice.service;

import io.github.robsonfe.rentservice.model.Cliente;
import io.github.robsonfe.rentservice.model.Locacao;
import io.github.robsonfe.rentservice.repository.ClienteRepository;
import io.github.robsonfe.rentservice.repository.LocacaoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class LocacaoServiceTest {

    @Mock
    private LocacaoRepository locacaoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private LocacaoService locacaoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarLocacoes() {
        Locacao locacao = new Locacao();
        when(locacaoRepository.findAll()).thenReturn(Collections.singletonList(locacao));

        assertEquals(1, locacaoService.listarLocacoes().size());
        verify(locacaoRepository, times(1)).findAll();
    }

    @Test
    void testCadastrarLocacao() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setLocacaoStatus(Cliente.LocacaoStatus.SEM_LOCACAO);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(locacaoRepository.save(any(Locacao.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Locacao locacao = locacaoService.cadastrarLocacao(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        assertNotNull(locacao);
        assertEquals(cliente, locacao.getCliente());
        assertEquals(Cliente.LocacaoStatus.TEM_LOCACAO, cliente.getLocacaoStatus());
    }

    @Test
    void testCadastrarLocacaoClienteNaoEncontrado() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                locacaoService.cadastrarLocacao(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1))
        );
    }

    @Test
    void testCadastrarLocacaoClienteJaTemLocacao() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setLocacaoStatus(Cliente.LocacaoStatus.TEM_LOCACAO);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        assertThrows(IllegalArgumentException.class, () ->
                locacaoService.cadastrarLocacao(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1))
        );
    }

    @Test
    void testCadastrarLocacaoDataInicialDepoisDataFinal() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setLocacaoStatus(Cliente.LocacaoStatus.SEM_LOCACAO);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        assertThrows(IllegalArgumentException.class, () ->
                locacaoService.cadastrarLocacao(1L, LocalDateTime.now().plusDays(1), LocalDateTime.now())
        );
    }

    @Test
    void testConsultarLocacao() {
        Locacao locacao = new Locacao();
        locacao.setId(1L);

        when(locacaoRepository.findById(1L)).thenReturn(Optional.of(locacao));

        Locacao result = locacaoService.consultarLocacao(1L);

        assertNotNull(result);
        assertEquals(locacao, result);
    }

    @Test
    void testConsultarLocacaoNaoEncontrada() {
        when(locacaoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> locacaoService.consultarLocacao(1L));
    }

    @Test
    void testCancelarLocacao() {
        Locacao locacao = new Locacao();
        locacao.setId(1L);
        Cliente cliente = new Cliente();
        cliente.setLocacaoStatus(Cliente.LocacaoStatus.TEM_LOCACAO);
        locacao.setCliente(cliente);

        when(locacaoRepository.findById(1L)).thenReturn(Optional.of(locacao));

        locacaoService.cancelarLocacao(1L);

        assertEquals(Cliente.LocacaoStatus.SEM_LOCACAO, cliente.getLocacaoStatus());
        verify(locacaoRepository, times(1)).delete(locacao);
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    void testCancelarLocacaoNaoEncontrada() {
        when(locacaoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> locacaoService.cancelarLocacao(1L));
    }

    @Test
    void testBuscarPorNome() {
        Cliente cliente = new Cliente();
        cliente.setName("Test Cliente");

        TypedQuery<Cliente> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Cliente.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.singletonList(cliente));

        List<Cliente> result = locacaoService.buscarPorNome("Test");

        assertEquals(1, result.size());
        assertEquals(cliente.getName(), result.get(0).getName());
    }
}
