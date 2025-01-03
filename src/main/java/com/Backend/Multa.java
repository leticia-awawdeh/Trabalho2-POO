package com.Backend;

public class Multa {
    private String descricao;
    private double valor;

    // Construtor
    public Multa(String descricao, double valor) {
        this.descricao = descricao;
        this.valor = valor;
    }

    // Getters
    public String getDescricao() {
        return descricao;
    }

    public double getValor() {
        return valor;
    }
}