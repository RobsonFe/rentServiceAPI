package io.github.robsonfe.rentservice.service;

import io.github.robsonfe.rentservice.model.Cliente;
import io.github.robsonfe.rentservice.model.Locacao;
import io.github.robsonfe.rentservice.repository.ClienteRepository;
import io.github.robsonfe.rentservice.repository.LocacaoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GerenciadorLocacoes {

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
    public Locacao cadastrarLocacao(Long clienteId, LocalDateTime dataInicial, LocalDateTime dataFinal){
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        if(cliente.getLocacaoStatus() == Cliente.LocacaoStatus.TEM_LOCACAO){
            throw new IllegalArgumentException("O cliente já possui locação!");
        }

        if(dataInicial.isAfter(dataFinal)){
            throw new IllegalArgumentException("A data inicial não pode ser após a data final");
        }

        Locacao locacao = new Locacao();

        locacao.setCliente(cliente);
        locacao.setDataInicial(dataInicial);
        locacao.setDataFinal(dataFinal);

        cliente.setLocacaoStatus(Cliente.LocacaoStatus.TEM_LOCACAO);

        locacaoRepository.save(locacao);
        clienteRepository.save(cliente);

        return locacao;
    }
    @Transactional
    public Locacao consultarLocacao(Long id) {
        return locacaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Locação não encontrada"));
    }

    @Transactional
    public void cancelarLocacao(Long id) {
        Locacao locacao = locacaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Locação não encontrada"));

        Cliente cliente = locacao.getCliente();
        cliente.setLocacaoStatus(Cliente.LocacaoStatus.SEM_LOCACAO);

        locacaoRepository.delete(locacao);
        clienteRepository.save(cliente);
    }

    @Transactional(readOnly = true)
    public List<Cliente> buscarPorNome(String nome, int page, int size) {
        String jpql = "SELECT c FROM Cliente c WHERE c.name LIKE :nome";
        TypedQuery<Cliente> query = entityManager.createQuery(jpql, Cliente.class);
        query.setParameter("nome", "%" + nome + "%");
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.getResultList();
    }
}
