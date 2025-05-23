package com.ecopla.ecopla.repository;

import com.ecopla.ecopla.model.Vender;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenderRepository extends MongoRepository<Vender, String> {
    List<Vender> findByUserId(String userId);
    List<Vender> findByTipo(String tipo);
}