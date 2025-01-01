package com.Backend;
import com.GUI.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CadastroCli {

    private String nomeCli;
    private String cpf; //verificar qual formato de cpf é melhor
    private String telefone; //verificar qual formato de telefone é melhor (se necessita de DDD)
    private List<Equipamento> equipAlug;
    private LocalDate dataIni;
    private LocalDate dataDevol;
    private int qtdDiasAlug;
    private double multa;

    // Construtores

    public CadastroCli(String nomeCli, String cpf, String telefone) {
        this.nomeCli = nomeCli;
        this.cpf = cpf;
        this.telefone = telefone;
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
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }


    @Override
    public String toString() {
        return "Cadastro do Cliente: " + nomeCli +
                ", CPF: " + cpf +
                ", Telefone: " + telefone;
    }
}
