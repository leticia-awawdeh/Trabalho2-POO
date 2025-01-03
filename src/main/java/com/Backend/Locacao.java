package com.Backend;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Locacao {

    private Cliente cliente;
    private Equipamento equipamento;
    private LocalDate dataInicio;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucao = LocalDate.now();
    private double multaDiaria;  // Ex.: 0.10 para 10%
    private double valorDiario;  // Valor diário do equipamento
    private double valorTotal;   // Valor total da locação

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
        return valorDiario; // Retorna o valor diário
    }

    public double getValorTotal() {
        return valorTotal; // Retorna o valor total (aluguel + multa, se houver)
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
            // Calcula os dias com base no dia seguinte à data prevista
            long diasDeAtraso = ChronoUnit.DAYS.between(dataPrevistaDevolucao, dataDevolucao);
            System.out.println("Dias de atraso calculados: " + diasDeAtraso);
            return diasDeAtraso; // Retorna os dias reais de atraso
        }
        return 0; // Sem atraso
    }

    public double calcularMulta() {
        if (dataDevolucao == null || dataPrevistaDevolucao == null) {
            return 0.0; // Sem devolução ou previsão, sem multa
        }

        long diasDeAtraso = getDiasAtraso(); // Usa o cálculo ajustado de dias de atraso

        // Se não há atraso, multa é 0:
        if (diasDeAtraso <= 0) {
            return 0.0;
        }

        // Multa diária padrão (10% de valor diário do aluguel)
        double multaDiariaPadronizada = valorDiario * 0.10;

        // Calcula o valor total da multa:
        return diasDeAtraso * multaDiariaPadronizada;
    }

    @Override
    public String toString() {
        return "Locacao{" +
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
                '}';
    }
}