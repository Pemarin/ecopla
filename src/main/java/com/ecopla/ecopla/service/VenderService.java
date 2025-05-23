package com.ecopla.ecopla.service;

import com.ecopla.ecopla.model.Vender;
import com.ecopla.ecopla.repository.VenderRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class VenderService {
    private final VenderRepository repo;
    private final MongoTemplate mongoTemplate;

    public VenderService(VenderRepository repo, MongoTemplate mongoTemplate) {
        this.repo = repo;
        this.mongoTemplate = mongoTemplate;
    }

    public List<Vender> listarTodos() {
        return repo.findAll();
    }

    public Optional<Vender> buscarPorId(String id) {
        return repo.findById(id);
    }

    public Vender criar(Vender venda) {
        venda.setId(null);
        return repo.save(venda);
    }

    public Vender atualizar(String id, Vender venda) {
        venda.setId(id);
        return repo.save(venda);
    }

    public void deletar(String id) {
        repo.deleteById(id);
    }

    public List<Vender> buscarPorUsuario(String userId) {
        return repo.findByUserId(userId);
    }

    public List<Vender> buscarPorTipo(String tipo) {
        return repo.findByTipo(tipo);
    }

    public List<Vender> pesquisarVendas(String userId, 
                                      String tipo, 
                                      Integer quantidadeMin, 
                                      Integer quantidadeMax, 
                                      String cor) {
        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        if (userId != null) {
            criteriaList.add(Criteria.where("userId").is(userId));
        }
        if (tipo != null) {
            criteriaList.add(Criteria.where("tipo").regex(".*" + tipo + ".*", "i"));
        }
        if (quantidadeMin != null) {
            criteriaList.add(Criteria.where("quantidade").gte(quantidadeMin));
        }
        if (quantidadeMax != null) {
            criteriaList.add(Criteria.where("quantidade").lte(quantidadeMax));
        }
        if (cor != null) {
            criteriaList.add(Criteria.where("cor").regex(".*" + cor + ".*", "i"));
        }

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        return mongoTemplate.find(query, Vender.class);
    }

    public Integer totalVendidoEmGramas() {
        return listarTodos().stream()
                .mapToInt(Vender::getQuantidade)
                .sum();
    }

    public Integer totalVendidoPorUsuario(String userId) {
        return buscarPorUsuario(userId).stream()
                .mapToInt(Vender::getQuantidade)
                .sum();
    }
}