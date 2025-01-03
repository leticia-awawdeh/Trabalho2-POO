package com.Backend;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Equipamento {
    private static final Set<Integer> codigosUsados = new HashSet<>();

    private final int codigo;
    private String nome;
    private final String descricao;
    private double valorDiario;
    private Status status;
    private Cliente cliente;
    private int frequenciaAluguel;
    private Cliente clienteAluguel;

    public Equipamento(String nome, String descricao, double valorDiario) {
        this.codigo = gerarCodigoEquip(); // Código gerado automaticamente
        this.nome = nome;
        this.descricao = descricao;
        this.valorDiario = valorDiario;
        this.status = Status.DISPONIVEL; // Disponível por padrão
    }

    private static int gerarCodigoEquip() {
        Random random = new Random();
        int codigo;

        do {
            codigo = 100000 + random.nextInt(900000);
        } while (codigosUsados.contains(codigo));

        codigosUsados.add(codigo);

        return codigo;
    }

    // Getters e Setters
    public Cliente getClienteAluguel() {
        return clienteAluguel;
    }

    public void setClienteAluguel(Cliente clienteAluguel) {
        this.clienteAluguel = clienteAluguel;
    }

    public int getFrequenciaAluguel() {
        return frequenciaAluguel;
    }

    public void incrementarFrequenciaAluguel() {
        this.frequenciaAluguel++;
    }


    public int getCodigo() {
        return codigo;
    }



    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setValorDiario(double valorDiario) {
        this.valorDiario = valorDiario;
    }

    public double getValorDiario() {
        return valorDiario;
    }

    public Status getStatus() {
        return status;
    }

    public String getDescricao(){
        return descricao;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public double calcularReceitaTotal() {
        return frequenciaAluguel * valorDiario;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return nome;
    }
}