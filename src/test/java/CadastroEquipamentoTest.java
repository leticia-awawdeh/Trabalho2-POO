import com.Backend.Equipamento;
import com.Backend.Status;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CadastroEquipamentoTest {

    @Test
    public void testCodigoGeradoUnico() {
        Equipamento equipamento1 = new Equipamento("Equipamento 1", "Descrição 1", 100.0);
        Equipamento equipamento2 = new Equipamento("Equipamento 2", "Descrição 2", 200.0);

        assertNotEquals(equipamento1.getCodigo(), equipamento2.getCodigo(),
                "Os códigos dos equipamentos devem ser únicos.");
    }

    @Test
    public void testValoresIniciais() {
        Equipamento equipamento = new Equipamento("Câmera", "Câmera fotográfica HD", 150.0);

        assertEquals("Câmera", equipamento.getNome(), "O nome do equipamento deve ser inicializado corretamente.");
        assertEquals("Câmera fotográfica HD", equipamento.getDescricao(),
                "A descrição do equipamento deve ser inicializada corretamente.");
        assertEquals(150.0, equipamento.getValorDiario(), 0.001,
                "O valor diário deve ser inicializado corretamente.");
        assertEquals(Status.DISPONIVEL, equipamento.getStatus(),
                "O status inicial do equipamento deve ser DISPONÍVEL.");
        assertEquals(0, equipamento.getFrequenciaAluguel(),
                "A frequência de aluguel inicial deve ser 0.");
    }

    @Test
    public void testCalcularReceitaTotal() {
        Equipamento equipamento = new Equipamento("Projetor", "Projetor 4K", 50.0);
        equipamento.incrementarFrequenciaAluguel();
        equipamento.incrementarFrequenciaAluguel();

        double receitaEsperada = 100.0; // 2 alugueis x 50.0
        assertEquals(receitaEsperada, equipamento.calcularReceitaTotal(), 0.001,
                "A receita total deve ser correta baseada na frequência de aluguel.");
    }

    @Test
    public void testIncrementarFrequenciaAluguel() {
        Equipamento equipamento = new Equipamento("Monitor", "Monitor 144Hz", 120.0);

        equipamento.incrementarFrequenciaAluguel();
        equipamento.incrementarFrequenciaAluguel();

        assertEquals(2, equipamento.getFrequenciaAluguel(), "A frequência de aluguel deve ser incrementada corretamente.");
    }

    @Test
    public void testSetValorDiario() {
        Equipamento equipamento = new Equipamento("Notebook", "Notebook Gamer", 300.0);

        equipamento.setValorDiario(400.0);
        assertEquals(400.0, equipamento.getValorDiario(), 0.001,
                "O valor diário deve ser alterado corretamente quando setado.");
    }

    @Test
    public void testStatusEquipamento() {
        Equipamento equipamento = new Equipamento("Fone", "Fone de ouvido bluetooth", 80.0);

        equipamento.setStatus(Status.ALUGADO);
        assertEquals(Status.ALUGADO, equipamento.getStatus(),
                "O status do equipamento deve ser atualizado corretamente.");

        equipamento.setStatus(Status.DISPONIVEL);
        assertEquals(Status.DISPONIVEL, equipamento.getStatus(),
                "O status do equipamento deve ser atualizado de volta para DISPONÍVEL.");
    }

    @Test
    public void testSetNomeEquipamento() {
        Equipamento equipamento = new Equipamento("Smartphone", "Smartphone Android", 250.0);

        equipamento.setNome("Smartphone Pro");
        assertEquals("Smartphone Pro", equipamento.getNome(),
                "O nome do equipamento deve ser atualizado corretamente.");
    }

    @Test
    public void testToStringEquipamento() {
        Equipamento equipamento = new Equipamento("Tablet", "Tablet multifuncional", 180.0);

        assertEquals("Tablet", equipamento.toString(),
                "O método toString() deve retornar o nome do equipamento.");
    }

    @Test
    public void testValorDiarioInvalido() {
        Exception exception = assertThrows(NumberFormatException.class, () -> {
            new Equipamento("Câmera", "Câmera HD", Double.parseDouble("ABC"));
        });

        String mensagemEsperada = "For input string";
        assertTrue(exception.getMessage().contains(mensagemEsperada),
                "A exceção lançada deve conter uma mensagem indicando erro na conversão numérica.");
    }

    @Test
    public void testCodigoUnicoNaoDuplicado() {
        Equipamento equipamento1 = new Equipamento("Drone", "Drone 4K", 300);
        Equipamento equipamento2 = new Equipamento("Drone Pro", "Drone Pro 4K", 400);

        assertFalse(equipamento1.getCodigo() == equipamento2.getCodigo(),
                "Códigos gerados para diferentes equipamentos não devem ser duplicados.");
    }
}