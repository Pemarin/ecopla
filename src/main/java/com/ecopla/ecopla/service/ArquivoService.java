package com.ecopla.ecopla.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecopla.ecopla.model.Arquivo;
import com.ecopla.ecopla.repository.ArquivoRepository;

@Service
public class ArquivoService {
    private final ArquivoRepository repository;

    @Autowired
    public ArquivoService(ArquivoRepository repository) {
        this.repository = repository;
    }

    public List<Arquivo> listarTodos() {
        return repository.findAll();
    }

    public Optional<Arquivo> buscarPorId(String id) {
        return repository.findById(id);
    }

    public List<Arquivo> buscarPorUsuario(String userId) {
        return repository.findByUserId(userId);
    }

    public Arquivo salvarArquivo(String userId, MultipartFile file, int printScale) throws IOException {
        Arquivo arquivo = new Arquivo();
        arquivo.setUserId(userId);
        arquivo.setFile(file);
        arquivo.setPrintScale(printScale);
        return repository.save(arquivo);
    }

    public void deletarArquivo(String id) {
        repository.deleteById(id);
    }
}