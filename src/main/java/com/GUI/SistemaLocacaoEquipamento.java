package com.GUI;

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
    private JTextField txtCpf;
    private JTextField txtTelefone;
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

    private List<CadastroCli> clientes = new ArrayList<>();

    public SistemaLocacaoEquipamento() {
        btnSalvarCadCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = txtNomeCli.getText();
                String cpf = txtCpf.getText();
                String telefone = txtTelefone.getText();
                if (nome == null || nome.trim().isEmpty() ||
                        cpf == null || cpf.trim().isEmpty() ||
                        telefone == null || telefone.trim().isEmpty()) {

                    JOptionPane.showMessageDialog(panel1, "Todos os campos são obrigatórios. Por favor, preencha-os!", "Erro", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                CadastroCli cliente = new CadastroCli(nome, cpf, telefone);

                clientes.add(cliente);

                JOptionPane.showMessageDialog(panel1, "Cliente cadastrado com sucesso:\n" + cliente, "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                // Limpa os campos da GUI após o cadastro
                txtNomeCli.setText("Digite o nome do cliente");
                txtCpf.setText("000.000.000-00");
                txtTelefone.setText("(00) 00000-0000");
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
