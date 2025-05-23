package com.ecopla.ecopla.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ecopla.ecopla.model.Arquivo;

@Repository
public interface ArquivoRepository extends MongoRepository<Arquivo, String> {
    List<Arquivo> findByUserId(String userId);
}