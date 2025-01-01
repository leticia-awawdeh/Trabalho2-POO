package com.Backend;

import java.util.List;
import java.util.Map;

public class Relatorio {

    public static void exibirRelatorioEquipamentosMaisAlugados(List<Map.Entry<Equipamento, Integer>> listaEquipamentos) {
        System.out.println("Relatório de Equipamentos Mais Alugados");
        System.out.println("======================================");

        for (Map.Entry<Equipamento, Integer> entry : listaEquipamentos) {
            System.out.println(entry.getKey().getNome() + " - " + entry.getValue() + " vezes");
        }

        System.out.println("======================================");
    }

    public static void exibirRelatorioClientesComMultasAcumuladas(List<Map.Entry<Cliente, Double>> listaClientes) {
        System.out.println("Relatório de Clientes com Multas Acumuladas");
        System.out.println("===========================================");

        for (Map.Entry<Cliente, Double> entry : listaClientes) {
            System.out.println(entry.getKey().getNome() + " - R$ " + entry.getValue());
        }

        System.out.println("===========================================");
    }
}