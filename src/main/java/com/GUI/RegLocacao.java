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
import java.util.List;

public class RegLocacao {
    private JComboBox<String> dropDwnEquip;
    private JButton btnRegistrarLoc;
    private JButton btnCancelarLoc;
    private JTextField txtDataTermino;
    private JTextField txtDataInicio;
    private JTextField txtTelefoneCli;
    private JFormattedTextField txtCpfCli;
    private JTextField txtNomeCli;
    private JPanel regLoc;

    public RegLocacao() {
        carregarEquipamentos();

        try {
            // Aplicando máscara para CPF
            MaskFormatter cpfMask = new MaskFormatter("###.###.###-##");
            cpfMask.setPlaceholderCharacter('_');
            txtCpfCli.setFormatterFactory(new DefaultFormatterFactory(cpfMask));

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
                // Valida se o cliente foi encontrado pelo CPF
                String cpfCliente = txtCpfCli.getText();
                CadastroCli cliente = buscarClientePorCpf(cpfCliente);

                if (cliente == null) {
                    JOptionPane.showMessageDialog(regLoc,
                            "Cliente não encontrado. Verifique o CPF e tente novamente.",
                            "Erro", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Obtém o equipamento selecionado
                String nomeEquip = (String) dropDwnEquip.getSelectedItem();

                if (nomeEquip == null || nomeEquip.equals("Selecione o equipamento") || nomeEquip.equals("Nenhum equipamento disponível")) {
                    JOptionPane.showMessageDialog(regLoc,
                            "Por favor, selecione um equipamento válido para registrar a locação.",
                            "Erro", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Busca o equipamento correspondente
                List<Equipamento> equipamentos = GerenciadorDados.getListaEquipamentos();
                for (Equipamento equipamento : equipamentos) {
                    if (equipamento.getNome().equals(nomeEquip)) {
                        if (equipamento.getStatus() == Status.DISPONIVEL) {
                            // Marca o equipamento como alugado
                            equipamento.setStatus(Status.ALUGADO);

                            // Notifica listeners e atualiza a interface
                            GerenciadorDados.notificarAtualizacao();
                            atualizarDropdownEquipamentos();

                            JOptionPane.showMessageDialog(regLoc,
                                    "Locação registrada com sucesso para o cliente: " + cliente.getNomeCli() +
                                            "\nEquipamento: " + equipamento.getNome(),
                                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(regLoc,
                                    "O equipamento selecionado já está alugado ou indisponível.",
                                    "Erro", JOptionPane.WARNING_MESSAGE);
                        }
                        return;
                    }
                }

                // Caso o equipamento não seja encontrado
                JOptionPane.showMessageDialog(regLoc,
                        "Erro: Equipamento não encontrado.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
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
