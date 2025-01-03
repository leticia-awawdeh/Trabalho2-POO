package com.Backend;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GerenciadorDados {
    private static final List<Equipamento> listaEquipamentos = new ArrayList<>();
    private static final List<Cliente> listaClientes = new ArrayList<>();
    private static final List<Locacao> listaLocacoes = new ArrayList<>();

    private static final List<AtualizacaoListener> listeners = new ArrayList<>();

    // ---------------- Métodos para Equipamentos ---------------- //
    public static List<Equipamento> getListaEquipamentos() {
        return listaEquipamentos;
    }

    public static void adicionarEquipamento(Equipamento equipamento) {
        listaEquipamentos.add(equipamento);
        notificarAtualizacao();
    }

    public static void removerEquipamento(Equipamento equipamento) {
        listaEquipamentos.remove(equipamento);
        notificarAtualizacao();
    }

    // ---------------- Métodos para Clientes ---------------- //

    public static List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public static List<Locacao> getListaLocacoes() {
        return listaLocacoes;
    }

    public static void adicionarLocacao(Locacao locacao) {
        listaLocacoes.add(locacao);
    }

    public static void listarLocacoes() {
        listaLocacoes.forEach(loc -> System.out.println("Cliente CPF: " + loc.getCliente().getCpfCli()));
    }

    public static List<Cliente> calcularMultasPorCliente() {
        // Itera sobre cada cliente registrado
        for (Cliente cliente : listaClientes) {
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
            if (locacao.getDataDevolucao().isAfter(locacao.getDataPrevistaDevolucao())) {
                long diasAtraso = locacao.getDataPrevistaDevolucao().until(locacao.getDataDevolucao()).getDays();
                totalMultas += diasAtraso * 10.0;
            }
        }
        return totalMultas;
    }

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
        notificarAtualizacao();
    }

    public static void removerCliente(Cliente cliente) {
        listaClientes.remove(cliente);
        notificarAtualizacao();
    }

    public static boolean isCpfCadastrado(String cpf) {
        for (Cliente cliente : listaClientes) {
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

    // ---------------- Notificações (listeners) ---------------- //

    public static void addAtualizacaoListener(AtualizacaoListener listener) {
        listeners.add(listener);
    }

    public static void removeAtualizacaoListener(AtualizacaoListener listener) {
        listeners.remove(listener);
    }

    public static void notificarAtualizacao() {
        for (AtualizacaoListener listener : listeners) {
            listener.onAtualizacao();
        }
    }

    // ---------------- Interface Listener ---------------- //

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

        private List<String> buscarCpfsCompatíveis(String inputCpf) {
            List<Cliente> listaClientes = GerenciadorDados.getListaClientes();

            // Realiza a filtragem baseada no input fornecido
            return listaClientes.stream()
                    .map(Cliente::getCpfCli)  // Obtém todos os CPFs
                    .filter(cpf -> cpf.contains(inputCpf)) // Filtra os que contêm o texto digitado
                    .collect(Collectors.toList()); // Converte para lista
        }

        private void atualizarResultados(List<String> resultados) {
            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (String cpf : resultados) {
                listModel.addElement(cpf);
            }
            listResultCpfs.setModel(listModel); // Atualiza a lista exibida
        }

        public JPanel getPanel() {
            return panelBuscaCpf;
        }
    }

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