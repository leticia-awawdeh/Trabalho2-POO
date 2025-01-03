package com.GUI;

import com.Backend.CadastroCli;
import com.Backend.Equipamento;
import com.Backend.GerenciadorDados;
import com.Backend.Status;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class RegLocacao {
    private JComboBox<String> dropDwnEquip;
    private JButton btnRegistrarLoc;
    private JButton btnCancelarLoc;
    private JFormattedTextField txtDataTermino;
    private JFormattedTextField txtDataInicio;
    private JTextField txtTelefoneCli;
    private JFormattedTextField txtCpfCli;
    private JTextField txtNomeCli;
    private JPanel regLoc;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public RegLocacao() {
        carregarEquipamentos();

        try {
            // Aplicando máscara para CPF
            MaskFormatter cpfMask = new MaskFormatter("###.###.###-##");
            cpfMask.setPlaceholderCharacter('_');
            txtCpfCli.setFormatterFactory(new DefaultFormatterFactory(cpfMask));

            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dateMask.setPlaceholderCharacter('_');

            txtDataInicio.setFormatterFactory(new DefaultFormatterFactory(dateMask));
            txtDataTermino.setFormatterFactory(new DefaultFormatterFactory(dateMask));

        } catch (Exception e) {
            e.printStackTrace();
        }

        GerenciadorDados.addAtualizacaoListener(() -> carregarEquipamentos());

        dropDwnEquip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String equipamentoSelecionado = (String) dropDwnEquip.getSelectedItem();
                if (!"Selecione o equipamento".equals(equipamentoSelecionado)) {
                    System.out.println("Equipamento selecionado: " + equipamentoSelecionado);
                }
            }
        });

        txtCpfCli.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String cpfDigitado = txtCpfCli.getText(); // Obtém o CPF digitado com máscara.
                CadastroCli cliente = buscarClientePorCpf(cpfDigitado);

                if (cliente != null) {
                    // Preenche os campos se o cliente for encontrado
                    txtNomeCli.setText(cliente.getNomeCli());
                    txtTelefoneCli.setText(cliente.getTelefone());
                } else {
                    // Limpa os campos caso o cliente não seja encontrado
                    txtNomeCli.setText("");
                    txtTelefoneCli.setText("");
                }
            }
        });


        btnRegistrarLoc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String dataInicioStr = txtDataInicio.getText();
                String dataTerminoStr = txtDataTermino.getText();

                // Validar e converter as datas
                LocalDate dataInicio;
                LocalDate dataTermino;
                try {
                    dataInicio = LocalDate.parse(dataInicioStr, dateFormatter);
                    dataTermino = LocalDate.parse(dataTerminoStr, dateFormatter);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(regLoc,
                            "Por favor, insira as datas no formato DD/MM/AAAA.",
                            "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Calcular a quantidade de dias de locação
                long diasLocacao = calcularDiasEntreDatas(dataInicio, dataTermino);
                if (diasLocacao < 0) {
                    JOptionPane.showMessageDialog(regLoc,
                            "A data de término não pode ser anterior à data de início.",
                            "Erro de Datas", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Valida se o cliente foi encontrado pelo CPF
                String cpfCliente = txtCpfCli.getText();
                CadastroCli cliente = buscarClientePorCpf(cpfCliente);

                if (cliente == null) {
                    JOptionPane.showMessageDialog(regLoc,
                            "Cliente não encontrado. Verifique o CPF e tente novamente.",
                            "Erro", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Obter o equipamento selecionado
                String nomeEquip = (String) dropDwnEquip.getSelectedItem();
                if (nomeEquip == null || nomeEquip.equals("Selecione o equipamento") || nomeEquip.equals("Nenhum equipamento disponível")) {
                    JOptionPane.showMessageDialog(regLoc,
                            "Por favor, selecione um equipamento válido para registrar a locação.",
                            "Erro", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Buscar equipamento correspondente
                Equipamento equipamentoSelecionado = GerenciadorDados.getListaEquipamentos()
                        .stream()
                        .filter(equip -> equip.getNome().equals(nomeEquip))
                        .findFirst()
                        .orElse(null);

                if (equipamentoSelecionado == null || equipamentoSelecionado.getStatus() != Status.DISPONIVEL) {
                    JOptionPane.showMessageDialog(regLoc,
                            "O equipamento selecionado já está alugado ou indisponível.",
                            "Erro", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Define o status como alugado e vincula o cliente
                equipamentoSelecionado.setStatus(Status.ALUGADO);

                // *** Configura a data prevista de devolução ***
                equipamentoSelecionado.setDataPrevistaDevolucao(dataTermino);

                // Registrar locação no sistema
                GerenciadorDados.registrarLocacao(cliente, equipamentoSelecionado);

                // Exibir informações da locação para o usuário
                double valorDiario = equipamentoSelecionado.getValorDiario(); // Supondo que Equipamento tenha um método getValorDiario()
                double valorTotal = valorDiario * diasLocacao;

                JOptionPane.showMessageDialog(regLoc,
                        "Locação registrada com sucesso!\n" +
                                "Equipamento: " + equipamentoSelecionado.getNome() + "\n" +
                                "Data de Início: " + dataInicio.format(dateFormatter) + "\n" +
                                "Data Prevista de Devolução: " + dataTermino.format(dateFormatter) + "\n" +
                                "Quantidade de Dias: " + diasLocacao + "\n",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private long calcularDiasEntreDatas(LocalDate inicio, LocalDate termino) {
        return java.time.temporal.ChronoUnit.DAYS.between(inicio, termino);
    }

    private void atualizarDropdownEquipamentos() {
        // Limpa os itens do dropdown
        dropDwnEquip.removeAllItems();

        dropDwnEquip.addItem("Selecione o equipamento");

        // Obtém todos os equipamentos disponíveis
        List<Equipamento> equipamentosDisponiveis = GerenciadorDados.getListaEquipamentos();

        for (Equipamento equipamento : equipamentosDisponiveis) {
            // Adiciona apenas equipamentos com Status DISPONIVEL
            if (equipamento.getStatus() == Status.DISPONIVEL) {
                dropDwnEquip.addItem(equipamento.getNome()); // Adiciona o nome ao dropdown
            }
        }

        // Se não houver nenhum equipamento disponível, exibe mensagem ou mantém o dropdown vazio
        if (dropDwnEquip.getItemCount() == 1) { // Apenas o placeholder
            dropDwnEquip.addItem("Nenhum equipamento disponível");
        }

        dropDwnEquip.setSelectedIndex(0);
    }

    private CadastroCli buscarClientePorCpf(String cpf) {
        // Remove a formatação para buscar apenas pelos números
        String cpfSemFormatacao = cpf.replaceAll("[^\\d]", "");

        List<CadastroCli> clientes = GerenciadorDados.getListaClientes();
        return clientes.stream()
                .filter(cli -> cli.getCpf().replaceAll("[^\\d]", "").equals(cpfSemFormatacao)) // Compara sem formatação
                .findFirst()
                .orElse(null);
    }

    private void carregarEquipamentos() {
        // Limpa todos os itens do JComboBox
        dropDwnEquip.removeAllItems();

        // Adiciona o item "placeholder"
        dropDwnEquip.addItem("Selecione o equipamento");

        // Obtém a lista de equipamentos do sistema
        List<Equipamento> equipamentos = GerenciadorDados.getListaEquipamentos();

        // Adiciona cada equipamento ao JComboBox
        for (Equipamento equipamento : equipamentos) {
            dropDwnEquip.addItem(equipamento.getNome());
        }

        // Define como selecionado o primeiro item (placeholder)
        dropDwnEquip.setSelectedIndex(0);
    }


    public JPanel getPanel() {
        return regLoc;
    }
}
