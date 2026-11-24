package br.edu.hotel.service;

import br.edu.hotel.model.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReservaService {

    private final List<Quarto> quartos = new ArrayList<>();
    private final List<Reserva> reservas = new ArrayList<>();

    public void cadastrarQuarto(Quarto quarto) {
        quartos.add(quarto);
    }

    // RF01 + RN01 + RN03 + RN05: Pesquisar quartos disponíveis
    public List<Quarto> pesquisarQuartosDisponiveis(LocalDate entrada, LocalDate saida,
                                                    int qtdHospedes, CategoriaQuarto categoria) {
        validarPeriodo(entrada, saida);
        if (qtdHospedes <= 0) {
            throw new IllegalArgumentException("Quantidade de hóspedes deve ser maior que zero.");
        }

        List<Quarto> disponiveis = new ArrayList<>();
        for (Quarto q : quartos) {
            if (q.getStatus() == StatusQuarto.MANUTENCAO || q.getStatus() == StatusQuarto.INDISPONIVEL) {
                continue; // RN05: oculta quartos em manutenção/indisponíveis
            }
            if (categoria != null && q.getCategoria() != categoria) {
                continue;
            }
            if (qtdHospedes > q.getCategoria().getCapacidadeMaxima()) {
                continue; // RN03: respeita capacidade da categoria
            }
            if (!possuiSobreposicao(q, entrada, saida)) {
                disponiveis.add(q);
            }
        }
        return disponiveis;
    }

    // RF02: Calcular valor total da estadia com multiplicador sazonal
    public BigDecimal calcularValorTotal(Quarto quarto, LocalDate entrada, LocalDate saida, BigDecimal fatorSazonal) {
        validarPeriodo(entrada, saida);
        if (fatorSazonal == null || fatorSazonal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Fator sazonal inválido.");
        }
        long diarias = ChronoUnit.DAYS.between(entrada, saida);
        return quarto.getDiariaBase()
                .multiply(fatorSazonal)
                .multiply(BigDecimal.valueOf(diarias));
    }

    // RF03 + RN01 + RN03 + RN05: Criar e confirmar reserva
    public Reserva criarReserva(Quarto quarto, String nomeHospede, int qtdHospedes,
                                LocalDate entrada, LocalDate saida, BigDecimal fatorSazonal) {
        validarPeriodo(entrada, saida);
        if (nomeHospede == null || nomeHospede.isBlank()) {
            throw new IllegalArgumentException("Nome do hóspede é obrigatório.");
        }
        if (quarto.getStatus() == StatusQuarto.MANUTENCAO || quarto.getStatus() == StatusQuarto.INDISPONIVEL) {
            throw new IllegalStateException("Quarto indisponível ou em manutenção.");
        }
        if (qtdHospedes <= 0 || qtdHospedes > quarto.getCategoria().getCapacidadeMaxima()) {
            throw new IllegalArgumentException("Quantidade de hóspedes excede a capacidade do quarto.");
        }
        if (possuiSobreposicao(quarto, entrada, saida)) {
            throw new IllegalStateException("Quarto já reservado para o período informado.");
        }

        BigDecimal total = calcularValorTotal(quarto, entrada, saida, fatorSazonal);
        Reserva novaReserva = new Reserva(
                UUID.randomUUID().toString(), quarto, nomeHospede, qtdHospedes, entrada, saida, total
        );
        reservas.add(novaReserva);
        return novaReserva;
    }

    // RN02: Validar horários padrão de check-in (a partir das 14h) e check-out (até 12h)
    public boolean validarHorarioCheckIn(LocalTime horarioCheckIn) {
        return !horarioCheckIn.isBefore(LocalTime.of(14, 0));
    }

    public boolean validarHorarioCheckOut(LocalTime horarioCheckOut) {
        return !horarioCheckOut.isAfter(LocalTime.of(12, 0));
    }

    // RN04: Verificar elegibilidade de cancelamento gratuito (até 48h antes do check-in)
    public boolean isCancelamentoGratuito(Reserva reserva, LocalDateTime momentoSolicitacao) {
        long horasAntecedencia = Duration.between(momentoSolicitacao, reserva.getHorarioPrevistoCheckIn()).toHours();
        return horasAntecedencia >= 48;
    }

    // RF06: Atualizar status do quarto (Governança)
    public void atualizarStatusQuarto(Quarto quarto, StatusQuarto novoStatus) {
        if (quarto == null || novoStatus == null) {
            throw new IllegalArgumentException("Quarto e novo status são obrigatórios.");
        }
        quarto.setStatus(novoStatus);
    }

    private boolean possuiSobreposicao(Quarto quarto, LocalDate entrada, LocalDate saida) {
        for (Reserva r : reservas) {
            if (!r.isCancelada() && r.getQuarto().getNumero().equals(quarto.getNumero())) {
                boolean sobrepoe = entrada.isBefore(r.getDataCheckOut()) && saida.isAfter(r.getDataCheckIn());
                if (sobrepoe) {
                    return true;
                }
            }
        }
        return false;
    }

    private void validarPeriodo(LocalDate entrada, LocalDate saida) {
        if (entrada == null || saida == null || !saida.isAfter(entrada)) {
            throw new IllegalArgumentException("Data de check-out deve ser posterior à data de check-in.");
        }
    }
}
