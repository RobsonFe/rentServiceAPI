package io.github.robsonfe.rentservice.controller;

import io.github.robsonfe.rentservice.model.Cliente;
import io.github.robsonfe.rentservice.model.Locacao;
import io.github.robsonfe.rentservice.model.LocacaoForm;
import io.github.robsonfe.rentservice.repository.ClienteRepository;
import io.github.robsonfe.rentservice.repository.LocacaoRepository;
import io.github.robsonfe.rentservice.service.LocacaoService;
import io.github.robsonfe.rentservice.service.RabbitMQSender;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Tag(name = "Locação", description = "Contém todos os recursos referentes ao registro de locação.")
@RestController
@RequestMapping("api/v1/locacoes")
public class LocadoraController {

    @Autowired
    private LocacaoService locacaoService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private LocacaoRepository locacaoRepository;

    @Autowired
    private RabbitMQSender rabbitMQSender;

    @Operation(summary = "Cadastrar uma Locação",
            description = "Você cadastra uma locação com data inicial e data final dessa locação",
            responses = {
            @ApiResponse(responseCode = "200", description = "Cadastrado com suceso!",
                    content = @Content(mediaType = "application/json", schema = @Schema(
                          implementation = LocadoraController.class
                    ))),
                    @ApiResponse(responseCode = "400", description = "A data inicial não pode ser maior do que a data final",
                            content = @Content(mediaType = "application/json", schema = @Schema(
                                    implementation = Error.class
                            )))
            })
    @PostMapping("/cadastrar")
    public ResponseEntity<Locacao> cadastrarLocacao(@Valid @RequestBody LocacaoForm locacaoForm) {
        try {
            Locacao locacao = locacaoService.cadastrarLocacao(locacaoForm);
            return ResponseEntity.ok(locacao);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @Operation(summary = "Listar Todas as Locações",
            description = "Listagem de todas as locações",
            responses = {
            @ApiResponse(responseCode = "200", description = "Listagem Concluída!",
                    content = @Content(mediaType = "application/json", schema = @Schema(
                            implementation = LocadoraController.class
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
    @GetMapping("/listar")
    public List<Locacao> listarLocacoes(){
        return locacaoService.listarLocacoes();
    }

    @Operation(summary = "Consultar Locação",
            description = "Buscar por locações conforme ID fornecido",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Busca Concluída!",
                            content = @Content(mediaType = "application/json", schema = @Schema(
                                    implementation = LocadoraController.class
                            ))),
                    @ApiResponse(responseCode = "202", description = "Não possui Locação",
                            content = @Content(mediaType = "application/json", schema = @Schema(
                                    implementation = Error.class
                            )))
            })
    @GetMapping("/consultar/{id}")
    public ResponseEntity<Locacao> consultarLocacao(@PathVariable Long id){
        try {
            Locacao locacao = locacaoService.consultarLocacao(id);
            return ResponseEntity.ok(locacao);
        } catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Buscar pelo Nome do Cliente",
            description = "Resultado de busca pelo o nome do cliente conforme nome fornecido",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Nome do usuario encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(
                                    implementation = LocadoraController.class
                            ))),
                    @ApiResponse(responseCode = "202", description = "Não possui esse nome na lista",
                            content = @Content(mediaType = "application/json", schema = @Schema(
                                    implementation = Error.class
                            )))
            })
    @GetMapping("/buscar/nome")
    public List<Cliente> buscarPorNome(@RequestParam String nome){
        return  locacaoService.buscarPorNome(nome);
    }

    @Operation(summary = "Cancelar Locação",
            description = "Cancela uma locação com base no ID fornecido",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Locação cancelada com sucesso!"),
                    @ApiResponse(responseCode = "404", description = "Locação não encontrada")
            })
    @DeleteMapping("/cancelar/{id}")
    public ResponseEntity<Void> cancelarLocacao(@PathVariable Long id){

        try {
            locacaoService.cancelarLocacao(id);
            return ResponseEntity.ok().build();
        }catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }
    }

    // RabbitMQ

    @PostMapping("/calcular-dias-restantes")
    public void calcularDiasRestantes() {
        List<Locacao> locacoes = locacaoRepository.findAll();
        for (Locacao locacao : locacoes) {
            LocalDateTime now = LocalDateTime.now();
            long diasRestantes = ChronoUnit.DAYS.between(now, locacao.getDataFinal());
            Cliente cliente = locacao.getCliente();
            rabbitMQSender.send("locacaoQueue", cliente, diasRestantes);
        }
    }

}
