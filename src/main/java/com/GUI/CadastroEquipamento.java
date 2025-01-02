package com.GUI;

import com.Backend.Equipamento;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CadastroEquipamento {
    private JTextField txtNomeEquip;
    private JTextField txtDesricao;
    private JTextField txtLocDiaria;
    private JButton btnCadEquip;
    private JButton btnCancelarCadEquip;
    private JPanel panelCadEquip;

    public CadastroEquipamento(List<Equipamento> equipamentos) {
        btnCadEquip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomeEquip = txtNomeEquip.getText();
                String descricaoEquip = txtDesricao.getText();

                try {
                    // Remove pontos e substitui vírgula por ponto para conversão numérica
                    String valorDigitado = txtLocDiaria.getText().replace(".", "").replace(",", ".");
                    double valorLocacao = Double.parseDouble(valorDigitado);

                    // Lógica de cadastro e exibição
                    Equipamento equipamento = new Equipamento(nomeEquip, descricaoEquip, valorLocacao);
                    equipamentos.add(equipamento);

                    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                    String valorFormatado = formatter.format(valorLocacao);

                    JOptionPane.showMessageDialog(panelCadEquip, "Equipamento registrado com sucesso: \n" +
                            "Nome: " + nomeEquip + "\n" +
                            "Descrição: " + descricaoEquip + "\n" +
                            "Valor de Locação Diária: " + valorFormatado);

                    // Limpa os campos
                    txtNomeEquip.setText("");
                    txtDesricao.setText("");
                    txtLocDiaria.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panelCadEquip, "Por favor, insira um valor válido no campo de locação diária.",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        btnCancelarCadEquip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(panelCadEquip, "Cadastro de Equipamento Cancelado", "Cancelado", JOptionPane.INFORMATION_MESSAGE);

                txtNomeEquip.setText("");
                txtDesricao.setText("");
                txtLocDiaria.setText("");
            }
        });
        txtLocDiaria.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtLocDiaria.getDocument().addDocumentListener(new DocumentListener() {
                    private boolean isUpdating = false; // Evita loops durante o update

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        formatInput();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        formatInput();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        formatInput();
                    }

                    private void formatInput() {
                        if (isUpdating) return;

                        isUpdating = true;
                        String input = txtLocDiaria.getText();

                        // Remove caracteres indesejados (mantém apenas números e vírgula)
                        input = input.replace(".", "").replace(",", "");

                        if (!input.isEmpty()) {
                            // Formata o valor automaticamente com ponto e vírgula
                            double value = Double.parseDouble(input) / 100; // Ajusta para casas decimais
                            DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
                            txtLocDiaria.setText(decimalFormat.format(value));
                        }

                        isUpdating = false;
                    }
                });
            }
        });
    }
    public JPanel getPanel() {
        return panelCadEquip;
    }
}
