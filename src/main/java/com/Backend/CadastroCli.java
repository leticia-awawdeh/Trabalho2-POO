package com.Backend;
import com.GUI.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CadastroCli {

    private String nomeCli;
    private String cpfCli; //verificar qual formato de cpf é melhor
    private String telefoneCli; //verificar qual formato de telefone é melhor (se necessita de DDD)
    private List<Equipamento> equipAlug;
    private LocalDate dataIni;
    private LocalDate dataDevol;
    private int qtdDiasAlug;
    private double multa;

    // Construtores

    public CadastroCli(String nomeCli, String cpfCli, String telefoneCli) {
        this.nomeCli = nomeCli;
        this.cpfCli = cpfCli;
        this.telefoneCli = telefoneCli;
    }

    // Métodos

    // Adicionar com.Backend.Equipamento
    public void adicionarEquipamento(Equipamento equipamento) {
        if (equipamento.getStatus() == Status.DISPONIVEL) {
            equipAlug.add(equipamento);
            equipamento.setStatus(Status.ALUGADO); // Atualiza o status do equipamento
        } else {
            throw new IllegalArgumentException("com.Backend.Equipamento não está disponível para locação.");
        }
    }

    // Calcular quantidade de dias de locação
    private int calcularDiasLocacao() {
        return dataDevol.compareTo(dataIni); // Número de dias entre a devolução prevista e a data inicial
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

    public void setCpf(String cpf) {
        this.cpfCli = cpfCli;
    }

    public String getTelefone() {
        return telefoneCli;
    }

    public void setTelefone(String telefone) {
        this.telefoneCli = telefoneCli;
    }

    @Override
    public String toString() {
        return "Nome: " + nomeCli + "\n" +
                "CPF: " + cpfCli + "\n" +
                "Telefone Celular: " + telefoneCli;
    }
}
