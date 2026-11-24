package br.edu.hotel.model;

public enum CategoriaQuarto {
    STANDARD(2),
    SUPERIOR(3),
    SUITE(4);

    private final int capacidadeMaxima;

    CategoriaQuarto(int capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
    }

    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }
}
