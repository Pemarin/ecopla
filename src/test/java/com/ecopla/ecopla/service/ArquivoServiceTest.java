package com.ecopla.ecopla.service;

import com.ecopla.ecopla.model.Arquivo;
import com.ecopla.ecopla.repository.ArquivoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArquivoServiceTest {

    @Mock
    private ArquivoRepository repository;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private ArquivoService service;

    @Test
    void listarTodos_ShouldReturnAllFiles() {
        // Arrange
        when(repository.findAll()).thenReturn(List.of(
            new Arquivo("1", "user1", file, 100),
            new Arquivo("2", "user2", file, 50)
        ));

        // Act
        List<Arquivo> result = service.listarTodos();

        // Assert
        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void buscarPorId_WhenExists_ShouldReturnFile() {
        // Arrange
        Arquivo arquivo = new Arquivo("1", "user1", file, 100);
        when(repository.findById("1")).thenReturn(Optional.of(arquivo));

        // Act
        Optional<Arquivo> result = service.buscarPorId("1");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("user1", result.get().getUserId());
        verify(repository, times(1)).findById("1");
    }

    @Test
    void buscarPorId_WhenNotExists_ShouldReturnEmpty() {
        // Arrange
        when(repository.findById("99")).thenReturn(Optional.empty());

        // Act
        Optional<Arquivo> result = service.buscarPorId("99");

        // Assert
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findById("99");
    }

    @Test
    void buscarPorUsuario_ShouldReturnUserFiles() {
        // Arrange
        when(repository.findByUserId("user1")).thenReturn(List.of(
            new Arquivo("1", "user1", file, 100)
        ));

        // Act
        List<Arquivo> result = service.buscarPorUsuario("user1");

        // Assert
        assertEquals(1, result.size());
        assertEquals("user1", result.get(0).getUserId());
        verify(repository, times(1)).findByUserId("user1");
    }

    @Test
    void salvarArquivo_ShouldSaveAndReturnFile() throws IOException {
        // Arrange
        when(file.getOriginalFilename()).thenReturn("model.stl");
        when(repository.save(any(Arquivo.class))).thenAnswer(inv -> {
            Arquivo a = inv.getArgument(0);
            a.setId("3");
            return a;
        });

        // Act
        Arquivo result = service.salvarArquivo("user1", file, 100);

        // Assert
        assertEquals("3", result.getId());
        assertEquals("user1", result.getUserId());
        assertEquals(100, result.getPrintScale());
        verify(repository, times(1)).save(any(Arquivo.class));
    }

    @Test
    void deletarArquivo_ShouldCallRepositoryDelete() {
        // Arrange
        doNothing().when(repository).deleteById("1");

        // Act
        service.deletarArquivo("1");

        // Assert
        verify(repository, times(1)).deleteById("1");
    }
}