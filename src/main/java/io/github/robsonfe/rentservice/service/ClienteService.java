package io.github.robsonfe.rentservice.service;

import io.github.robsonfe.rentservice.model.Cliente;
import io.github.robsonfe.rentservice.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente cadastrarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente atualizarCliente(Long id, Cliente clienteAtualizado) {
        Optional<Cliente> optionalCliente = clienteRepository.findById(id);
        if (optionalCliente.isPresent()) {
            Cliente cliente = optionalCliente.get();
            cliente.setName(clienteAtualizado.getName());
            cliente.setLocacaoStatus(clienteAtualizado.getLocacaoStatus());
            return clienteRepository.save(cliente);
        }
        throw new IllegalArgumentException("Cliente não encontrado para atualizar");
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public void deletarCliente(Long id) {
        clienteRepository.deleteById(id);
    }
}
