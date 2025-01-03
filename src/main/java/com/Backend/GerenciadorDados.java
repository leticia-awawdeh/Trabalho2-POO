package com.Backend;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GerenciadorDados {
    private static List<Equipamento> listaEquipamentos = new ArrayList<>();
    private static List<Cliente> listaClientes = new ArrayList<>();
    private static List<Locacao> listaLocacoes = new ArrayList<>();// Lista de clientes

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
    public static List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public static List<Locacao> getListaLocacoes() {
        return listaLocacoes;
    }

    public static void adicionarLocacao(Locacao locacao) {
        listaLocacoes.add(locacao);
    }

    // Opcional: Para debug, exibir locações registradas
    public static void listarLocacoes() {
        listaLocacoes.forEach(loc -> System.out.println("Cliente CPF: " + loc.getCliente().getCpfCli()));
    }

    public static List<Cliente> calcularMultasPorCliente() {
        // Itera sobre cada cliente registrado
        for (Cliente cliente : listaClientes) {
            // Seleciona as locações associadas a este cliente
            List<Locacao> locacoesCliente = listaLocacoes.stream()
                    .filter(locacao -> locacao.getCliente() != null && locacao.getCliente().equals(cliente))
                    .toList();

            // Calcula o total de multas por cliente
            double totalMultas = locacoesCliente.stream()
                    .mapToDouble(Locacao::calcularMulta)
                    .sum();

            // Atualiza apenas se houver multas acumuladas
            cliente.setMultasTotais(totalMultas);
        }

        // Retorna clientes com multas acumuladas maiores que 0
        return listaClientes.stream()
                .filter(cli -> cli.getMultasTotais() > 0) // Apenas clientes com multas
                .toList();
    }

    private static double getTotalMultas() {
        double totalMultas = 0.0;

        for (Locacao locacao : listaLocacoes) {
            // Verifica se a data de devolução é posterior à permitida
            if (locacao.getDataDevolucao().isAfter(locacao.getDataPrevistaDevolucao())) {
                // Calcula a diferença de dias entre a devolução e a data prevista
                long diasAtraso = locacao.getDataPrevistaDevolucao().until(locacao.getDataDevolucao()).getDays();
                // Calcula multa para os dias atrasados (exemplo: R$ 10,00 por dia de atraso)
                totalMultas += diasAtraso * 10.0;
            }
        }
        return totalMultas;
    }

    /**
     * Adiciona um cliente à lista de clientes.
     */
    public static void adicionarCliente(Cliente cliente) {
        // Verifica se o CPF já está cadastrado
        boolean cpfCadastrado = listaClientes.stream()
                .anyMatch(cli -> cli.getCpfCli().equals(cliente.getCpfCli()));

        if (cpfCadastrado) {
            throw new IllegalArgumentException("Cliente com CPF já cadastrado.");
        } if (cliente == null){
            throw new IllegalArgumentException("Cliente não pode ser nulo.");
        }

        listaClientes.add(cliente);
        notificarAtualizacao(); // Notifica listeners sobre a mudança
    }

    /**
     * Remove um cliente da lista de clientes.
     */
    public static void removerCliente(Cliente cliente) {
        listaClientes.remove(cliente);
        notificarAtualizacao();
    }

    public static boolean isCpfCadastrado(String cpf) {
        for (Cliente cliente : listaClientes) { // "listaClientes" é o nome correto da variável
            if (cliente.getCpfCli().equals(cpf)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTelefoneCadastrado(String telefone){
        for (Cliente cliente : listaClientes){
            if (cliente.getTelefoneCli().equals(telefone)){
                return true;
            }
        }
        return false;
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
            List<Cliente> listaClientes = GerenciadorDados.getListaClientes();

            // Realiza a filtragem baseada no input fornecido
            return listaClientes.stream()
                    .map(Cliente::getCpfCli)  // Obtém todos os CPFs
                    .filter(cpf -> ((String) cpf).contains(inputCpf)) // Filtra os que contêm o texto digitado
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

    // Método para associar um cliente a um equipamento alugado
    public static void registrarLocacao(Cliente cliente, Equipamento equipamento) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo.");
        }

        if (equipamento == null) {
            throw new IllegalArgumentException("O equipamento não pode ser nulo.");
        }

        equipamento.setCliente(cliente);
        equipamento.setStatus(Status.ALUGADO);
        notificarAtualizacao(); // Notifica os componentes sobre a alteração
    }

    // Método para encontrar um equipamento alugado por CPF do cliente
    public static Optional<Equipamento> buscarEquipamentoAlugadoPorCpf(String cpf) {
        String cpfDigitado = cpf.replaceAll("[^\\d]", ""); // Remove formatação

        return listaEquipamentos
                .stream()
                .filter(equip ->
                        equip.getStatus() == Status.ALUGADO &&
                                equip.getCliente() != null &&
                                equip.getCliente().getCpfCli().replaceAll("[^\\d]", "").equals(cpfDigitado))
                .findFirst();
    }
}