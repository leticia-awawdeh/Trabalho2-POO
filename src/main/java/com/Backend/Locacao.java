package com.Backend;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Locacao {

    private CadastroCli cliente;
    private Equipamento equipamento;
    private static LocalDate dataInicio;
    private static LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucao;
    private double multaDiaria; // Ex.: 0.10 para 10%
    private static double valorDiario; // Valor diário do equipamento
    private static double valorTotal;
    private static double valorTotal2;
    // Valor total da locação

    public Locacao(CadastroCli cliente, Equipamento equipamento, LocalDate dataInicio, LocalDate dataPrevistaDevolucao, double multaDiaria) {
        this.cliente = cliente;
        this.equipamento = equipamento;
        this.dataInicio = dataInicio;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
        this.multaDiaria = multaDiaria;
        this.valorDiario = equipamento.getValorDiario(); // Obtém diretamente do equipamento
//        this.valorTotal = calcularValorTotal(); // Calcula o total estimado
        this.dataDevolucao = null; // Inicialmente sem data
    }

    // Getters e setters
    public CadastroCli getCliente() {
        return cliente;
    }

    public Equipamento getEquipamento() {
        return equipamento;
    }

    public static LocalDate getDataInicio() {
        return dataInicio;
    }

    public static LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

//    public void setDataDevolucao(LocalDate dataDevolucao) {
//        this.dataDevolucao = dataDevolucao;
//        this.valorTotal = calcularValorTotal(); // Recalcula o total ao registrar devolução
//    }

    public double getMultaDiaria() {
        return multaDiaria;
    }

    public static double getValorDiario() {
        return valorDiario; // Retorna o valor diário
    }

    public static double getValorTotal() {
        return valorTotal; // Retorna o valor total (aluguel + multa, se houver)
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

//    public double calcularMulta() {
//        return getDiasAtraso() * multaDiaria;
//    }
//
//    public double calcularValorTotal() {
//        double valorBase = valorDiario * getQuantidadeDiasLocacao(); // Aluguel básico
//
//        return valorTotal2 = valorBase + calcularMulta(); // Soma da multa, se aplicável
//    }
//
//    public static double getValorTotalM(){
//        return valorTotal2;
//    }

    @Override
    public String toString() {
        return "com.Backend.Locacao{" +
                "cliente=" + cliente +
                ", equipamento=" + equipamento +
                ", dataInicio=" + dataInicio +
                ", dataPrevistaDevolucao=" + dataPrevistaDevolucao +
                ", dataDevolucao=" + dataDevolucao +
                ", multaDiaria=" + multaDiaria +
                ", valorDiario=" + valorDiario +
                ", valorTotal=" + valorTotal +
                ", quantidadeDiasLocacao=" + getQuantidadeDiasLocacao() +
                ", diasAtraso=" + getDiasAtraso() +
//                ", multa=" + calcularMulta() +
                '}';
    }
}