package com.ecopla.ecopla.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Document("arquivos")
public class Arquivo {
    @Id
    private String id;
    
    @NotBlank
    private String userId;
    
    @NotNull
    private MultipartFile file;
    
    @Min(10)
    @Max(500)
    private int printScale; //Tamanho da print pode ser de 10% a 500% o tamanho da print do arquivo

    public Arquivo() {}

    public Arquivo(String id, String userId, MultipartFile file, int printScale) {
        this.id = id;
        this.userId = userId;
        this.file = file;
        this.printScale = printScale;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public int getPrintScale() {
        return printScale;
    }

    public void setPrintScale(int printScale) {
        this.printScale = printScale;
    }

    public String getFilename() {
        return this.file != null ? this.file.getOriginalFilename() : null;
    }
}