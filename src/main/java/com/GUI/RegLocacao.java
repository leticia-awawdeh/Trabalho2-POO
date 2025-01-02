package com.GUI;

import com.Backend.Equipamento;
import com.Backend.GerenciadorDados;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class RegLocacao {
    private JComboBox<String> dropDwnEquip;
    private JButton btnRegistrarLoc;
    private JButton btnCancelarLoc;
    private JTextField txtDataTermino;
    private JTextField txtDataInicio;
    private JTextField txtTelefoneCli;
    private JTextField txtCpfCli;
    private JTextField txtNomeCli;
    private JPanel regLoc;

    public RegLocacao() {
        carregarEquipamentos();

        GerenciadorDados.addAtualizacaoListener(() -> carregarEquipamentos());

        dropDwnEquip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String equipamentoSelecionado = (String) dropDwnEquip.getSelectedItem();
                if (!"Selecione o equipamento".equals(equipamentoSelecionado)) {
                    System.out.println("Equipamento selecionado: " + equipamentoSelecionado);
                }
            }
        });


    }

    private void carregarEquipamentos() {
        // Limpa todos os itens do JComboBox
        dropDwnEquip.removeAllItems();

        // Adiciona o item "placeholder"
        dropDwnEquip.addItem("Selecione o equipamento");

        // Obtém a lista de equipamentos do sistema
        List<Equipamento> equipamentos = GerenciadorDados.getListaEquipamentos();

        // Adiciona cada equipamento ao JComboBox
        for (Equipamento equipamento : equipamentos) {
            dropDwnEquip.addItem(equipamento.getNome());
        }

        // Define como selecionado o primeiro item (placeholder)
        dropDwnEquip.setSelectedIndex(0);
    }




    public JPanel getPanel() {
        return regLoc;
    }
}
