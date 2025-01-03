package com.GUI;

import com.Backend.*;
import com.Backend.Utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.util.Optional;

public class DevolucaoEquipamentos {
    private JFormattedTextField txtCpf;
    private JButton btnConfDevolucao;
    private JButton btnBuscarLoc;
    private JPanel panelDevolucao;
    private JLabel lblResult;
    private JPanel panelResult;
    private JButton btnConfPagCli;

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

        btnConfPagCli.addActionListener(e -> confirmarPagamento());
    }

    // Método para buscar locações por CPF
    private void buscarLocacao(String cpf) {
        String cpfDigitado = txtCpf.getText().replaceAll("[^\\d]", ""); // Remove pontuações

        // Busca cliente pelo CPF
        Optional<Cliente> clienteOpt = GerenciadorDados.getListaClientes()
                .stream()
                .filter(cli -> cli.getCpfCli().replaceAll("[^\\d]", "").equals(cpfDigitado))
                .findFirst();

        if (clienteOpt.isEmpty()) {
            JOptionPane.showMessageDialog(panelDevolucao,
                    "Cliente não encontrado. Confira o CPF e tente novamente.",
                    "Erro", JOptionPane.WARNING_MESSAGE);
            lblResult.setText("");
            return;
        }

        Cliente cliente = clienteOpt.get();

        // Buscar equipamento alugado do cliente
        Optional<Locacao> locacaoOpt = GerenciadorDados.getListaLocacoes()
                .stream()
                .filter(locacao -> locacao.getCliente().getCpfCli().equals(cliente.getCpfCli()))
                .findFirst();

        if (locacaoOpt.isEmpty()) {
            JOptionPane.showMessageDialog(panelDevolucao,
                    "Nenhuma locação ativa encontrada para esse cliente.",
                    "Erro", JOptionPane.WARNING_MESSAGE);
            lblResult.setText("");
            return;
        }

        Locacao locacao = locacaoOpt.get();
        Equipamento equipamento = locacao.getEquipamento();

        lblResult.setText(String.format(
                "<html>Equipamento: %s<br>" +
                        "Cliente: %s<br>" +
                        "Data de Empréstimo: %s<br>" +
                        "Data Prevista de Devolução: %s<br>" +
                        "Valor da Locação: R$ %s</html>",
                equipamento.getNome(),
                cliente.getNomeCli(),
                locacao.getDataInicio().format(dateFormatter),
                locacao.getDataPrevistaDevolucao().format(dateFormatter),
                Utils.formatarMonetario(equipamento.getValorDiario())
        ));

        // Armazenar o equipamento no painel
        panelResult.putClientProperty("locacaoSelecionada", locacao);
    }

    private void confirmarDevolucao() {
        Object locacaoObj = panelResult.getClientProperty("locacaoSelecionada");

        if (locacaoObj == null || !(locacaoObj instanceof Locacao locacao)) {
            JOptionPane.showMessageDialog(panelDevolucao,
                    "Nenhuma locação selecionada ou encontrada. Tente buscar novamente.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Equipamento equipamento = locacao.getEquipamento();

        equipamento.setStatus(Status.DISPONIVEL);
        equipamento.setCliente(null);

        LocalDate dataDevolucao = LocalDate.now();
        locacao.setDataDevolucao(dataDevolucao);
        long diasAtraso = locacao.getDiasAtraso();
        double multa = locacao.calcularMulta();
        double valorAluguel = locacao.getValorDiario() * locacao.getQuantidadeDiasLocacao();

        double total = valorAluguel + multa;

        if (diasAtraso > 0) {
            Cliente cliente = locacao.getCliente();
            if (cliente != null) {
                String descricaoMulta = String.format(
                        "Atraso de %d dia(s) na devolução do equipamento %s.",
                        diasAtraso,
                        equipamento.getNome()
                );
                cliente.adicionarMulta(descricaoMulta, multa);
            }
        }

        lblResult.setText(String.format(
                "<html>Resumo da Devolução:<br><br>" +
                        "Equipamento: %s<br>" +
                        "Data Prevista de Término: %s<br>" +
                        "Data de Devolução: %s<br>" +
                        "Multa por atraso: R$ %s<br>" +
                        "Valor do Aluguel: R$ %s<br>" +
                        "Total a Pagar: R$ %s</html>",
                equipamento.getNome(),
                locacao.getDataPrevistaDevolucao().format(dateFormatter),
                dataDevolucao.format(dateFormatter),
                Utils.formatarMonetario(multa),
                Utils.formatarMonetario(valorAluguel),
                Utils.formatarMonetario(total)
        ));

        panelResult.putClientProperty("locacaoSelecionada", null);
    }

    private void confirmarPagamento() {
        // Exibe um pop-up com mensagem de confirmação
        JOptionPane.showMessageDialog(panelDevolucao,
                "Pagamento confirmado e devoluções realizadas!",
                "Confirmação", JOptionPane.INFORMATION_MESSAGE);

        txtCpf.setValue(null);
        txtCpf.setText("");
        lblResult.setText("");
    }

    public JPanel getPanel() {
        return panelDevolucao;
    }
}