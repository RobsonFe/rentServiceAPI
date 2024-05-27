package io.github.robsonfe.rentservice.controller;

import io.github.robsonfe.rentservice.model.Cliente;
import io.github.robsonfe.rentservice.model.Locacao;
import io.github.robsonfe.rentservice.repository.ClienteRepository;
import io.github.robsonfe.rentservice.service.ClienteService;
import io.github.robsonfe.rentservice.service.LocacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Cliente", description = "Recursos para manipulação de dados do cliente")
@RestController
@RequestMapping("/api/v1/locacoes/clientes")
public class ClienteController {

    private final ClienteRepository clienteRepository;
    private final LocacaoService locacaoService;
    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteRepository clienteRepository, LocacaoService locacaoService, ClienteService clienteService) {
        this.clienteRepository = clienteRepository;
        this.locacaoService = locacaoService;
        this.clienteService = clienteService;
    }

    @Operation(summary = "Registrar um cliente",
            description = "Registra um cliente no sistema.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(
                                    implementation = Cliente.class))),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida")
            })
    @PostMapping("/cadastrar")
    public ResponseEntity<Cliente> cadastrarCliente(@Valid @RequestBody Cliente cliente) {
        cliente.setLocacaoStatus(Cliente.LocacaoStatus.SEM_LOCACAO);
        Cliente novoCliente = clienteRepository.save(cliente);
        return new ResponseEntity<>(novoCliente, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todos os clientes",
            description = "Retorna uma lista de todos os clientes cadastrados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(
                                    implementation = Cliente.class))),
                    @ApiResponse(responseCode = "404", description = "Nenhum cliente encontrado")
            })
    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    @Operation(summary = "Consultar cliente por ID",
            description = "Busca um cliente pelo seu ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(
                                    implementation = Cliente.class))),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
            })
    @GetMapping("/consultar/{id}")
    public ResponseEntity<Cliente> consultarClientePorId(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Atualizar cliente e locação associada",
            description = "Atualiza um cliente existente e, opcionalmente, a locação associada.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente e locação atualizados com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(
                                    implementation = Cliente.class))),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
            })
    @PutMapping("/alterar/{id}")
    public ResponseEntity<Cliente> atualizarClienteElocacao(@PathVariable("id") Long clienteId, @Valid @RequestBody Cliente clienteAtualizado) {

        Cliente clienteExistente = clienteService.buscarPorId(clienteId);

        if (clienteExistente == null) {
            return ResponseEntity.notFound().build();
        }

        clienteExistente.setName(clienteAtualizado.getName());
        clienteExistente.setLocacaoStatus(clienteAtualizado.getLocacaoStatus());

        if (!clienteExistente.getLocacoes().isEmpty()) {

            Locacao locacaoExistente = clienteExistente.getLocacoes().get(0);

            if (clienteAtualizado.getLocacoes() != null && !clienteAtualizado.getLocacoes().isEmpty()) {
                Locacao locacaoAtualizada = clienteAtualizado.getLocacoes().get(0);

                locacaoExistente.setDataInicial(locacaoAtualizada.getDataInicial());
                locacaoExistente.setDataFinal(locacaoAtualizada.getDataFinal());
                locacaoExistente.setVeiculo(locacaoAtualizada.getVeiculo());
                locacaoExistente.setDescricao(locacaoAtualizada.getDescricao());
                locacaoExistente.setTipoVeiculo(locacaoAtualizada.getTipoVeiculo());

                locacaoService.salvar(locacaoExistente); // Atualiza a locação no banco de dados
            }
        }

        // Salva as alterações no cliente
        clienteService.salvar(clienteExistente);

        return ResponseEntity.ok(clienteExistente);
    }

    @Operation(summary = "Deletar cliente por ID",
            description = "Deleta um cliente existente pelo seu ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Cliente deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
            })
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);

        if (cliente.isPresent()) {
            clienteRepository.delete(cliente.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
