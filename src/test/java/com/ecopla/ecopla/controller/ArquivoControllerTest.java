package com.ecopla.ecopla.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import com.ecopla.ecopla.model.Arquivo;
import com.ecopla.ecopla.service.ArquivoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArquivoController.class)
@AutoConfigureMockMvc(addFilters = false)
class ArquivoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ArquivoService service;

    @Test
    void listarTodos_ShouldReturnAllFiles() throws Exception {
        // Arrange
        when(service.listarTodos()).thenReturn(List.of(
            new Arquivo("1", "user1", null, 100),
            new Arquivo("2", "user2", null, 50)
        ));

        // Act & Assert
        mvc.perform(get("/api/arquivos")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[1].id").value("2"));
    }

    @Test
    void buscarPorId_WhenExists_ShouldReturnFile() throws Exception {
        // Arrange
        when(service.buscarPorId("1")).thenReturn(Optional.of(
            new Arquivo("1", "user1", null, 100)
        ));

        // Act & Assert
        mvc.perform(get("/api/arquivos/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user1"))
                .andExpect(jsonPath("$.printScale").value(100));
    }

    @Test
    void buscarPorId_WhenNotExists_ShouldReturn404() throws Exception {
        // Arrange
        when(service.buscarPorId("99")).thenReturn(Optional.empty());

        // Act & Assert
        mvc.perform(get("/api/arquivos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarPorUsuario_ShouldReturnUserFiles() throws Exception {
        // Arrange
        when(service.buscarPorUsuario("user1")).thenReturn(List.of(
            new Arquivo("1", "user1", null, 100)
        ));

        // Act & Assert
        mvc.perform(get("/api/arquivos/usuario/user1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("user1"));
    }

    @Test
    void uploadArquivo_ShouldReturnCreated() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
            "file", 
            "model.stl", 
            "application/octet-stream", 
            "3D model content".getBytes()
        );

        Arquivo saved = new Arquivo("3", "user1", null, 100);
        when(service.salvarArquivo(eq("user1"), any(), eq(100))).thenReturn(saved);

        // Act & Assert
        mvc.perform(multipart("/api/arquivos")
                .file(file)
                .param("userId", "user1")
                .param("printScale", "100")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("3"));
    }

    @Test
    void deletarArquivo_WhenExists_ShouldReturnNoContent() throws Exception {
        // Arrange
        when(service.buscarPorId("1")).thenReturn(Optional.of(
            new Arquivo("1", "user1", null, 100)
        ));

        // Act & Assert
        mvc.perform(delete("/api/arquivos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletarArquivo_WhenNotExists_ShouldReturn404() throws Exception {
        // Arrange
        when(service.buscarPorId("99")).thenReturn(Optional.empty());

        // Act & Assert
        mvc.perform(delete("/api/arquivos/99"))
                .andExpect(status().isNotFound());
    }
}