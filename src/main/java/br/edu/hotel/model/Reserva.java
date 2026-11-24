package br.edu.hotel.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reserva {
    private final String id;
    private final Quarto quarto;
    private final String nomeHospede;
    private final int quantidadeHospedes;
    private LocalDate dataCheckIn;
    private LocalDate dataCheckOut;
    private BigDecimal valorTotal;
    private boolean cancelada;

    public Reserva(String id, Quarto quarto, String nomeHospede, int quantidadeHospedes,
                   LocalDate dataCheckIn, LocalDate dataCheckOut, BigDecimal valorTotal) {
        this.id = id;
        this.quarto = quarto;
        this.nomeHospede = nomeHospede;
        this.quantidadeHospedes = quantidadeHospedes;
        this.dataCheckIn = dataCheckIn;
        this.dataCheckOut = dataCheckOut;
        this.valorTotal = valorTotal;
        this.cancelada = false;
    }

    public LocalDateTime getHorarioPrevistoCheckIn() {
        return dataCheckIn.atTime(14, 0); // RN02: check-in a partir das 14h
    }

    public LocalDateTime getHorarioPrevistoCheckOut() {
        return dataCheckOut.atTime(12, 0); // RN02: check-out até 12h
    }

    public String getId() { return id; }
    public Quarto getQuarto() { return quarto; }
    public String getNomeHospede() { return nomeHospede; }
    public int getQuantidadeHospedes() { return quantidadeHospedes; }
    public LocalDate getDataCheckIn() { return dataCheckIn; }
    public LocalDate getDataCheckOut() { return dataCheckOut; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public boolean isCancelada() { return cancelada; }
    public void setCancelada(boolean cancelada) { this.cancelada = cancelada; }
}
