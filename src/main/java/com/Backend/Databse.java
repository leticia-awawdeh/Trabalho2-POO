package com.Backend;

import java.util.ArrayList;
import java.util.List;

public class Databse {
    private static final List<Equipamento> listaEquipamentos = new ArrayList<>();
    private static final List<Locacao> listaLocacoes = new ArrayList<>();
    private static final List<Cliente> listaClientes = new ArrayList<>();

    // Métodos de Equipamentos
    public static void adicionarEquipamento(Equipamento e) {
        listaEquipamentos.add(e);
    }

    public static Equipamento buscarEquipamentoPorCodigo(String codigo) {
        for (Equipamento e : listaEquipamentos) {
            if (String.valueOf(e.getCodigo()).equalsIgnoreCase(codigo)) {
                return e;
            }
        }
        return null;
    }

    public static List<Equipamento> getEquipamentos() {
        return listaEquipamentos;
    }

    // Métodos de Locação
    public static void adicionarLocacao(Locacao loc) {
        listaLocacoes.add(loc);
    }

    public static List<Locacao> getLocacoes() {
        return listaLocacoes;
    }

    // Métodos de com.Backend.Cliente
    public static void adicionarCliente(Cliente c) {
        listaClientes.add(c);
    }

    public static Cliente buscarClientePorCPF(String cpf) {
        for (Cliente c : listaClientes) {
            if (c.getCpf().equalsIgnoreCase(cpf)) {
                return c;
            }
        }
        return null;
    }

    public static List<Cliente> getClientes() {
        return listaClientes;
    }
}