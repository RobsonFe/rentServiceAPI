package io.github.robsonfe.rentservice.controller;

import io.github.robsonfe.rentservice.model.Cliente;
import io.github.robsonfe.rentservice.model.Locacao;
import io.github.robsonfe.rentservice.service.GerenciadorLocacoes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/locacoes")
public class LocadoraController {

    @Autowired
    private GerenciadorLocacoes gerenciadorLocacoes;

    @GetMapping
    public List<Locacao> listarLocacoes(){
        return gerenciadorLocacoes.listarLocacoes();
    }

    @GetMapping("buscar/nome")
    public List<Cliente> buscarPorNome(@RequestParam String nome){
        return  gerenciadorLocacoes.buscarPorNome(nome);
    }

    @PostMapping
    public ResponseEntity<Locacao> cadastrarLocacao(
            @RequestParam Long clienteId,
            @RequestParam LocalDateTime dataInicial,
            @RequestParam LocalDateTime dataFinal
    ){

       try {
           Locacao locacao = gerenciadorLocacoes.cadastrarLocacao(clienteId,dataInicial,dataFinal);
           return ResponseEntity.ok(locacao);
       }catch (IllegalArgumentException e){
           return ResponseEntity.badRequest().body(null);
       }

    }

    @GetMapping("consultar/{id}")
    public ResponseEntity<Locacao> consultarLocacao(@PathVariable Long id){
        try {
            Locacao locacao = gerenciadorLocacoes.consultarLocacao(id);
            return ResponseEntity.ok(locacao);
        } catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("cancelar/{id}")
    public ResponseEntity<Void> cancelarLocacao(@PathVariable Long id){

        try {
            gerenciadorLocacoes.cancelarLocacao(id);
            return ResponseEntity.ok().build();
        }catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }
    }
}
