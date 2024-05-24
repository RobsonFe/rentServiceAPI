package io.github.robsonfe.rentservice.controller;

import io.github.robsonfe.rentservice.model.Cliente;
import io.github.robsonfe.rentservice.repository.ClienteRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Cliente" , description = "Recursos conforme manipulação de dados do cliente.")
@RestController
@RequestMapping("api/v1/locacoes/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Operation(summary = "Registrar um cliente",
            description = "Você registra um cliente no sistema",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cadastrado com suceso!",
                            content = @Content(mediaType = "application/json", schema = @Schema(
                                    implementation = ClienteController.class
                            ))),
                    @ApiResponse(responseCode = "204", description = "O cliente não foi encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(
                                    implementation = Error.class
                            ))),
                    @ApiResponse(responseCode = "404", description = "Não foi encontrado nenhum registro no servidor",
                            content = @Content(mediaType = "application/json", schema = @Schema(
                                    implementation = Error.class
                            )))
            })
    @PostMapping("/cadastrar")
    public ResponseEntity<Cliente> cadastrarCliente(@RequestBody Cliente cliente) {
        cliente.setLocacaoStatus(Cliente.LocacaoStatus.SEM_LOCACAO);
        Cliente novoCliente = clienteRepository.save(cliente);
        return new ResponseEntity<>(novoCliente, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar Todos os Clientes",
            description = "Listagem de todos os clientes",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listagem Concluída!",
                            content = @Content(mediaType = "application/json", schema = @Schema(
                                    implementation = ClienteController.class
                            ))),
                    @ApiResponse(responseCode = "202", description = "Não possui lista a ser exibida",
                            content = @Content(mediaType = "application/json", schema = @Schema(
                                    implementation = Error.class
                            ))),
                    @ApiResponse(responseCode = "404", description = "Não foi encontrado nenhum dado no servidor.",
                            content = @Content(mediaType = "application/json", schema = @Schema(
                                    implementation = Error.class
                            )))
            })
    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    @Operation(summary = "Consultar Cliente",
            description = "Buscar por cliente conforme ID fornecido",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Busca Concluída!",
                            content = @Content(mediaType = "application/json", schema = @Schema(
                                    implementation = ClienteController.class
                            ))),
                    @ApiResponse(responseCode = "202", description = "Não possui nenhum cliente",
                            content = @Content(mediaType = "application/json", schema = @Schema(
                                    implementation = Error.class
                            )))
            })
    @GetMapping("consultar/{id}")
    public ResponseEntity<Cliente> consultarClientePorId(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Atualizar Cliente",
            description = "Atualiza um cliente existente com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(
                                    implementation = Cliente.class))),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado.")
            })
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

    @Operation(summary = "Deletar",
            description = "Deleta um cliente com base no ID fornecido",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente excluido com sucesso!"),
                    @ApiResponse(responseCode = "204", description = "Cliente não encontrado")
            })
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
