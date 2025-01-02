package com.Backend;

import java.util.ArrayList;
import java.util.List;

public class GerenciadorDados {
    private static List<Equipamento> listaEquipamentos = new ArrayList<>();
    private static List<AtualizacaoListener> listeners = new ArrayList<>(); // Lista de ouvintes

    public static List<Equipamento> getListaEquipamentos() {
        return listaEquipamentos;
    }

    public static void adicionarEquipamento(Equipamento equipamento) {
        listaEquipamentos.add(equipamento);
        notificarAtualizacao(); // Notifica painéis e componentes sobre mudanças
    }

    public static void removerEquipamento(Equipamento equipamento) {
        listaEquipamentos.remove(equipamento);
        notificarAtualizacao();
    }

    /**
     * Adiciona um listener para ser notificado quando a lista de equipamentos mudar.
     */
    public static void addAtualizacaoListener(AtualizacaoListener listener) {
        listeners.add(listener);
    }

    /**
     * Remove um listener.
     */
    public static void removeAtualizacaoListener(AtualizacaoListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifica todos os listeners sobre a atualização.
     */
    private static void notificarAtualizacao() {
        for (AtualizacaoListener listener : listeners) {
            listener.onAtualizacao();
        }
    }

    /**
     * Interface para observar mudanças na lista de equipamentos.
     */
    public interface AtualizacaoListener {
        void onAtualizacao();
    }
}