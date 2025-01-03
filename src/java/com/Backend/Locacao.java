package com.Backend;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Locacao {

    private final Cliente cliente;
    private final Equipamento equipamento;
    private final LocalDate dataInicio;
    private final LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucao = LocalDate.now();
    private double multaDiaria;
    private final double valorDiario;
    private double valorTotal;

    public Locacao(Cliente cliente, Equipamento equipamento, LocalDate dataInicio, LocalDate dataPrevistaDevolucao, double multaDiaria) {
        if (equipamento == null) {
            throw new IllegalArgumentException("O equipamento não pode ser nulo.");
        }
        this.cliente = cliente;
        this.equipamento = equipamento;
        this.dataInicio = dataInicio;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
        this.multaDiaria = multaDiaria;
        this.valorDiario = equipamento.getValorDiario();
    }

    // Getters e setters
    public Cliente getCliente() {
        return cliente;
    }

    public Equipamento getEquipamento() {
        return equipamento;
    }

    public LocalDate getDataInicio() { // MÉTODO NÃO É MAIS STATIC
        return dataInicio;
    }

    public LocalDate getDataPrevistaDevolucao() { // MÉTODO NÃO É MAIS STATIC
        return dataPrevistaDevolucao;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public void setMultaDiaria(){
        multaDiaria = 0.10;
    }

    public double getMultaDiaria() {
        return multaDiaria;
    }

    public double getValorDiario() {
        return valorDiario;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public long getQuantidadeDiasLocacao() {
        return ChronoUnit.DAYS.between(dataInicio, dataPrevistaDevolucao);
    }

    public long getDiasAtraso() {
        // Retorna 0 se qualquer uma das datas estiver indisponível
        if (dataDevolucao == null || dataPrevistaDevolucao == null) {
            return 0;
        }

        // Verifica se a devolução foi após a data prevista
        if (dataDevolucao.isAfter(dataPrevistaDevolucao)) {

            long diasDeAtraso = ChronoUnit.DAYS.between(dataPrevistaDevolucao, dataDevolucao);
            System.out.println("Dias de atraso calculados: " + diasDeAtraso);
            return diasDeAtraso;
        }
        return 0; // Sem atraso
    }

    public double calcularMulta() {
        if (dataDevolucao == null || dataPrevistaDevolucao == null) {
            return 0.0;
        }

        long diasDeAtraso = getDiasAtraso();

        if (diasDeAtraso <= 0) {
            return 0.0;
        }

        double multaDiariaPadronizada = valorDiario * 0.10;

        return diasDeAtraso * multaDiariaPadronizada;
    }

}