package com.GUI;

import com.Backend.CadastroCli;
import com.Backend.GerenciadorDados;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CadastroCliente {

    private JTextField txtNomeCli;
    private JFormattedTextField txtTelefone;
    private JFormattedTextField txtCpf;
    private JButton btnSalvarCadCli;
    private JButton btnCancelarCadastro;
    private JPanel panelCadastroCliente;

    private List<CadastroCli> clientes;

    // Construtor da interface
    public CadastroCliente(List<CadastroCli> clientes) {
        this.clientes = clientes;

        panelCadastroCliente = new JPanel(); // Inicializa o painel principal
        JLabel lblNome = new JLabel("Nome:");
        JLabel lblCpf = new JLabel("CPF:");
        JLabel lblTelefone = new JLabel("Telefone:");
//        txtNomeCli = new JFormattedTextField();
//        txtCpf = new JFormattedTextField();
//        txtTelefone = new JFormattedTextField();
//        btnSalvarCadCli = new JButton("Salvar");
//        btnCancelarCadastro = new JButton("Cancelar");

        try {
            // Aplicando máscara para CPF
            MaskFormatter cpfMask = new MaskFormatter("###.###.###-##");
            cpfMask.setPlaceholderCharacter('_');
            txtCpf.setFormatterFactory(new DefaultFormatterFactory(cpfMask));

            // Aplicando máscara para Telefone
            MaskFormatter telefoneMask = new MaskFormatter("(##) #####-####");
            telefoneMask.setPlaceholderCharacter('_');
            txtTelefone.setFormatterFactory(new DefaultFormatterFactory(telefoneMask));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Configurando o layout do painel
        GroupLayout layout = new GroupLayout(panelCadastroCliente);
        panelCadastroCliente.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // Adicionando os componentes no painel em posições específicas
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(lblNome)
                                .addComponent(lblCpf)
                                .addComponent(lblTelefone))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(txtNomeCli)
                                .addComponent(txtCpf)
                                .addComponent(txtTelefone)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnSalvarCadCli)
                                        .addComponent(btnCancelarCadastro)))
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblNome)
                                .addComponent(txtNomeCli))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblCpf)
                                .addComponent(txtCpf))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblTelefone)
                                .addComponent(txtTelefone))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(btnSalvarCadCli)
                                .addComponent(btnCancelarCadastro))
        );

        // Ação do botão "Salvar"
        btnSalvarCadCli.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomeCli = txtNomeCli.getText();
                String cpfCli = txtCpf.getText(); // CPF formatado
                String telefoneCli = txtTelefone.getText(); // Telefone formatado

                // Verifica campos obrigatórios
                if (nomeCli == null || nomeCli.trim().isEmpty() ||
                        cpfCli == null || cpfCli.trim().isEmpty() ||
                        telefoneCli == null || telefoneCli.trim().isEmpty()) {

                    JOptionPane.showMessageDialog(panelCadastroCliente,
                            "Todos os campos são obrigatórios. Por favor, preencha-os!",
                            "Erro", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Cria o objeto cliente
                CadastroCli cliente = new CadastroCli(nomeCli, cpfCli, telefoneCli);

                // Adiciona o cliente no GerenciadorDados
                GerenciadorDados.adicionarCliente(cliente);

                JOptionPane.showMessageDialog(panelCadastroCliente,
                        "Cliente cadastrado com sucesso:\n"
                                + "Nome: " + nomeCli + "\n"
                                + "CPF: " + cpfCli + "\n"
                                + "Telefone: " + telefoneCli,
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                // Reinicia os campos
                txtNomeCli.setText("");
                txtCpf.setValue(null);
                txtTelefone.setValue(null);
            }
        });

        // Ação do botão "Cancelar"
        btnCancelarCadastro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(panelCadastroCliente,
                        "Cadastro cancelado.",
                        "Cancelado", JOptionPane.INFORMATION_MESSAGE);

                txtNomeCli.setText("");
                txtCpf.setValue(null);
                txtTelefone.setValue(null);
            }
        });
    }

    // Método para retornar o painel principal que será usado em outra classe
    public JPanel getPanel() {
        return panelCadastroCliente;
    }
}