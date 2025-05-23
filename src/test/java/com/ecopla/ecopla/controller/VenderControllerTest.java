package com.ecopla.ecopla.controller;

import com.ecopla.ecopla.model.Vender;
import com.ecopla.ecopla.service.VenderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VenderController.class)
@AutoConfigureMockMvc(addFilters = false) 
class VenderControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private VenderService service;

    private final Vender venda1 = new Vender("1", "user1", "reciclavel", 500, "verde");
    private final Vender venda2 = new Vender("2", "user2", "organico", 300, "marrom");

    @Test
    void listarTodasVendas_ShouldReturnAllSales() throws Exception {
        when(service.listarTodos()).thenReturn(Arrays.asList(venda1, venda2));

        mvc.perform(get("/api/vendas")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[1].id").value("2"));
    }

    @Test
    void buscarPorId_WhenExists_ShouldReturnVenda() throws Exception {
        when(service.buscarPorId("1")).thenReturn(Optional.of(venda1));

        mvc.perform(get("/api/vendas/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user1"))
                .andExpect(jsonPath("$.tipo").value("reciclavel"));
    }

    @Test
    void buscarPorId_WhenNotExists_ShouldReturn404() throws Exception {
        when(service.buscarPorId("99")).thenReturn(Optional.empty());

        mvc.perform(get("/api/vendas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void criarVenda_ShouldReturnCreated() throws Exception {
        when(service.criar(any(Vender.class))).thenAnswer(inv -> {
            Vender v = inv.getArgument(0);
            v.setId("3");
            return v;
        });

        mvc.perform(post("/api/vendas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":\"user3\",\"tipo\":\"plastico\",\"quantidade\":200,\"cor\":\"azul\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("3"));
    }

    @Test
    void atualizarVenda_WhenExists_ShouldReturnUpdated() throws Exception {
        when(service.buscarPorId("1")).thenReturn(Optional.of(venda1));
        when(service.atualizar(eq("1"), any(Vender.class))).thenReturn(venda1);

        mvc.perform(put("/api/vendas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":\"user1\",\"tipo\":\"reciclavel\",\"quantidade\":500,\"cor\":\"verde\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void atualizarVenda_WhenNotExists_ShouldReturn404() throws Exception {
        when(service.buscarPorId("99")).thenReturn(Optional.empty());

        mvc.perform(put("/api/vendas/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":\"user99\",\"tipo\":\"tipo\",\"quantidade\":100,\"cor\":\"cor\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletarVenda_WhenExists_ShouldReturnNoContent() throws Exception {
        when(service.buscarPorId("1")).thenReturn(Optional.of(venda1));

        mvc.perform(delete("/api/vendas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletarVenda_WhenNotExists_ShouldReturn404() throws Exception {
        when(service.buscarPorId("99")).thenReturn(Optional.empty());

        mvc.perform(delete("/api/vendas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarPorUsuario_ShouldReturnUserSales() throws Exception {
        when(service.buscarPorUsuario("user1")).thenReturn(Arrays.asList(venda1));

        mvc.perform(get("/api/vendas/usuario/user1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("user1"))
                .andExpect(jsonPath("$[0].quantidade").value(500));
    }

    @Test
    void buscarPorTipo_ShouldReturnSalesByType() throws Exception {
        when(service.buscarPorTipo("reciclavel")).thenReturn(Arrays.asList(venda1));

        mvc.perform(get("/api/vendas/tipo/reciclavel")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value("reciclavel"));
    }

    @Test
    void pesquisarVendas_ShouldReturnFilteredResults() throws Exception {
        when(service.pesquisarVendas("user1", "reciclavel", 100, 600, "verde"))
                .thenReturn(Arrays.asList(venda1));

        mvc.perform(get("/api/vendas/search")
                .param("userId", "user1")
                .param("tipo", "reciclavel")
                .param("quantidadeMin", "100")
                .param("quantidadeMax", "600")
                .param("cor", "verde")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"));
    }

    @Test
    void getTotalVendidoEmGramas_ShouldReturnTotal() throws Exception {
        when(service.totalVendidoEmGramas()).thenReturn(800);

        mvc.perform(get("/api/vendas/total-gramas"))
                .andExpect(status().isOk())
                .andExpect(content().string("800"));
    }

    @Test
    void getTotalVendidoPorUsuario_ShouldReturnUserTotal() throws Exception {
        when(service.totalVendidoPorUsuario("user1")).thenReturn(500);

        mvc.perform(get("/api/vendas/total-gramas/usuario/user1"))
                .andExpect(status().isOk())
                .andExpect(content().string("500"));
    }
}