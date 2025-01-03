import com.Backend.*;
import junit.framework.TestCase;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DevolucaoEquipamentosTest extends TestCase {

    public void testAdicionarEquipamento() {
        // Arrange
        Equipamento equipamento = new Equipamento("Câmera", "Câmera profissional", 50.0);

        // Act
        GerenciadorDados.adicionarEquipamento(equipamento);

        // Assert
        assertTrue(GerenciadorDados.getListaEquipamentos().contains(equipamento));
    }

    public void testRemoverEquipamento() {
        // Arrange
        Equipamento equipamento = new Equipamento("Tripé", "Tripé para câmera", 20.0);
        GerenciadorDados.adicionarEquipamento(equipamento);

        // Act
        GerenciadorDados.removerEquipamento(equipamento);

        // Assert
        assertFalse(GerenciadorDados.getListaEquipamentos().contains(equipamento));
    }

    public void testAdicionarClienteDuplicadoPorCPF() {
        // Arrange
        Cliente cliente1 = new Cliente("João", "123.456.789-00", "9999-0000");
        Cliente cliente2 = new Cliente("Maria", "123.456.789-00", "8888-0000");
        GerenciadorDados.adicionarCliente(cliente1);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            GerenciadorDados.adicionarCliente(cliente2);
        });

        assertEquals("Cliente com CPF já cadastrado.", exception.getMessage());
    }

    public void testRegistrarLocacaoComClienteEEquipamentoValidos() {
        // Arrange
        Cliente cliente = new Cliente("Lucas", "234.567.890-12", "9876-5432");
        Equipamento equipamento = new Equipamento("Projetor", "Projetor HD", 100.0);

        GerenciadorDados.adicionarCliente(cliente);
        GerenciadorDados.adicionarEquipamento(equipamento);

        // Act
        GerenciadorDados.registrarLocacao(cliente, equipamento);

        // Assert
        assertEquals(cliente, equipamento.getCliente());
        assertEquals(Status.ALUGADO, equipamento.getStatus());
    }

    public void testRegistrarLocacaoSemEquipamento() {
        // Arrange
        Cliente cliente = new Cliente("Laura", "123.123.123-12", "1234-4321");

        GerenciadorDados.adicionarCliente(cliente);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            GerenciadorDados.registrarLocacao(cliente, null);
        });

        assertEquals("O equipamento não pode ser nulo.", exception.getMessage());
    }

    public void testBuscarEquipamentoPorCPF() {
        // Arrange
        Cliente cliente = new Cliente("Carlos", "345.678.912-34", "1111-2222");
        Equipamento equipamento = new Equipamento("Notebook", "Notebook alto desempenho", 200.0);

        GerenciadorDados.adicionarCliente(cliente);
        GerenciadorDados.adicionarEquipamento(equipamento);
        GerenciadorDados.registrarLocacao(cliente, equipamento);

        // Act
        var resultado = GerenciadorDados.buscarEquipamentoAlugadoPorCpf("345.678.912-34");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(equipamento, resultado.get());
    }

    public void testCalcularMultaPorAtraso() {
        // Arrange
        Cliente cliente = new Cliente("Ana", "456.789.123-56", "6666-5555");
        Equipamento equipamento = new Equipamento("Placa de Vídeo", "RTX 3080", 150.0);

        GerenciadorDados.adicionarCliente(cliente);
        GerenciadorDados.adicionarEquipamento(equipamento);

        // Data de início e data prevista para a locação
        LocalDate dataInicio = LocalDate.now().minusDays(10);
        LocalDate dataPrevistaDevolucao = LocalDate.now().minusDays(5);

        // Cria a locação
        Locacao locacao = new Locacao(cliente, equipamento, dataInicio, dataPrevistaDevolucao, 10.0);

        // Defina a data de devolução (5 dias de atraso)
        locacao.setDataDevolucao(LocalDate.now());

        // Act
        long diasAtraso = locacao.getDiasAtraso();

        // Assert
        assertEquals(5, diasAtraso);
    }
}