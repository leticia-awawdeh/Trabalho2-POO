package com.GUI;

import com.Backend.Cliente;
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
    public JButton btnSalvarCadCli;
    public JButton btnCancelarCadastro;
    private JPanel panelCadastroCliente;

    private final List<Cliente> clientes;

    // Construtor da interface
    public CadastroCliente(List<Cliente> clientes) {
        this.clientes = clientes;

        panelCadastroCliente = new JPanel();
        JLabel lblNome = new JLabel("Nome:");
        JLabel lblCpf = new JLabel("CPF:");
        JLabel lblTelefone = new JLabel("Telefone:");

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

        btnSalvarCadCli.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomeCli = txtNomeCli.getText();
                String cpfCli = txtCpf.getText().replaceAll("[^\\d]", ""); // Remove a formatação
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

                // Verifica se o CPF já está cadastrado
                if (GerenciadorDados.isCpfCadastrado(cpfCli)) {
                    JOptionPane.showMessageDialog(panelCadastroCliente,
                            "Erro: O CPF informado já está cadastrado!",
                            "CPF Duplicado", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (GerenciadorDados.isTelefoneCadastrado(telefoneCli)) {
                    JOptionPane.showMessageDialog(panelCadastroCliente,
                            "Erro: O número de telefone informado já está cadastrado!",
                            "Telefone Duplicado", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Cliente cliente = new Cliente(nomeCli, cpfCli, telefoneCli);

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


    public JPanel getPanel() {
        return panelCadastroCliente;
    }
}