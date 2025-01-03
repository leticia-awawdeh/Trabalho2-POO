package com.GUI;

import com.Backend.*;

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

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        GerenciadorDados.addAtualizacaoListener(() -> carregarEquipamentos());

        dropDwnEquip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String equipamentoSelecionado = (String) dropDwnEquip.getSelectedItem();
                if (!"Selecione o equipamento".equals(equipamentoSelecionado)) {
                    System.out.println("Equipamento selecionado: " + equipamentoSelecionado);
                }
            }
        });

        txtCpfCli.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent event) {
                String cpfDigitado = txtCpfCli.getText();
                Cliente cliente = buscarClientePorCpf(cpfDigitado);

                if (cliente != null) {
                    txtNomeCli.setText(cliente.getNomeCli());
                    txtTelefoneCli.setText(cliente.getTelefoneCli());
                } else {
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

                long diasLocacao = calcularDiasEntreDatas(dataInicio, dataTermino);
                if (diasLocacao <= 0) {
                    JOptionPane.showMessageDialog(regLoc,
                            "Data de término deve ser posterior à data de início.",
                            "Erro", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String cpfCliente = txtCpfCli.getText();
                Cliente cliente = buscarClientePorCpf(cpfCliente);
                if (cliente == null) {
                    JOptionPane.showMessageDialog(regLoc,
                            "Cliente não encontrado. Verifique o CPF e tente novamente.",
                            "Erro", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String nomeEquip = (String) dropDwnEquip.getSelectedItem();
                Equipamento equipamentoSelecionado = GerenciadorDados.getListaEquipamentos()
                        .stream()
                        .filter(eq -> eq.getNome().equals(nomeEquip) && eq.getStatus() == Status.DISPONIVEL)
                        .findFirst()
                        .orElse(null);

                if (equipamentoSelecionado == null) {
                    JOptionPane.showMessageDialog(regLoc,
                            "Equipamento inválido ou indisponível.",
                            "Erro", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Locacao novaLocacao = new Locacao(cliente,
                        equipamentoSelecionado,
                        dataInicio,
                        dataTermino,
                        0.2 // Multa diária de 20%
                );

                equipamentoSelecionado.incrementarFrequenciaAluguel();
                equipamentoSelecionado.setCliente(cliente);
                equipamentoSelecionado.setStatus(Status.ALUGADO);

                GerenciadorDados.adicionarLocacao(novaLocacao);

                GerenciadorDados.notificarAtualizacao();

                // Exibir resumo para o usuário
                JOptionPane.showMessageDialog(regLoc,
                        String.format(
                                "Locação registrada com sucesso.\n\n" +
                                        "Equipamento: %s\n" +
                                        "Data Início: %s | Data Fim: %s\n" +
                                        "Dias Locação: %d dias\n" +
                                        "Valor Diário: R$ %.2f\n" +
                                        "Valor Total: R$ %.2f",
                                equipamentoSelecionado.getNome(),
                                dataInicio.format(dateFormatter),
                                dataTermino.format(dateFormatter),
                                diasLocacao,
                                novaLocacao.getValorDiario(),
                                calcularValorLocacao(novaLocacao.getValorDiario(), diasLocacao)
                        ), "Sucesso", JOptionPane.INFORMATION_MESSAGE
                );
                limparCampos();
            }

        });
    }
    double valorLoc;
    public double calcularValorLocacao(double valorDiario, long dias) {
        valorLoc = valorDiario * dias;
        return valorLoc;
    }

    private static String formatarValorMonetario(double valor) {
        return String.format("R$ %.2f", valor);
    }

    public static long calcularDiasEntreDatas(LocalDate inicio, LocalDate termino) {
        return java.time.temporal.ChronoUnit.DAYS.between(inicio, termino);
    }

    private void atualizarDropdownEquipamentos() {
        dropDwnEquip.removeAllItems();

        dropDwnEquip.addItem("Selecione o equipamento");

        List<Equipamento> equipamentosDisponiveis = GerenciadorDados.getListaEquipamentos();

        for (Equipamento equipamento : equipamentosDisponiveis) {
            if (equipamento.getStatus() == Status.DISPONIVEL) {
                dropDwnEquip.addItem(equipamento.getNome());
            }
        }

        if (dropDwnEquip.getItemCount() == 1) { // Apenas o placeholder
            dropDwnEquip.addItem("Nenhum equipamento disponível");
        }

        dropDwnEquip.setSelectedIndex(0);
    }

    public Cliente buscarClientePorCpf(String cpf) {
        String cpfSemFormatacao = cpf.replaceAll("[^\\d]", "");

        List<Cliente> clientes = GerenciadorDados.getListaClientes();
        return clientes.stream()
                .filter(cli -> cli.getCpfCli().replaceAll("[^\\d]", "").equals(cpfSemFormatacao)) // Compara sem formatação
                .findFirst()
                .orElse(null);
    }

    private void carregarEquipamentos() {
        dropDwnEquip.removeAllItems();

        dropDwnEquip.addItem("Selecione o equipamento");

        List<Equipamento> equipamentos = GerenciadorDados.getListaEquipamentos();

        for (Equipamento equipamento : equipamentos) {
            if (equipamento.getStatus() == Status.DISPONIVEL) {
                dropDwnEquip.addItem(equipamento.getNome());
            }
        }

        if (dropDwnEquip.getItemCount() == 1) { // Apenas o placeholder
            dropDwnEquip.addItem("Nenhum equipamento disponível");
        }

        dropDwnEquip.setSelectedIndex(0);
    }

    private void limparCampos() {

        txtDataInicio.setValue(null);
        txtDataInicio.setText("");
        txtDataTermino.setValue(null);
        txtDataTermino.setText("");
        txtCpfCli.setValue(null);
        txtCpfCli.setText("");
        txtNomeCli.setText("");
        txtTelefoneCli.setText("");

        dropDwnEquip.setSelectedIndex(0);
    }

    public JPanel getPanel() {
        return regLoc;
    }

}
