package br.edu.hotel.model;

import java.math.BigDecimal;

public class Quarto {
    private final String numero;
    private final CategoriaQuarto categoria;
    private final BigDecimal diariaBase;
    private StatusQuarto status;

    public Quarto(String numero, CategoriaQuarto categoria, BigDecimal diariaBase, StatusQuarto status) {
        this.numero = numero;
        this.categoria = categoria;
        this.diariaBase = diariaBase;
        this.status = status;
    }

    public String getNumero() { return numero; }
    public CategoriaQuarto getCategoria() { return categoria; }
    public BigDecimal getDiariaBase() { return diariaBase; }
    public StatusQuarto getStatus() { return status; }
    public void setStatus(StatusQuarto status) { this.status = status; }
}
