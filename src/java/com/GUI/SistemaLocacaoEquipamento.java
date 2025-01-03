package com.GUI;

import com.Backend.Cliente;
import com.Backend.Equipamento;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class SistemaLocacaoEquipamento extends JFrame {
    private JPanel panel1;
    private JTabbedPane PainelSis;

    private final List<Cliente> clientes;
    private final List<Equipamento> equipamentos;


    public SistemaLocacaoEquipamento() {
        clientes = new ArrayList<>();
        equipamentos = new ArrayList<>();


        //utilizado para concatenação de painéis
        // Configurando a interface principal
        PainelSis = new JTabbedPane();

        CadastroCliente cadastroCliente = new CadastroCliente(clientes);
        PainelSis.addTab("Cadastro de Cliente", cadastroCliente.getPanel());

        CadastroEquipamento cadastroEquipamento = new CadastroEquipamento();
        PainelSis.addTab("Cadastro de Equipamentos", cadastroEquipamento.getPanel());

        RegLocacao regLocacao = new RegLocacao();
        PainelSis.addTab("Registro de Locação", regLocacao.getPanel());

        DevolucaoEquipamentos devolucaoEquipamentos = new DevolucaoEquipamentos();
        PainelSis.addTab("Devolução de Equipamentos", devolucaoEquipamentos.getPanel());

        GerarRelatorio gerarRelatorio = new GerarRelatorio();
        PainelSis.addTab("Gerar Relatório", gerarRelatorio.getPanel());

        setContentPane(PainelSis);
        setTitle("Sistema de Locação de Equipamentos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

    }

    public static void main (String[]args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            try {
                SistemaLocacaoEquipamento frame = new SistemaLocacaoEquipamento();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace(); // Registrar exceções adequadamente em produção
            }
        });


    }
}