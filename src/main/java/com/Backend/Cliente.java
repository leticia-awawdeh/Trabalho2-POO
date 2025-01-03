package com.Backend;

import java.util.ArrayList;
import java.util.List;

public class Cliente {




    private String nomeCli;
    private String cpfCli;
    private String telefoneCli;
    private List<Equipamento> equipAlug = new ArrayList<>();



    private double multasTotais;
    private List<String> historicoMultas = new ArrayList<>(); // Adicionado: Histórico de multas no formato de texto




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

    // Sobrescrevendo o toString
    @Override
    public String toString() {
        return "Nome: " + nomeCli + "\n" +
                "CPF: " + cpfCli + "\n" +
                "Telefone: " + telefoneCli + "\n" +
                "Multas Totais: R$ " + multasTotais;
    }
}