package com.Backend;

import java.time.LocalDate;
import java.util.*;

public class CadastroEquip {
    private List<Equipamento> equipamentos;
    private List<Locacao> locacoes;

    public CadastroEquip() {
        this.equipamentos = new ArrayList<>();
        this.locacoes = new ArrayList<>();
    }

    public void cadastrarEquipamento(String nome, String descricao, double valorDiario) {
        Equipamento equipamento = new Equipamento(nome, descricao, valorDiario);
        equipamentos.add(equipamento);
        System.out.println("com.Backend.Equipamento cadastrado com sucesso: " + equipamento);
    }

    public void listarEquipamentos() {
        if (equipamentos.isEmpty()) {
            System.out.println("Nenhum equipamento cadastrado.");
        } else {
            for (Equipamento equipamento : equipamentos) {
                System.out.println(equipamento);
            }
        }
    }

    public void registrarLocacao(Cliente cliente, int codigoEquipamento, LocalDate dataInicio, LocalDate dataPrevistaDevolucao, double multaDiaria) {
        Equipamento equipamento = buscarEquipamentoPorCodigo(codigoEquipamento);

        if (equipamento != null && equipamento.getStatus() == Status.DISPONIVEL) {
            equipamento.setStatus(Status.ALUGADO);
            Locacao locacao = new Locacao(cliente, equipamento, dataInicio, dataPrevistaDevolucao, multaDiaria);
            locacoes.add(locacao);
            System.out.println("Locação registrada com sucesso: " + locacao);
        } else {
            System.out.println("com.Backend.Equipamento não disponível para locação.");
        }
    }

    public void registrarDevolucao(int codigoEquipamento, LocalDate dataDevolucao) {
        Locacao locacao = buscarLocacaoPorCodigoEquipamento(codigoEquipamento);

        if (locacao != null) {
            locacao.setDataDevolucao(dataDevolucao);
            Equipamento equipamento = locacao.getEquipamento();
            equipamento.setStatus(Status.DISPONIVEL);

            System.out.println("Devolução registrada com sucesso: ");
            System.out.println("Valor do aluguel: R$ " + locacao.getValorTotalEstimado());
            System.out.println("Multa (se houver): R$ " + locacao.calcularMulta());
            System.out.println("Total a pagar: R$ " + locacao.calcularValorTotal());
        } else {
            System.out.println("Locação não encontrada para o código do equipamento fornecido.");
        }
    }

    private Equipamento buscarEquipamentoPorCodigo(int codigo) {
        for (Equipamento equipamento : equipamentos) {
            if (equipamento.getCodigo() == codigo) {
                return equipamento;
            }
        }
        return null;
    }

    private Locacao buscarLocacaoPorCodigoEquipamento(int codigoEquipamento) {
        for (Locacao locacao : locacoes) {
            if (locacao.getEquipamento().getCodigo() == codigoEquipamento) {
                return locacao;
            }
        }
        return null;
    }

    public void gerarRelatorioEquipamentosMaisAlugados() {
        Map<Equipamento, Integer> frequenciaAluguel = new HashMap<>();

        for (Locacao locacao : locacoes) {
            Equipamento equipamento = locacao.getEquipamento();
            frequenciaAluguel.put(equipamento, frequenciaAluguel.getOrDefault(equipamento, 0) + 1);
        }

        List<Map.Entry<Equipamento, Integer>> listaEquipamentos = new ArrayList<>(frequenciaAluguel.entrySet());
        listaEquipamentos.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        Relatorio.exibirRelatorioEquipamentosMaisAlugados(listaEquipamentos);
    }

    public void gerarRelatorioClientesComMultasAcumuladas() {
        Map<Cliente, Double> multasAcumuladas = new HashMap<>();

        for (Locacao locacao : locacoes) {
            Cliente cliente = locacao.getCliente();
            double multa = locacao.calcularMulta();
            multasAcumuladas.put(cliente, multasAcumuladas.getOrDefault(cliente, 0.0) + multa);
        }

        List<Map.Entry<Cliente, Double>> listaClientes = new ArrayList<>(multasAcumuladas.entrySet());
        listaClientes.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        Relatorio.exibirRelatorioClientesComMultasAcumuladas(listaClientes);
    }
}