package com.GUI;

import com.Backend.Equipamento;
import com.Backend.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class SistemaLocacaoEquipamento extends JFrame {
    private JPanel panel1;
    private JTabbedPane PainelSis;
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
    private JButton baixarRegistroDeClientesButton;
    private JButton baixarRegistroDeEquipamentosButton;

    private List<CadastroCli> clientes;
    private List<Equipamento> equipamentos;

    public SistemaLocacaoEquipamento() {
        clientes = new ArrayList<>();
        equipamentos = new ArrayList<>();

        // Configurando a interface principal
        PainelSis = new JTabbedPane();

        // Adicionar o painel do cadastro de cliente (usando o novo arquivo CadastroCliente)
        CadastroCliente cadastroCliente = new CadastroCliente(clientes);
        PainelSis.addTab("Cadastro de Cliente", cadastroCliente.getPanel());

        CadastroEquipamento cadastroEquipamento = new CadastroEquipamento(equipamentos);
        PainelSis.addTab("Cadastro de Equipamentos", cadastroEquipamento.getPanel());

        setContentPane(PainelSis);
        setTitle("Sistema de Locação de Equipamentos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null); // Centraliza a janela na tela
    }

    public static void main(String[] args) {
        // Define o estilo da interface gráfica conforme o sistema operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            try {
                SistemaLocacaoEquipamento frame = new SistemaLocacaoEquipamento();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Configuração de fechamento
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace(); // Registrar exceções adequadamente em produção
            }
        });
    }
}

