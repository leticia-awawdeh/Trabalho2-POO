package com.Backend;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CadastroCli {

    private String nomeCli;
    private String cpfCli; // CPF do cliente
    private String telefoneCli; // Telefone do cliente
    private List<Equipamento> equipAlug = new ArrayList<>(); // Lista de equipamentos alugados
    private LocalDate dataIni;
    private LocalDate dataDevol;
    private int qtdDiasAlug;
    private double multa;

    // Construtor
    public CadastroCli(String nomeCli, String cpfCli, String telefoneCli) {
        this.nomeCli = nomeCli;
        this.cpfCli = cpfCli;
        this.telefoneCli = telefoneCli;
    }

    // Adicionar equipamento ao cliente
    public void adicionarEquipamento(Equipamento equipamento) {
        if (equipamento.getStatus() == Status.DISPONIVEL) {
            equipAlug.add(equipamento);
            equipamento.setStatus(Status.ALUGADO); // Atualiza o status do equipamento
        } else {
            throw new IllegalArgumentException("Equipamento não está disponível para locação.");
        }
    }

    // Getters e Setters
    public String getNomeCli() {
        return nomeCli;
    }

    public void setNomeCli(String nomeCli) {
        this.nomeCli = nomeCli;
    }

    public String getCpf() {
        return cpfCli;
    }

    public void setCpf(String cpfCli) {
        this.cpfCli = cpfCli;
    }

    public String getTelefone() {
        return telefoneCli;
    }

    public void setTelefone(String telefoneCli) {
        this.telefoneCli = telefoneCli;
    }

    @Override
    public String toString() {
        return "Nome: " + nomeCli + "\n" +
                "CPF: " + cpfCli + "\n" +
                "Telefone Celular: " + telefoneCli;
    }
}