package io.github.robsonfe.rentservice.service;

import io.github.robsonfe.rentservice.model.Cliente;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send(String queueName, Cliente cliente, long diasRestantes) {
        String message = String.format("Cliente: %s, Dias Restantes: %d", cliente.getName(), diasRestantes);
        amqpTemplate.convertAndSend(queueName, message);
    }
}
