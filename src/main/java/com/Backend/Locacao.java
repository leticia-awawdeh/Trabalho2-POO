package com.Backend;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Locacao {

    private Cliente cliente;
    private Equipamento equipamento;
    private LocalDate dataInicio;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucao;
    private double multaDiaria;  // Ex.: 0.10 para 10%

    public Locacao(Cliente cliente, Equipamento equipamento, LocalDate dataInicio, LocalDate dataPrevistaDevolucao, double multaDiaria) {
        this.cliente = cliente;
        this.equipamento = equipamento;
        this.dataInicio = dataInicio;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
        this.multaDiaria = multaDiaria;
        this.dataDevolucao = null;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Equipamento getEquipamento() {
        return equipamento;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public double getMultaDiaria() {
        return multaDiaria;
    }

    public long getQuantidadeDiasLocacao() {
        return ChronoUnit.DAYS.between(dataInicio, dataPrevistaDevolucao);
    }

    public long getDiasAtraso() {
        if (dataDevolucao != null && dataDevolucao.isAfter(dataPrevistaDevolucao)) {
            return ChronoUnit.DAYS.between(dataPrevistaDevolucao, dataDevolucao);
        }
        return 0;
    }

    public double getValorTotalEstimado() {
        return equipamento.getValorDiario() * getQuantidadeDiasLocacao();
    }

    public double calcularMulta() {
        return getDiasAtraso() * multaDiaria;
    }

    public double calcularValorTotal() {
        return getValorTotalEstimado() + calcularMulta();
    }

    @Override
    public String toString() {
        return "com.Backend.Locacao{" +
                "cliente=" + cliente +
                ", equipamento=" + equipamento +
                ", dataInicio=" + dataInicio +
                ", dataPrevistaDevolucao=" + dataPrevistaDevolucao +
                ", dataDevolucao=" + dataDevolucao +
                ", multaDiaria=" + multaDiaria +
                ", quantidadeDiasLocacao=" + getQuantidadeDiasLocacao() +
                ", diasAtraso=" + getDiasAtraso() +
                ", valorTotalEstimado=" + getValorTotalEstimado() +
                ", multa=" + calcularMulta() +
                ", valorTotal=" + calcularValorTotal() +
                '}';
    }
}
