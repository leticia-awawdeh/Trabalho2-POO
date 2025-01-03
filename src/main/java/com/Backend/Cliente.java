package com.Backend;

import java.util.ArrayList;
import java.util.List;

public class Cliente {

    private String nomeCli;
    private String cpfCli;
    private final String telefoneCli;
    private final List<Equipamento> equipAlug = new ArrayList<>();

    private double multasTotais;
    private final List<String> historicoMultas = new ArrayList<>();

    // Construtor
    public Cliente(String nomeCli, String cpfCli, String telefoneCli) {
        this.nomeCli = nomeCli;
        this.cpfCli = cpfCli;
        this.telefoneCli = telefoneCli;
        this.multasTotais = 0.0;
    }

    public void setNomeCli(String nomeCli) {
        this.nomeCli = nomeCli;
    }

    public void setCpfCli(String cpfCli) {
        this.cpfCli = cpfCli;
    }

    public void setMultasTotais(double multasTotais) {
        this.multasTotais = multasTotais;
    }

    // Adicionar multa no histórico
    public void adicionarMulta(String descricao, double valor) {
        String multa = String.format("Multa: %s | Valor: R$ %.2f", descricao, valor);
        historicoMultas.add(multa);
        multasTotais += valor;
    }

    public String getTelefoneCli() {
        return telefoneCli;
    }

    public String getCpfCli() {
        return cpfCli;
    }

    public String getNomeCli() {
        return nomeCli;
    }

    // Getters
    public List<String> getHistoricoMultas() {
        return historicoMultas;
    }

    public double getMultasTotais() {
        return multasTotais;
    }

}