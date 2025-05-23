package com.ecopla.ecopla.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.*;

@Document("vendas")  // "vendas" means "sales" in Portuguese
public class Vender {
    @Id
    private String id;

    @NotBlank
    private String userId;

    @NotBlank
    private String tipo;

    @NotNull
    @Positive
    private Integer quantidade; 

    private String cor;

    public Vender() {}

    public Vender(String id, String userId, String tipo, Integer quantidade, String cor) {
        this.id = id;
        this.userId = userId;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.cor = cor;
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }
}