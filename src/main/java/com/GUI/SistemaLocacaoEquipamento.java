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
    private List<CadastroEquip> equipamentos;

    public SistemaLocacaoEquipamento() {
        clientes = new ArrayList<>();
        equipamentos = new ArrayList<>();

        // Configurando a interface principal
        PainelSis = new JTabbedPane();

        // Adicionar o painel do cadastro de cliente (usando o novo arquivo CadastroCliente)
        CadastroCliente cadastroCliente = new CadastroCliente(clientes);
        PainelSis.addTab("Cadastro de Cliente", cadastroCliente.getPanel());

        // Outras abas podem ser adicionadas aqui, como cadastro de equipamento ou buscas...

        setContentPane(PainelSis);
        setTitle("Sistema de Locação de Equipamentos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null); // Centraliza a janela na tela
    }

    public static void main(String[] args) {
        // Define o estilo visual do sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Mostrando a janela principal
        SwingUtilities.invokeLater(() -> {
            SistemaLocacaoEquipamento frame = new SistemaLocacaoEquipamento();
            frame.setVisible(true);
        });
    }
}
