package io.github.robsonfe.rentservice.service;

import io.github.robsonfe.rentservice.model.Cliente;
import io.github.robsonfe.rentservice.model.Locacao;
import io.github.robsonfe.rentservice.model.LocacaoForm;
import io.github.robsonfe.rentservice.repository.ClienteRepository;
import io.github.robsonfe.rentservice.repository.LocacaoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LocacaoService {

    @Autowired
    private LocacaoRepository locacaoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EntityManager entityManager;

    public List<Locacao> listarLocacoes(){
        return locacaoRepository.findAll();
    }


    @Transactional
    public Locacao cadastrarLocacao(LocacaoForm form){

        Optional<Cliente> clientes = clienteRepository.findByName(form.getNomeCliente());
                Cliente cliente;

        if(clientes.isPresent()){
            cliente = clientes.get();
        } else {
            cliente = new Cliente();
            cliente.setName(form.getNomeCliente());
            cliente.setLocacaoStatus(form.getLocacaoStatus());
            cliente = clienteRepository.save(cliente);
        }

        Locacao locacao = new Locacao();

        locacao.setCliente(cliente);
        locacao.setDataInicial(form.getDataInicial());
        locacao.setDataFinal(form.getDataFinal());
        locacao.setVeiculo(form.getVeiculo());
        locacao.setDescricao(form.getDescricao());
        locacao.setTipoVeiculo(form.getTipoVeiculo());

        cliente.getLocacoes().add(locacao);
        locacao = locacaoRepository.save(locacao);
        return locacao;
    }
    @Transactional
    public Locacao consultarLocacao(Long id) {
        return locacaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Locação não encontrada"));
    }

    @Transactional
    public Locacao atualizar(Long id, LocacaoForm form){
        Optional<Locacao> locacoes = locacaoRepository.findById(id);

        if(locacoes.isPresent()){
            Locacao locacao = locacoes.get();
            locacao.setDataInicial(form.getDataInicial());
            locacao.setDataFinal(form.getDataFinal());
            locacao.setVeiculo(form.getVeiculo());
            locacao.setDescricao(form.getDescricao());
            locacao.setTipoVeiculo(form.getTipoVeiculo());

            return locacaoRepository.save(locacao);
        }

        throw new IllegalArgumentException("Locação não encontrada para atualizar!");
    }

    @Transactional
    public void cancelarLocacao(Long id) {
        Locacao locacao = locacaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Locação não encontrada"));

        Cliente cliente = locacao.getCliente();

        locacaoRepository.delete(locacao);
        clienteRepository.save(cliente);
    }

    @Transactional(readOnly = true)
    public List<Cliente> buscarPorNome(String nome) {
        String jpql = "SELECT c FROM Cliente c WHERE c.name LIKE :nome";
        TypedQuery<Cliente> query = entityManager.createQuery(jpql, Cliente.class);
        query.setParameter("nome", "%" + nome + "%");
        return query.getResultList();
    }
}
