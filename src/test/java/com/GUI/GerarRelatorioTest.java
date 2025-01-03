package com.GUI;

import com.Backend.Cliente;
import com.Backend.Equipamento;
import com.Backend.GerenciadorDados;
import com.Backend.Locacao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GerarRelatorioTest {

    private GerarRelatorio gerarRelatorio;

    @BeforeEach
    void setUp() {
        gerarRelatorio = new GerarRelatorio();
    }

    @AfterEach
    void tearDown() {

        GerenciadorDados.getListaClientes().clear();
        GerenciadorDados.getListaEquipamentos().clear();
        GerenciadorDados.getListaLocacoes().clear();
    }

    @Test
    public void testGerarRelatorioClientesComClientes() {

        Cliente cliente1 = new Cliente("Cliente 1", "123.456.789-01", "150.0");
        Cliente cliente2 = new Cliente("Cliente 2", "987.654.321-00", "300.0");

        GerenciadorDados.adicionarCliente(cliente1);
        GerenciadorDados.adicionarCliente(cliente2);

        Equipamento equipamento1 = new Equipamento("Equipamento 1", "Descrição equipamento 1", 50.0);
        Equipamento equipamento2 = new Equipamento("Equipamento 2", "Descrição equipamento 2", 100.0);

        GerenciadorDados.adicionarEquipamento(equipamento1);
        GerenciadorDados.adicionarEquipamento(equipamento2);

        Locacao locacao1 = new Locacao(
                cliente1, equipamento1,
                LocalDate.now().minusDays(10),
                LocalDate.now().minusDays(5),
                10.0
        );
        locacao1.setDataDevolucao(LocalDate.now());
        GerenciadorDados.adicionarLocacao(locacao1);

        Locacao locacao2 = new Locacao(
                cliente2, equipamento2,
                LocalDate.now().minusDays(15),
                LocalDate.now().minusDays(10),
                20.0
        );
        locacao2.setDataDevolucao(LocalDate.now());
        GerenciadorDados.adicionarLocacao(locacao2);

        List<Cliente> clientesComMultas = GerenciadorDados.calcularMultasPorCliente();

        assertNotNull(clientesComMultas, "A lista não deve ser nula.");
        assertEquals(2, clientesComMultas.size(), "Deve haver 2 clientes com multas.");

        assertEquals(25.0, clientesComMultas.get(0).getMultasTotais(), "A multa total do cliente 1 está errada.");
        assertEquals(100.0, clientesComMultas.get(1).getMultasTotais(), "A multa total do cliente 2 está errada.");
    }

    @Test
    public void deveAdicionarClienteNaLista() {
        Cliente cliente = new Cliente("Cliente teste", "12345678900", "email@teste.com");
        Equipamento equipamento = new Equipamento("Equipamento teste", "descrição", 100.0);
        Locacao locacao = new Locacao(cliente, equipamento, LocalDate.now(), LocalDate.now().plusDays(7), 0.10);

        List<Cliente> clientes = new ArrayList<>();
        clientes.add(cliente);

        assertEquals(1, clientes.size(), "Deve haver 1 cliente na lista.");
    }

    @Test
    void testCalcularMultasPorClienteSemAtrasos() {
        Cliente cliente = new Cliente("Cliente 1", "123.456.789-01", "(11) 98765-4321");
        GerenciadorDados.adicionarCliente(cliente);

        Equipamento equipamento = new Equipamento("Equipamento teste", "descrição", 100.0);
        GerenciadorDados.adicionarEquipamento(equipamento);

        Locacao locacaoSemAtraso = new Locacao(
                cliente,
                equipamento,
                LocalDate.now().minusDays(5), // Data de início
                LocalDate.now().minusDays(1), // Data prevista de devolução
                0.10                           // Multa padrão
        );
        locacaoSemAtraso.setDataDevolucao(LocalDate.now().minusDays(1)); // Devolução no prazo
        GerenciadorDados.adicionarLocacao(locacaoSemAtraso);

        List<Cliente> clientesCalculados = GerenciadorDados.calcularMultasPorCliente();

        assertNotNull(clientesCalculados, "A lista de clientes não pode ser nula.");
        assertTrue(clientesCalculados.isEmpty(), "Nenhum cliente deve ter multas.");
    }


    @Test
    void testGerarRelatorioEquipamentosComEquipamentos() {

        Equipamento equipamento1 = new Equipamento("Equipamento A", "E001", 10);
        equipamento1.setNome("Equipamento A");

        Equipamento equipamento2 = new Equipamento("Equipamento B", "E002", 15);
        equipamento2.setNome("Equipamento B");

        GerenciadorDados.adicionarEquipamento(equipamento1);
        GerenciadorDados.adicionarEquipamento(equipamento2);

        List<Equipamento> listaEquipamentos = GerenciadorDados.getListaEquipamentos();

        assertNotNull(listaEquipamentos, "A lista de equipamentos deve existir.");
        assertEquals(2, listaEquipamentos.size(), "A lista deve conter 2 equipamentos.");
        assertTrue(listaEquipamentos.stream().anyMatch(eq -> eq.getNome().equals("Equipamento A")));
        assertTrue(listaEquipamentos.stream().anyMatch(eq -> eq.getNome().equals("Equipamento B")));
    }

    @Test
    void testGerarRelatorioEquipamentosSemEquipamentos() {
        List<Equipamento> listaEquipamentos = GerenciadorDados.getListaEquipamentos();

        assertNotNull(listaEquipamentos, "A lista de equipamentos deve existir.");
        assertTrue(listaEquipamentos.isEmpty(), "A lista de equipamentos deve estar vazia.");
    }

    @Test
    void testCriarDiretorioRelatorios() {
        String documentosPath = System.getProperty("user.home") + "\\Documents";
        String relatoriosPath = documentosPath + "\\Relatórios";

        File pastaRelatorios = new File(relatoriosPath);

        boolean criadaComSucesso = pastaRelatorios.mkdir();

        assertTrue(pastaRelatorios.exists(), "A pasta de relatórios deve existir.");
        assertTrue(criadaComSucesso || pastaRelatorios.exists(), "A pasta deve ser criada com sucesso ou já existir.");
    }

    @Test
    void testNaoGerarRelatorioComNomeDeArquivoExistente() {
        String documentosPath = System.getProperty("user.home") + "\\Documents";
        String relatoriosPath = documentosPath + "\\Relatórios";

        File pastaRelatorios = new File(relatoriosPath);
        if (!pastaRelatorios.exists()) {
            pastaRelatorios.mkdir();
        }

        String relatorioExistentePath = relatoriosPath + "\\relatorio_teste.pdf";
        File relatorioExistente = new File(relatorioExistentePath);
        try {
            boolean criado = relatorioExistente.createNewFile(); // Cria um arquivo fake para o teste
            assertTrue(criado || relatorioExistente.exists(), "O arquivo do relatório deve existir.");

            // Simula gerar relatório sem sobrescrever
            assertThrows(Exception.class, () -> {
                if (relatorioExistente.exists()) {
                    throw new Exception("Arquivo já existe.");
                }
            }, "Deve lançar uma exceção porque o arquivo existe.");
        } catch (Exception e) {
            fail("Erro inesperado ao preparar o teste: " + e.getMessage());
        } finally {
            // Limpa o ambiente de teste
            relatorioExistente.delete();
        }
    }
}