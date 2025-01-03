package com.GUI;

import com.Backend.*;
import junit.framework.TestCase;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DevolucaoEquipamentosTest extends TestCase {

    public void testAdicionarEquipamento() {
        Equipamento equipamento = new Equipamento("Câmera", "Câmera profissional", 50.0);

        GerenciadorDados.adicionarEquipamento(equipamento);

        assertTrue(GerenciadorDados.getListaEquipamentos().contains(equipamento));
    }

    public void testRemoverEquipamento() {
        Equipamento equipamento = new Equipamento("Tripé", "Tripé para câmera", 20.0);
        GerenciadorDados.adicionarEquipamento(equipamento);

        GerenciadorDados.removerEquipamento(equipamento);

        assertFalse(GerenciadorDados.getListaEquipamentos().contains(equipamento));
    }

    public void testAdicionarClienteDuplicadoPorCPF() {
        Cliente cliente1 = new Cliente("João", "123.456.789-00", "9999-0000");
        Cliente cliente2 = new Cliente("Maria", "123.456.789-00", "8888-0000");
        GerenciadorDados.adicionarCliente(cliente1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            GerenciadorDados.adicionarCliente(cliente2);
        });

        assertEquals("Cliente com CPF já cadastrado.", exception.getMessage());
    }

    public void testRegistrarLocacaoComClienteEEquipamentoValidos() {
        Cliente cliente = new Cliente("Lucas", "234.567.890-12", "9876-5432");
        Equipamento equipamento = new Equipamento("Projetor", "Projetor HD", 100.0);

        GerenciadorDados.adicionarCliente(cliente);
        GerenciadorDados.adicionarEquipamento(equipamento);

        GerenciadorDados.registrarLocacao(cliente, equipamento);

        assertEquals(cliente, equipamento.getCliente());
        assertEquals(Status.ALUGADO, equipamento.getStatus());
    }

    public void testRegistrarLocacaoSemEquipamento() {
        Cliente cliente = new Cliente("Laura", "123.123.123-12", "1234-4321");

        GerenciadorDados.adicionarCliente(cliente);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            GerenciadorDados.registrarLocacao(cliente, null);
        });

        assertEquals("O equipamento não pode ser nulo.", exception.getMessage());
    }

    public void testBuscarEquipamentoPorCPF() {
        Cliente cliente = new Cliente("Carlos", "345.678.912-34", "1111-2222");
        Equipamento equipamento = new Equipamento("Notebook", "Notebook alto desempenho", 200.0);

        GerenciadorDados.adicionarCliente(cliente);
        GerenciadorDados.adicionarEquipamento(equipamento);
        GerenciadorDados.registrarLocacao(cliente, equipamento);

        var resultado = GerenciadorDados.buscarEquipamentoAlugadoPorCpf("345.678.912-34");

        assertTrue(resultado.isPresent());
        assertEquals(equipamento, resultado.get());
    }

    public void testCalcularMultaPorAtraso() {
        Cliente cliente = new Cliente("Ana", "456.789.123-56", "6666-5555");
        Equipamento equipamento = new Equipamento("Placa de Vídeo", "RTX 3080", 150.0);

        GerenciadorDados.adicionarCliente(cliente);
        GerenciadorDados.adicionarEquipamento(equipamento);

        LocalDate dataInicio = LocalDate.now().minusDays(10);
        LocalDate dataPrevistaDevolucao = LocalDate.now().minusDays(5);

        Locacao locacao = new Locacao(cliente, equipamento, dataInicio, dataPrevistaDevolucao, 10.0);

        locacao.setDataDevolucao(LocalDate.now());

        long diasAtraso = locacao.getDiasAtraso();

        assertEquals(5, diasAtraso);
    }
}