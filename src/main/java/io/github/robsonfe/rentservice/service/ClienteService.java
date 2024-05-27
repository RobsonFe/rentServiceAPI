package io.github.robsonfe.rentservice.service;

import io.github.robsonfe.rentservice.model.Cliente;
import io.github.robsonfe.rentservice.model.LocacaoDTO;
import io.github.robsonfe.rentservice.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente cadastrarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente atualizar(Long id, LocacaoDTO locacaoDTO) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);

        if (clienteOptional.isPresent()) {
            Cliente cliente = clienteOptional.get();
            cliente.setName(locacaoDTO.getName());
            cliente.setLocacaoStatus(locacaoDTO.getLocacaoStatus());
            return clienteRepository.save(cliente);
        }

        throw new IllegalArgumentException("Locação não encontrada para atualizar!");
    }


}
