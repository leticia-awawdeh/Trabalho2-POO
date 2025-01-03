package com.GUI;

import com.Backend.GerenciadorDados;
import com.Backend.Equipamento;
import com.Backend.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.FocusAdapter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SistemaLocacaoEquipamento extends JFrame {
    private JPanel panel1;
    private JTabbedPane PainelSis;
    private JComboBox<String> dropDwnEquip;
    private JComboBox comboBox2;
    private JButton baixarRegistroDeClientesButton;
    private JButton baixarRegistroDeEquipamentosButton;

    private List<CadastroCli> clientes;
    private List<Equipamento> equipamentos;


    public SistemaLocacaoEquipamento() {
        clientes = new ArrayList<>();
        equipamentos = new ArrayList<>();


        //utilizado para concatenação de painéis

//        // Configurando a interface principal
        PainelSis = new JTabbedPane();

//
//
        CadastroCliente cadastroCliente = new CadastroCliente(clientes);
        PainelSis.addTab("Cadastro de Cliente", cadastroCliente.getPanel());
//
        CadastroEquipamento cadastroEquipamento = new CadastroEquipamento();
        PainelSis.addTab("Cadastro de Equipamentos", cadastroEquipamento.getPanel());

        RegLocacao regLocacao = new RegLocacao();
        PainelSis.addTab("Registro de Locação", regLocacao.getPanel());

        DevolucaoEquipamentos devolucaoEquipamentos = new DevolucaoEquipamentos();
        PainelSis.addTab("Devolução de Equipamentos", devolucaoEquipamentos.getPanel());
//
        setContentPane(PainelSis);
        setTitle("Sistema de Locação de Equipamentos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null); // Centraliza a janela na tela

    }

    public static void main (String[]args){

        //utilizado na concatenação dos painéis
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