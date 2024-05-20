package io.github.robsonfe.rentservice.controller;

import io.github.robsonfe.rentservice.model.Cliente;
import io.github.robsonfe.rentservice.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/locacoes/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @PostMapping
    public ResponseEntity<Cliente> cadastrarCliente(@RequestBody Cliente cliente) {
        cliente.setLocacaoStatus(Cliente.LocacaoStatus.SEM_LOCACAO);
        Cliente novoCliente = clienteRepository.save(cliente);
        return new ResponseEntity<>(novoCliente, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    @GetMapping("consultar/{id}")
    public ResponseEntity<Cliente> consultarClientePorId(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("alterar/{id}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Long id, @RequestBody Cliente clienteDetalhes) {
        Optional<Cliente> clienteAtual = clienteRepository.findById(id);

        if (clienteAtual.isPresent()) {
            Cliente cliente = clienteAtual.get();
            cliente.setName(clienteDetalhes.getName());
            Cliente clienteAtualizado = clienteRepository.save(cliente);
            return ResponseEntity.ok(clienteAtualizado);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("deletar/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);

        if (cliente.isPresent()) {
            clienteRepository.delete(cliente.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
