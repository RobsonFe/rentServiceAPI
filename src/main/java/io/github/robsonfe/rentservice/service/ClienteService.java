package io.github.robsonfe.rentservice.service;

import io.github.robsonfe.rentservice.model.Cliente;
import io.github.robsonfe.rentservice.model.Locacao;
import io.github.robsonfe.rentservice.model.LocacaoDTO;
import io.github.robsonfe.rentservice.repository.ClienteRepository;
import io.github.robsonfe.rentservice.repository.LocacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private LocacaoRepository locacaoRepository;

    public Cliente cadastrarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente findById(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado com o ID: " + id));
    }

    @Transactional
    public Cliente atualizar(Long id) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
            LocacaoDTO locacaoDTO = new LocacaoDTO();
        if (clienteOptional.isPresent()) {
            Cliente cliente = clienteOptional.get();
            cliente.setName(locacaoDTO.getName());
            cliente.setLocacaoStatus(locacaoDTO.getLocacaoStatus());
            return clienteRepository.save(cliente);
        }

        throw new IllegalArgumentException("Locação não encontrada para atualizar!");
    }

    @Transactional
    public Cliente update(Long id, Cliente cliente) {
        Cliente clienteUpdate = findById(id);

        clienteUpdate.setName(cliente.getName());
        clienteUpdate.setLocacaoStatus(cliente.getLocacaoStatus());

        if (cliente.getLocacoes() != null && !cliente.getLocacoes().isEmpty()) {
            for (Locacao locacao : cliente.getLocacoes()) {
                locacao.setCliente(clienteUpdate);
                locacaoRepository.save(locacao);
            }
        }

        return clienteRepository.save(clienteUpdate);
    }

}
