package com.GUI;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class SistemaLocacaoEquipamentoTest {

    @Test
    void testInitializacaoJanela() {
        // Inicializa o sistema
        SistemaLocacaoEquipamento sistema = new SistemaLocacaoEquipamento();

        // Verifica se o título está correto
        assertEquals("Sistema de Locação de Equipamentos", sistema.getTitle(), "O título da janela está incorreto!");

        // Verifica se o conteúdo principal foi definido corretamente
        assertNotNull(sistema.getContentPane(), "O painel principal (contentPane) não foi inicializado.");

        // Verifica se o tamanho da janela está correto
        assertEquals(600, sistema.getWidth(), "A largura da janela está incorreta.");
        assertEquals(400, sistema.getHeight(), "A altura da janela está incorreta.");
    }

    @Test
    void testNumeroDeAbas() {
        // Inicializa o sistema
        SistemaLocacaoEquipamento sistema = new SistemaLocacaoEquipamento();

        // Obtém o painel de abas (JTabbedPane)
        JTabbedPane painelAbas = (JTabbedPane) sistema.getContentPane();

        // Verifica se o painel de abas contém todas as abas esperadas
        assertEquals(5, painelAbas.getTabCount(), "O número de abas da interface está incorreto.");

        // Verifica se os nomes das abas estão corretos
        assertEquals("Cadastro de Cliente", painelAbas.getTitleAt(0), "A primeira aba tem o título incorreto.");
        assertEquals("Cadastro de Equipamentos", painelAbas.getTitleAt(1), "A segunda aba tem o título incorreto.");
        assertEquals("Registro de Locação", painelAbas.getTitleAt(2), "A terceira aba tem o título incorreto.");
        assertEquals("Devolução de Equipamentos", painelAbas.getTitleAt(3), "A quarta aba tem o título incorreto.");
        assertEquals("Gerar Relatório", painelAbas.getTitleAt(4), "A quinta aba tem o título incorreto.");
    }

    @Test
    void testComponentesSwing() {
        SistemaLocacaoEquipamento sistema = new SistemaLocacaoEquipamento();
        JTabbedPane painelAbas = (JTabbedPane) sistema.getContentPane();
        JPanel abaGerarRelatorio = (JPanel) painelAbas.getComponentAt(4);

        assertNotNull(abaGerarRelatorio, "O painel 'Gerar Relatório' não foi inicializado corretamente.");

        Component[] componentes = abaGerarRelatorio.getComponents();
        assertTrue(componentes.length > 0, "Nenhum componente encontrado no painel 'Gerar Relatório'.");
    }

    @Test
    void testFechamentoJanela() {
        // Inicializa o sistema
        SistemaLocacaoEquipamento sistema = new SistemaLocacaoEquipamento();

        // Verifica se o comportamento de fechamento foi configurado
        assertEquals(JFrame.EXIT_ON_CLOSE, sistema.getDefaultCloseOperation(), "O comportamento de fechamento da janela está incorreto.");
    }

    /**
     * Método auxiliar para encontrar um botão pelo nome dentro de um painel.
     */
    private JButton encontrarBotao(JPanel painel, String nomeBotao) {
        for (java.awt.Component componente : painel.getComponents()) {
            if (componente instanceof JButton botao) {
                if (nomeBotao.equals(botao.getName())) {
                    return botao;
                }
            }
        }
        return null; // Botão não encontrado
    }
}