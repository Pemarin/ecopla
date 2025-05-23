package com.ecopla.ecopla.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecopla.ecopla.model.Arquivo;
import com.ecopla.ecopla.service.ArquivoService;

@RestController
@RequestMapping("/api/arquivos")
public class ArquivoController {
    private final ArquivoService service;

    @Autowired
    public ArquivoController(ArquivoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Arquivo> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Arquivo> buscarPorId(@PathVariable String id) {
        Optional<Arquivo> arquivo = service.buscarPorId(id);
        return arquivo.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{userId}")
    public List<Arquivo> buscarPorUsuario(@PathVariable String userId) {
        return service.buscarPorUsuario(userId);
    }

    @PostMapping
    public ResponseEntity<Arquivo> uploadArquivo(
            @RequestParam("userId") String userId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("printScale") int printScale) throws IOException {
        
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Arquivo arquivo = service.salvarArquivo(userId, file, printScale);
        return ResponseEntity.ok(arquivo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarArquivo(@PathVariable String id) {
        if (service.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        service.deletarArquivo(id);
        return ResponseEntity.noContent().build();
    }
}