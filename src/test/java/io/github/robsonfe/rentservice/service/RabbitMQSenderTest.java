package io.github.robsonfe.rentservice.service;

import io.github.robsonfe.rentservice.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.core.AmqpTemplate;

public class RabbitMQSenderTest {

    @InjectMocks
    private RabbitMQSender rabbitMQSender;

    @Mock
    private AmqpTemplate amqpTemplate;

    @BeforeEach // Método que roda antes de cada teste para inicializar mocks
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSend() {
        String queueName = "testQueue";
        Cliente cliente = new Cliente();
        cliente.setName("Teste");
        long diasRestantes = 10;

        rabbitMQSender.send(queueName, cliente, diasRestantes);

        String expectedMessage = String.format("Cliente: %s, Dias Restantes: %d", cliente.getName(), diasRestantes);

        verify(amqpTemplate, times(1)).convertAndSend(queueName, expectedMessage);
    }
}
