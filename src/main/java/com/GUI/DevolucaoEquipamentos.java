package com.GUI;

import com.Backend.*;
import com.Backend.Utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class DevolucaoEquipamentos {
    private JFormattedTextField txtCpf;
    private JButton btnConfDevolucao;
    private JButton btnBuscarLoc;
    private JPanel panelDevolucao;
    private JLabel lblResult;
    private JPanel panelResult;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public DevolucaoEquipamentos() {
        try {
            // Aplicando máscara para CPF
            MaskFormatter cpfMask = new MaskFormatter("###.###.###-##");
            cpfMask.setPlaceholderCharacter('_');
            txtCpf.setFormatterFactory(new DefaultFormatterFactory(cpfMask));
        } catch (Exception e) {
            e.printStackTrace();
        }


        btnConfDevolucao.addActionListener(e -> confirmarDevolucao());

        btnBuscarLoc.addActionListener(e -> buscarLocacao(txtCpf.getText()));
    }

    // Método para buscar locações por CPF
    private void buscarLocacao(String cpf) {
        String cpfDigitado = txtCpf.getText().replaceAll("[^\\d]", ""); // Remove pontuações

        // Busca cliente pelo CPF
        Optional<CadastroCli> clienteOpt = GerenciadorDados.getListaClientes()
                .stream()
                .filter(cli -> cli.getCpf().replaceAll("[^\\d]", "").equals(cpfDigitado))
                .findFirst();

        if (clienteOpt.isEmpty()) {
            JOptionPane.showMessageDialog(panelDevolucao,
                    "Cliente não encontrado. Confira o CPF e tente novamente.",
                    "Erro", JOptionPane.WARNING_MESSAGE);
            lblResult.setText("");
            return;
        }

        CadastroCli cliente = clienteOpt.get();

        // Buscar equipamento alugado do cliente
        Optional<Equipamento> equipamentoOpt = GerenciadorDados.buscarEquipamentoAlugadoPorCpf(cpfDigitado);

        if (equipamentoOpt.isEmpty()) {
            JOptionPane.showMessageDialog(panelDevolucao,
                    "Nenhum equipamento alugado encontrado para esse cliente.",
                    "Erro", JOptionPane.WARNING_MESSAGE);
            lblResult.setText("");
            return;
        }

        Equipamento equipamento = equipamentoOpt.get();

        // Exibir detalhes no painel
        lblResult.setText(String.format(
                "<html>Equipamento: %s<br>" +
                        "Cliente: %s<br>" +
                        "Data de Empréstimo: %s<br>" +
                        "Data Prevista de Devolução: %s<br>" +
                        "Valor da Locação: R$ %s</html>",
                equipamento.getNome(),
                cliente.getNomeCli(),
                Locacao.getDataInicio().format(dateFormatter),
                Locacao.getDataPrevistaDevolucao().format(dateFormatter),
                Utils.formatarMonetario(equipamento.getValorDiario())

        ));

        // Armazenar o equipamento no painel
        panelResult.putClientProperty("equipamentoSelecionado", equipamento);
    }

    // Método para confirmar devolução
    private void confirmarDevolucao() {
        Object equipamentoObj = panelResult.getClientProperty("equipamentoSelecionado");

        if (equipamentoObj == null || !(equipamentoObj instanceof Equipamento)) {
            JOptionPane.showMessageDialog(panelDevolucao,
                    "Nenhum equipamento selecionado ou encontrado. Tente buscar novamente.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Equipamento equipamento = (Equipamento) equipamentoObj;

        equipamento.setStatus(Status.DISPONIVEL);
        equipamento.setCliente(null);

        GerenciadorDados.notificarAtualizacao();

        // Limpa a seleção no painel
        panelResult.putClientProperty("equipamentoSelecionado", null);


        // Obtém a data de devolução diretamente do sistema
        LocalDate dataDevolucao = LocalDate.now();

        // Calcular multa e total a pagar
        LocalDate dataTerminoLocacao = Locacao.getDataPrevistaDevolucao();
        long diasExcedentes = java.time.temporal.ChronoUnit.DAYS.between(dataTerminoLocacao, dataDevolucao);
        double multa = diasExcedentes > 0 ? diasExcedentes * (equipamento.getValorDiario() * 0.2) : 0.0;
        double total = equipamento.getValorDiario() + multa;

        if (diasExcedentes > 0) { // Se houver dias excedentes
            // Vincula a multa ao cliente correspondente
            CadastroCli cliente = equipamento.getCliente(); // Obtém o cliente associado ao equipamento
            if (cliente != null) {
                // Cria uma nova instância de multa e adiciona à lista de multas do cliente
                Multa novaMulta = new Multa(
                        "Atraso de " + diasExcedentes + " dia(s) na devolução do equipamento " +
                                equipamento.getNome() + ".", multa);
                cliente.getListaMultas().add(novaMulta);

                // Atualiza o total de multas do cliente
                double multasTotais = cliente.getListaMultas()
                        .stream()
                        .mapToDouble(Multa::getValor)
                        .sum();
                cliente.setMultasTotais(multasTotais);
            }
        }

        // Atualizar status para "Disponível"
        equipamento.setStatus(Status.DISPONIVEL);
        equipamento.setCliente(null); // Remove o vínculo com o cliente
        GerenciadorDados.notificarAtualizacao();

        // Exibir resumo no lblResult
        lblResult.setText(String.format(
                "<html>Resumo da Devolução:<br><br>" +
                        "Equipamento: %s<br>" +
                        "Data de Término: %s<br>" +
                        "Data de Devolução: %s<br>" +
                        "Multa por atraso: R$ %s<br>" +
                        "Valor do Aluguel: R$ %s<br>" +
                        "Total a Pagar: R$ %s</html>",
                equipamento.getNome(),
                dataTerminoLocacao.format(dateFormatter),
                dataDevolucao.format(dateFormatter),
                Utils.formatarMonetario(multa), // Formata multa
                Utils.formatarMonetario(Locacao.getValorDiario()), // Formata valor diário
                Utils.formatarMonetario(total) // Formata total
        ));

        // Limpar a seleção no painel
        panelResult.putClientProperty("equipamentoSelecionado", null);
    }



    public JPanel getPanel() {
        return panelDevolucao;
    }
}