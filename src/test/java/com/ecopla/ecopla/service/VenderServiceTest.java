package com.ecopla.ecopla.service;

import com.ecopla.ecopla.model.Vender;
import com.ecopla.ecopla.repository.VenderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VenderServiceTest {

    @Mock
    private VenderRepository repo;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private VenderService service;

    private final Vender venda1 = new Vender("1", "user1", "PET", 500, "verde");
    private final Vender venda2 = new Vender("2", "user2", "organico", 300, "marrom");

    @Test
    void listarTodos_ShouldReturnAllSales() {
        when(repo.findAll()).thenReturn(Arrays.asList(venda1, venda2));
        
        List<Vender> result = service.listarTodos();
        
        assertEquals(2, result.size());
        verify(repo, times(1)).findAll();
    }

    @Test
    void buscarPorId_WhenExists_ShouldReturnVenda() {
        when(repo.findById("1")).thenReturn(Optional.of(venda1));
        
        Optional<Vender> result = service.buscarPorId("1");
        
        assertTrue(result.isPresent());
        assertEquals("user1", result.get().getUserId());
        verify(repo, times(1)).findById("1");
    }

    @Test
    void buscarPorId_WhenNotExists_ShouldReturnEmpty() {
        when(repo.findById("99")).thenReturn(Optional.empty());
        
        Optional<Vender> result = service.buscarPorId("99");
        
        assertTrue(result.isEmpty());
        verify(repo, times(1)).findById("99");
    }

    @Test
    void criar_ShouldSetNullIdAndSave() {
        Vender newVenda = new Vender(null, "user3", "plastico", 200, "azul");
        when(repo.save(any(Vender.class))).thenAnswer(inv -> {
            Vender v = inv.getArgument(0);
            v.setId("3");
            return v;
        });
        
        Vender result = service.criar(newVenda);
        
        assertEquals("3", result.getId());
        assertEquals("user3", result.getUserId());
        verify(repo, times(1)).save(any(Vender.class));
    }

    @Test
    void atualizar_ShouldUpdateExistingVenda() {
        when(repo.existsById("1")).thenReturn(true);
        when(repo.save(any(Vender.class))).thenReturn(venda1);
        
        Vender updated = service.atualizar("1", venda1);
        
        assertEquals("1", updated.getId());
        verify(repo, times(1)).existsById("1");
        verify(repo, times(1)).save(venda1);
    }

    @Test
    void deletar_ShouldCallRepositoryDelete() {
        doNothing().when(repo).deleteById("1");
        
        service.deletar("1");
        
        verify(repo, times(1)).deleteById("1");
    }

    @Test
    void buscarPorUsuario_ShouldReturnUserSales() {
        when(repo.findByUserId("user1")).thenReturn(Arrays.asList(venda1));
        
        List<Vender> result = service.buscarPorUsuario("user1");
        
        assertEquals(1, result.size());
        assertEquals("user1", result.get(0).getUserId());
        verify(repo, times(1)).findByUserId("user1");
    }

    @Test
    void buscarPorTipo_ShouldReturnSalesByType() {
        when(repo.findByTipo("PET")).thenReturn(Arrays.asList(venda1));
        
        List<Vender> result = service.buscarPorTipo("PET");
        
        assertEquals(1, result.size());
        assertEquals("PET", result.get(0).getTipo());
        verify(repo, times(1)).findByTipo("PET");
    }

    @Test
    void pesquisarVendas_ShouldUseMongoTemplate() {
        List<Vender> expected = Arrays.asList(venda1);
        when(mongoTemplate.find(any(Query.class), eq(Vender.class))).thenReturn(expected);
        
        List<Vender> result = service.pesquisarVendas("user1", "PET", 100, 600, "verde");
        
        assertEquals(expected, result);
        verify(mongoTemplate, times(1)).find(any(Query.class), eq(Vender.class));
    }

    @Test
    void totalVendidoEmGramas_ShouldReturnSum() {
        when(repo.findAll()).thenReturn(Arrays.asList(venda1, venda2));
        
        int result = service.totalVendidoEmGramas();
        
        assertEquals(800, result); // 500 + 300
        verify(repo, times(1)).findAll();
    }

    @Test
    void totalVendidoPorUsuario_ShouldReturnUserSum() {
        when(repo.findByUserId("user1")).thenReturn(Arrays.asList(venda1));
        
        int result = service.totalVendidoPorUsuario("user1");
        
        assertEquals(500, result);
        verify(repo, times(1)).findByUserId("user1");
    }
}