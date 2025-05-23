package com.ecopla.ecopla.controller;

import com.ecopla.ecopla.model.Vender;
import com.ecopla.ecopla.service.VenderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendas")
public class VenderController {
    private final VenderService service;

    public VenderController(VenderService service) {
        this.service = service;
    }

    // Basic CRUD Endpoints
    @GetMapping
    public List<Vender> listarTodasVendas() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vender> buscarPorId(@PathVariable String id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Vender> criarVenda(@RequestBody Vender venda) {
        Vender novaVenda = service.criar(venda);
        return ResponseEntity.status(201).body(novaVenda);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vender> atualizarVenda(
            @PathVariable String id,
            @RequestBody Vender venda) {
        if (service.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.atualizar(id, venda));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVenda(@PathVariable String id) {
        if (service.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // Custom Search Endpoints
    @GetMapping("/usuario/{userId}")
    public List<Vender> buscarPorUsuario(@PathVariable String userId) {
        return service.buscarPorUsuario(userId);
    }

    @GetMapping("/tipo/{tipo}")
    public List<Vender> buscarPorTipo(@PathVariable String tipo) {
        return service.buscarPorTipo(tipo);
    }

    @GetMapping("/search")
    public List<Vender> pesquisarVendas(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Integer quantidadeMin,
            @RequestParam(required = false) Integer quantidadeMax,
            @RequestParam(required = false) String cor) {
        return service.pesquisarVendas(userId, tipo, quantidadeMin, quantidadeMax, cor);
    }

    // Statistics Endpoints
    @GetMapping("/total-gramas")
    public ResponseEntity<Integer> getTotalVendidoEmGramas() {
        return ResponseEntity.ok(service.totalVendidoEmGramas());
    }

    @GetMapping("/total-gramas/usuario/{userId}")
    public ResponseEntity<Integer> getTotalVendidoPorUsuario(
            @PathVariable String userId) {
        return ResponseEntity.ok(service.totalVendidoPorUsuario(userId));
    }
}