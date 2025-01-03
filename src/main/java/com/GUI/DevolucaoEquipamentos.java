package com.GUI;

import com.Backend.Equipamento;
import com.Backend.CadastroCli;
import com.Backend.GerenciadorDados;
import com.Backend.Status;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class DevolucaoEquipamentos {
    private JFormattedTextField txtCpf;
    private JButton btnConfDevolucao;
    private JButton btnBuscarLoc;
    private JPanel panelDevolucao;
    private JLabel lblResult;
    private JPanel panelResult;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public DevolucaoEquipamentos() {
        try {
            // Aplicando máscara para CPF
            MaskFormatter cpfMask = new MaskFormatter("###.###.###-##");
            cpfMask.setPlaceholderCharacter('_');
            txtCpf.setFormatterFactory(new DefaultFormatterFactory(cpfMask));
        } catch (Exception e) {
            e.printStackTrace();
        }


        btnConfDevolucao.addActionListener(e -> confirmarDevolucao());

        btnBuscarLoc.addActionListener(e -> buscarLocacao(txtCpf.getText()));
    }

    // Método para buscar locações por CPF
    private void buscarLocacao(String cpf) {
        String cpfDigitado = txtCpf.getText().replaceAll("[^\\d]", "");

        // Busca cliente pelo CPF
        Optional<CadastroCli> cliente = GerenciadorDados.getListaClientes()
                .stream()
                .filter(cli -> cli.getCpf().replaceAll("[^\\d]", "").equals(cpfDigitado))
                .findFirst();

        if (cliente.isEmpty()) {
            JOptionPane.showMessageDialog(panelDevolucao, "Cliente não encontrado.", "Erro", JOptionPane.WARNING_MESSAGE);
            lblResult.setText("");
            return;
        }

        // Buscar equipamento alugado do cliente pelo CPF
        Optional<Equipamento> equipamentoAlugado = GerenciadorDados.buscarEquipamentoAlugadoPorCpf(cpfDigitado);

        if (equipamentoAlugado.isEmpty()) {
            JOptionPane.showMessageDialog(panelDevolucao,
                    "Nenhum equipamento alugado encontrado para esse cliente.",
                    "Informação", JOptionPane.INFORMATION_MESSAGE);
            lblResult.setText("");
            return;
        }

        // Preencher informações da locação no painel
        Equipamento equipamento = equipamentoAlugado.get();

        if (equipamento.getDataPrevistaDevolucao() == null) {
            lblResult.setText("<html>Equipamento: " + equipamento.getNome() +
                    "<br>Data de Término: Não configurada" +
                    "<br>Valor do aluguel: R$ " + String.format("%.2f", equipamento.getValorDiario()) + "</html>");
        } else {
            lblResult.setText(
                    "<html>Equipamento: " + equipamento.getNome() +
                            "<br>Data de Término: " + equipamento.getDataPrevistaDevolucao().format(dateFormatter) +
                            "<br>Valor do aluguel: R$ " + String.format("%.2f", equipamento.getValorDiario()) + "</html>"
            );
        }

        // Salvar temporariamente o equipamento no campo "Client Properties"
        panelResult.putClientProperty("equipamentoSelecionado", equipamento);
    }

    // Método para confirmar devolução
    private void confirmarDevolucao() {
        Object equipamentoObj = panelResult.getClientProperty("equipamentoSelecionado");

        if (equipamentoObj == null || !(equipamentoObj instanceof Equipamento)) {
            JOptionPane.showMessageDialog(panelDevolucao,
                    "Nenhum equipamento selecionado ou encontrado. Tente buscar novamente.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Equipamento equipamento = (Equipamento) equipamentoObj;

        // Obtém a data de devolução diretamente do sistema
        LocalDate dataDevolucao = LocalDate.now();

        // Calcular multa e total a pagar
        LocalDate dataTerminoLocacao = equipamento.getDataPrevistaDevolucao();
        long diasExcedentes = java.time.temporal.ChronoUnit.DAYS.between(dataTerminoLocacao, dataDevolucao);
        double multa = diasExcedentes > 0 ? diasExcedentes * (equipamento.getValorDiario() * 0.2) : 0.0;
        double total = equipamento.getValorDiario() + multa;

        // Atualizar status para "Disponível"
        equipamento.setStatus(Status.DISPONIVEL);
        equipamento.setCliente(null); // Remove o vínculo com o cliente
        GerenciadorDados.notificarAtualizacao();

        // Exibir resumo no lblResult
        lblResult.setText(
                String.format(
                        "<html>Resumo da Devolução:<br><br>" +
                                "Equipamento: %s<br>" +
                                "Data de Término: %s<br>" +
                                "Data de Devolução: %s<br>" +
                                "Multa por atraso: R$ %.2f<br>" +
                                "Valor do Aluguel: R$ %.2f<br>" +
                                "Total a Pagar: R$ %.2f</html>",
                        equipamento.getNome(),
                        dataTerminoLocacao.format(dateFormatter),
                        dataDevolucao.format(dateFormatter),
                        multa,
                        equipamento.getValorDiario(),
                        total
                )
        );

        // Limpar a seleção no painel
        panelResult.putClientProperty("equipamentoSelecionado", null);
    }



    public JPanel getPanel() {
        return panelDevolucao;
    }
}