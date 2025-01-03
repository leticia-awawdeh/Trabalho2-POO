package com.Backend;

import junit.framework.TestCase;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClienteTest extends TestCase {

    public static void adicionarEquipamento(Equipamento equipamento) {
        if (equipamento.getStatus() != Status.DISPONIVEL) {
            throw new IllegalArgumentException("Equipamento não está disponível para locação.");
        }
        GerenciadorDados.adicionarEquipamento(equipamento);
    }

    public void testAdicionarEquipamentoDisponivel() {
        Cliente cliente = new Cliente("João", "12345678901", "999999999");
        Equipamento equipamento = new Equipamento("Câmera", "Câmera profissional", 50.0);

        equipamento.setStatus(Status.DISPONIVEL);
        GerenciadorDados.adicionarEquipamento(equipamento);
        assertEquals(Status.DISPONIVEL, equipamento.getStatus());
    }

    public void testAdicionarEquipamentoNaoDisponivel() {
        Cliente cliente = new Cliente("João", "12345678901", "999999999");
        Equipamento equipamento = new Equipamento("Câmera", "Câmera profissional", 50.0);

        equipamento.setStatus(Status.ALUGADO);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ClienteTest.adicionarEquipamento(equipamento);
        });
        assertEquals("Equipamento não está disponível para locação.", exception.getMessage());
    }

    public void testModificarNomeCliente() {
        Cliente cliente = new Cliente("Maria", "12345678901", "999999999");
        cliente.setNomeCli("João");

        assertEquals("João", cliente.getNomeCli());
    }

    public static class EquipamentoTest extends TestCase {

    }
}
