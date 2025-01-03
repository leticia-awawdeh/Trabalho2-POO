package com.GUI;

import com.Backend.*;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GerarRelatorio {
    private JButton btnRelatorioCli;
    private JButton btnRelatorioEquip;
    private JPanel panelRelatorio;

    public GerarRelatorio() {
        btnRelatorioCli.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gerarRelatorioClientes();
            }
        });

        btnRelatorioEquip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gerarRelatorioEquipamentos();
            }
        });

    }

    // Método que gera relatório de clientes em PDF
    private void gerarRelatorioClientes() {
        //List<CadastroCli> clientesAtualizados = GerenciadorDados.calcularMultasPorCliente();
        try {
            String documentosPath = System.getProperty("user.home") + "\\Documents";
            String relatoriosPath = documentosPath + "\\Relatórios";

            // Criação dos diretórios, caso não existam
            File pastaRelatorios = new File(relatoriosPath);
            if (!pastaRelatorios.exists() && !pastaRelatorios.mkdir()) {
                JOptionPane.showMessageDialog(null, "Erro ao criar a pasta Relatórios.");
                return;
            }

            // Define o caminho do arquivo do relatório de clientes
            String mesAtual = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM"));
            String destPath = relatoriosPath + "\\relatorio_clientes_" + mesAtual + ".pdf";
            File arquivoRelatorio = new File(destPath);
            while (arquivoRelatorio.exists()) {
                String novoNome = JOptionPane.showInputDialog(
                        null,
                        "O arquivo " + arquivoRelatorio.getName() + " já existe.\n"
                                + "Por favor, insira um novo nome ou clique em 'Cancelar' para interromper:",
                        "Arquivo já existe",
                        JOptionPane.WARNING_MESSAGE
                );

                if (novoNome == null || novoNome.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Operação cancelada pelo usuário.");
                    return;
                }
                destPath = relatoriosPath + "\\" + novoNome + ".pdf";
                arquivoRelatorio = new File(destPath);
            }

            // Criação do PDF
            PdfWriter writer = new PdfWriter(destPath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Título do relatório
            document.add(new Paragraph("Relatório de Clientes - Ranking de Multas Acumuladas").setFont(com.itextpdf.kernel.font.PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD)));

            // Obtém a lista de clientes e calcula as multas acumuladas
            List<Cliente> clientesOrdenados = GerenciadorDados.getListaClientes()
                    .stream()
                    .sorted((c1, c2) -> Double.compare(c2.getMultasTotais(), c1.getMultasTotais())) // Ordena decrescente
                    .toList();

            // Adiciona cabeçalho da tabela no relatório
            document.add(new Paragraph(String.format("%-5s | %-25s | %-15s | %-15s",
                    "Pos.", "Nome", "CPF", "Multas (R$)")));

            // Adiciona os clientes ordenados ao relatório
            int posicao = 1;
            for (Cliente cliente : clientesOrdenados) {
                String clienteInfo = String.format(
                        "%-5d | %-25s | %-15s | R$ %-12s",
                        posicao,
                        cliente.getNomeCli(),
                        cliente.getCpfCli(),
                        Utils.formatarMonetario(cliente.getMultasTotais()) // Formata o valor
                );
                document.add(new Paragraph(clienteInfo));
                posicao++;
            }

            // Fecha o documento
            document.close();
            JOptionPane.showMessageDialog(null, "Relatório gerado com sucesso em: " + destPath);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao gerar o relatório!");
        }
    }
    // Método que gera relatório de equipamentos em PDF
    private void gerarRelatorioEquipamentos() {
        try {
            // Obtém o diretório "Documentos" do sistema operacional
            String documentosPath = System.getProperty("user.home") + "\\Documents";

            // Define o caminho da subpasta "Relatórios"
            String relatoriosPath = documentosPath + "\\Relatórios";

            // Cria a pasta "Relatórios" caso ela não exista
            File pastaRelatorios = new File(relatoriosPath);
            if (!pastaRelatorios.exists()) {
                boolean criada = pastaRelatorios.mkdir(); // Cria a pasta
                if (!criada) {
                    JOptionPane.showMessageDialog(null, "Erro ao criar a pasta Relatórios.");
                    return; // Encerra o método se a pasta não puder ser criada
                }
            }

            // Obtém o mês atual para gerar o nome do arquivo
            String mesAtual = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM")); // Nome do mês
            String destPath = relatoriosPath + "\\relatorio_equipamentos_" + mesAtual + ".pdf"; // Caminho inicial do arquivo

            File arquivoRelatorio = new File(destPath);

            // Verifica se o arquivo já existe
            while (arquivoRelatorio.exists()) {
                String novoNome = JOptionPane.showInputDialog(
                        null,
                        "O arquivo " + arquivoRelatorio.getName() + " já existe.\n"
                                + "Por favor, insira um novo nome ou clique em 'Cancelar' para interromper:",
                        "Arquivo já existe",
                        JOptionPane.WARNING_MESSAGE
                );

                // Se o usuário cancelar, encerra a operação
                if (novoNome == null || novoNome.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Operação cancelada pelo usuário.");
                    return; // Sai do método
                }

                // Atualiza o caminho com o novo nome fornecido pelo usuário
                destPath = relatoriosPath + "\\" + novoNome + ".pdf";
                arquivoRelatorio = new File(destPath);
            }

            // Cria o arquivo PDF no caminho especificado
            PdfWriter writer = new PdfWriter(destPath); // Configura o destino no caminho escolhido
            com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Adiciona o título do relatório
            document.add(new Paragraph("Relatório de Equipamentos Mais Alugados"));

            // Obtém a lista de equipamentos e ordena pela frequência de aluguel (maior para menor)
            List<Equipamento> equipamentosOrdenados = GerenciadorDados.getListaEquipamentos()
                    .stream()
                    .sorted((e1, e2) -> Integer.compare(e2.getFrequenciaAluguel(), e1.getFrequenciaAluguel())) // Ordem decrescente
                    .toList();

            // Variável para armazenar a soma da receita total
            double receitaTotal = 0.0;

            // Adiciona o cabeçalho do relatório
            document.add(new Paragraph(String.format("%-10s | %-25s | %-15s | %-15s | %s",
                    "Código", "Nome", "Total Aluguéis", "Receita Total", "Status")));

            // Adiciona os equipamentos com detalhes no relatório
            for (Equipamento equipamento : equipamentosOrdenados) {
                String equipamentoInfo = String.format(
                        "%-10s | %-25s | %-15d | R$ %-12.2f | %s",
                        equipamento.getCodigo(),
                        equipamento.getNome(),
                        equipamento.getFrequenciaAluguel(),
                        equipamento.calcularReceitaTotal(),
                        equipamento.getStatus().name()
                );
                document.add(new Paragraph(equipamentoInfo));

                // Acumula a receita total
                receitaTotal += equipamento.calcularReceitaTotal();
            }

            // Adiciona uma linha para a soma da receita total
            document.add(new Paragraph("\n")); // Linha em branco para separação
            document.add(new Paragraph(String.format("Receita Total Acumulada: R$ %.2f", receitaTotal)));

            // Fecha o documento
            document.close();

            // Exibe uma mensagem de sucesso com o caminho do arquivo
            JOptionPane.showMessageDialog(null, "Relatório gerado com sucesso em: " + destPath);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao gerar o relatório!");
        }
    }

    public JPanel getPanel() {
        return panelRelatorio;
    }
}
