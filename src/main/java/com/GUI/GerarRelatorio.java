package com.GUI;

import com.Backend.Equipamento;
import com.Backend.GerenciadorDados;

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
//        btnRelatorioCli.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                gerarRelatorioClientes();
//            }
//        });

        btnRelatorioEquip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gerarRelatorioEquipamentos();
            }
        });
    }

    // Método que gera relatório de equipamentos em PDF
    private void gerarRelatorioEquipamentos() {
        String destPath = "relatorio_equipamentos.pdf";

        try {
            PdfWriter writer = new PdfWriter(destPath); // Definindo o destino do arquivo
            com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Adiciona o título do relatório
            document.add(new Paragraph("Relatório de Equipamentos Mais Alugados"));

            // Obtém a lista de equipamentos e ordena pela frequência de aluguel (maior para menor)
            List<Equipamento> equipamentosOrdenados = GerenciadorDados.getListaEquipamentos()
                    .stream()
                    .sorted((e1, e2) -> Integer.compare(e2.getFrequenciaAluguel(), e1.getFrequenciaAluguel())) // Ordem decrescente
                    .toList();

            // Adiciona os equipamentos com suas frequências no relatório
            for (Equipamento equipamento : equipamentosOrdenados) {
                String equipamentoInfo = String.format(
                        "Equipamento: %s | Frequência de Aluguel: %d | Status: %s",
                        equipamento.getNome(),
                        equipamento.getFrequenciaAluguel(),
                        equipamento.getStatus().name()
                );
                document.add(new Paragraph(equipamentoInfo));
            }

            // Fecha o documento
            document.close();

            JOptionPane.showMessageDialog(null, "Relatório de Equipamentos gerado em: " + destPath);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao gerar relatório de equipamentos!");
        }
    }

    public JPanel getPanel() {
        return panelRelatorio;
    }
}
