package com.Backend;

import java.util.Random;
import java.util.HashSet;
import java.util.Set;

public class Equipamento {
    private static final Set<Integer> codigosUsados = new HashSet<>();

    private int codigo; // Código único gerado automaticamente
    private String nome; // Nome do equipamento
    private String descricao; // Descrição do equipamento
    private double valorDiario; // Valor diário do aluguel
    private Status status; // Disponível ou Alugado

    public Equipamento(String nome, String descricao, double valorDiario) {
        this.codigo = gerarCodigoEquip(); // Código gerado automaticamente
        this.nome = nome;
        this.descricao = descricao;
        this.valorDiario = valorDiario;
        this.status = Status.DISPONIVEL; // com.Backend.Equipamento está disponível por padrão
    }

    private static int gerarCodigoEquip(){
        Random random = new Random();
        int codigo;

        do {
            codigo = 10000000 + random.nextInt(90000000);
        }while (codigosUsados.contains(codigo));

        codigosUsados.add(codigo);

        return codigo;

    }
    // Getters e Setters
    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getValorDiario() {
        return valorDiario;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "com.Backend.Equipamento{" +
                "codigo:" + codigo +
                ", nome:'" + nome + '\'' +
                ", descricao:'" + descricao + '\'' +
                ", valorDiario:" + valorDiario +
                ", status:'" + status +
                '}';
    }
}

