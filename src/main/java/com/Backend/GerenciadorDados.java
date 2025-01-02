package com.Backend;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GerenciadorDados {
    private static List<Equipamento> listaEquipamentos = new ArrayList<>();
    private static List<CadastroCli> listaClientes = new ArrayList<>(); // Lista de clientes

    private static List<AtualizacaoListener> listeners = new ArrayList<>(); // Lista de ouvintes

    // ---------------- Métodos para Equipamentos ---------------- //
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

    // ---------------- Métodos para Clientes ---------------- //
    /**
     * Obtém a lista completa de clientes cadastrados.
     */
    public static List<CadastroCli> getListaClientes() {
        return listaClientes;
    }

    /**
     * Adiciona um cliente à lista de clientes.
     */
    public static void adicionarCliente(CadastroCli cliente) {
        listaClientes.add(cliente);
        notificarAtualizacao(); // Notifica listeners sobre a mudança
    }

    /**
     * Remove um cliente da lista de clientes.
     */
    public static void removerCliente(CadastroCli cliente) {
        listaClientes.remove(cliente);
        notificarAtualizacao();
    }

    // ---------------- Notificações ---------------- //
    /**
     * Adiciona um listener para ser notificado quando a lista de equipamentos ou clientes mudar.
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
    public static void notificarAtualizacao() {
        for (AtualizacaoListener listener : listeners) {
            listener.onAtualizacao();
        }
    }

    // ---------------- Interface Listener ---------------- //
    /**
     * Interface para observar mudanças na lista de equipamentos ou lista de clientes.
     */
    public interface AtualizacaoListener {
        void onAtualizacao();
    }

    public class BuscaCpfCliente {

        private JTextField txtCpfBusca;
        private JList<String> listResultCpfs;
        private JPanel panelBuscaCpf;

        public BuscaCpfCliente() {
            txtCpfBusca.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    // Obtém o texto digitado no campo de CPF
                    String inputCpf = txtCpfBusca.getText();

                    // Busca CPFs compatíveis
                    List<String> resultados = buscarCpfsCompatíveis(inputCpf);

                    // Atualiza a lista exibida
                    atualizarResultados(resultados);
                }
            });
        }

        /**
         * Busca os CPFs mais compatíveis com o texto informado no campo `txtCpfBusca`.
         *
         * @param inputCpf CPF ou parte dele digitado no campo de busca.
         * @return Lista de strings com CPFs compatíveis.
         */
        private List<String> buscarCpfsCompatíveis(String inputCpf) {
            // Obtém a lista de clientes do GerenciadorDados
            List<CadastroCli> listaClientes = GerenciadorDados.getListaClientes();

            // Realiza a filtragem baseada no input fornecido
            return listaClientes.stream()
                    .map(CadastroCli::getCpf)  // Obtém todos os CPFs
                    .filter(cpf -> cpf.contains(inputCpf)) // Filtra os que contêm o texto digitado
                    .collect(Collectors.toList()); // Converte para lista
        }

        /**
         * Atualiza a lista da interface com os resultados encontrados.
         *
         * @param resultados Lista de CPFs compatíveis.
         */
        private void atualizarResultados(List<String> resultados) {
            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (String cpf : resultados) {
                listModel.addElement(cpf); // Adiciona cada CPF no modelo
            }
            listResultCpfs.setModel(listModel); // Atualiza a lista exibida
        }

        /**
         * Retorna o painel principal para exibição no JFrame.
         */
        public JPanel getPanel() {
            return panelBuscaCpf;
        }
    }
}