package com.GUI;

import javax.swing.text.MaskFormatter;
import javax.swing.text.DefaultFormatterFactory;
import com.Backend.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.util.List;

public class SistemaLocacaoEquipamento extends JFrame {
    private JPanel panel1;
    private JTabbedPane PainelSis;
    private JTextField txtNomeCli;
    private JFormattedTextField txtCpf;
    private JFormattedTextField txtTelefone;
    private JButton btnSalvarCadCliente;
    private JButton cancelarCadastroButton;
    private JTextField txtNomeEquip;
    private JTextField txtDesricao;
    private JTextField txtLocDiaria;
    private JButton cadastrarEquipamentoButton;
    private JButton cancelarCadastroDoEquipamentoButton;
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

    public SistemaLocacaoEquipamento() {

        clientes = new ArrayList<>();

        try {
            // Máscara para CPF
            MaskFormatter cpfMask = new MaskFormatter("###.###.###-##");
            cpfMask.setPlaceholderCharacter('_'); // Caracter mostrado nas posições vazias
            txtCpf.setFormatterFactory(new DefaultFormatterFactory(cpfMask)); // Aplica a máscara no campo existente

            // Máscara para Telefone
            MaskFormatter telefoneMask = new MaskFormatter("(##) #####-####");
            telefoneMask.setPlaceholderCharacter('_'); // Caracter mostrado nas posições vazias
            txtTelefone.setFormatterFactory(new DefaultFormatterFactory(telefoneMask));
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnSalvarCadCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomeCli = txtNomeCli.getText();
                String cpfCli = txtCpf.getText(); // CPF formatado
                String telefoneCli = txtTelefone.getText();

                // Verificação de campos vazios
                if (nomeCli == null || nomeCli.trim().isEmpty() ||
                        cpfCli == null || cpfCli.trim().isEmpty() ||
                        telefoneCli == null || telefoneCli.trim().isEmpty()) {

                    JOptionPane.showMessageDialog(panel1,
                            "Todos os campos são obrigatórios. Por favor, preencha-os!",
                            "Erro", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                CadastroCli cliente = new CadastroCli(nomeCli, cpfCli, telefoneCli);
                clientes.add(cliente);

                JOptionPane.showMessageDialog(panel1,
                        "Cliente cadastrado com sucesso:\n"
                                + "Nome: " + nomeCli + "\n"
                                + "CPF: " + txtCpf.getText() + "\n" // CPF formatado
                                + "Telefone: " + txtTelefone.getText(), // Telefone formatado
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);


                // Limpar campos após o cadastro
                txtNomeCli.setText("");
                txtCpf.setValue(null); // Limpa a máscara
                txtTelefone.setValue(null); // Limpa a máscara
            }
        });

        cancelarCadastroButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(panel1, "Cadastro Cancelado.", "Cancelado", JOptionPane.INFORMATION_MESSAGE);
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
