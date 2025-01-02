package com.GUI;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import com.Backend.Equipamento;
import com.Backend.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SistemaLocacaoEquipamento extends JFrame {
    private JPanel panel1;
    private JTabbedPane PainelSis;
    private JTextField txtNomeEquip;
    private JTextField txtDesricao;
    private JTextField txtLocDiaria;
    private JButton btnCadEquip;
    private JButton btnCancelarCadEquip;
    private JComboBox comboBox1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JButton registrarLocaçãoButton;
    private JButton cancelarLocaçãoButton;
    private JTextField textField6;
    private JTextField textField7;
    private JButton buscarButton1;
    private JButton buscarButton;
    private JButton confirmarDevoluçãoButton;
    private JComboBox comboBox2;

    private List<CadastroCli> clientes;
    private List<Equipamento> equipamentos;

    public SistemaLocacaoEquipamento() {
        clientes = new ArrayList<>();
        equipamentos = new ArrayList<>();

//        // Configurando a interface principal
//        PainelSis = new JTabbedPane();
//
//        // Adicionar o painel do cadastro de cliente (usando o novo arquivo CadastroCliente)
//        CadastroCliente cadastroCliente = new CadastroCliente(clientes);
//        PainelSis.addTab("Cadastro de Cliente", cadastroCliente.getPanel());
//
//        // Outras abas podem ser adicionadas aqui, como cadastro de equipamento ou buscas...
//
//        setContentPane(PainelSis);
//        setTitle("Sistema de Locação de Equipamentos");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(600, 400);
//        setLocationRelativeTo(null); // Centraliza a janela na tela

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

                    JOptionPane.showMessageDialog(panel1, "Equipamento registrado com sucesso: \n" +
                            "Nome: " + nomeEquip + "\n" +
                            "Descrição: " + descricaoEquip + "\n" +
                            "Valor de Locação Diária: " + valorFormatado);

                    // Limpa os campos
                    txtNomeEquip.setText("");
                    txtDesricao.setText("");
                    txtLocDiaria.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel1, "Por favor, insira um valor válido no campo de locação diária.",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        btnCancelarCadEquip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(panel1, "Cadastro de Equipamento Cancelado", "Cancelado", JOptionPane.INFORMATION_MESSAGE);

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

    public static void main(String[] args) {
        // Define o estilo da interface gráfica conforme o sistema operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Cria e exibe a janela da aplicação
        SwingUtilities.invokeLater(() -> {
            SistemaLocacaoEquipamento frame = new SistemaLocacaoEquipamento();
            frame.setTitle("Sistema de Locação de Equipamentos");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(frame.panel1); // Adiciona o painel principal
            frame.pack(); // Ajusta o tamanho da janela aos componentes
            frame.setLocationRelativeTo(null); // Janela centralizada na tela
            frame.setVisible(true); // Torna a janela visível
        });
    }
}
