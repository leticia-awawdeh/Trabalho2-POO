import com.GUI.RegLocacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.Backend.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

class RegLocacaoTest {

    private RegLocacao regLocacao;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @BeforeEach
    void setUp() {
        regLocacao = new RegLocacao();
    }

    @Test
    void testCalcularDiasEntreDatas() {
        LocalDate inicio = LocalDate.parse("01/12/2023", dateFormatter);
        LocalDate termino = LocalDate.parse("10/12/2023", dateFormatter);

        long dias = RegLocacao.calcularDiasEntreDatas(inicio, termino);
        assertEquals(9, dias, "O cálculo dos dias entre as datas está incorreto!");
    }

    @Test
    void testCalcularDiasEntreDatasErro() {
        LocalDate inicio = LocalDate.parse("10/12/2023", dateFormatter);
        LocalDate termino = LocalDate.parse("01/12/2023", dateFormatter);

        long dias = RegLocacao.calcularDiasEntreDatas(inicio, termino);
        assertEquals(-9, dias, "O cálculo deveria retornar um número negativo para intervalo inválido!");
    }

    @Test
    void testCalcularValorLocacao() {
        double valorDiario = 100.0;
        long dias = 5;

        double valorEsperado = 500.0;
        double valorLocacao = regLocacao.calcularValorLocacao(valorDiario, dias);

        assertEquals(valorEsperado, valorLocacao, "O cálculo do valor da locação está incorreto!");
    }

    @Test
    void testCalcularValorLocacaoComZeroDias() {
        double valorDiario = 100.0;
        long dias = 0;

        double valorEsperado = 0.0;
        double valorLocacao = regLocacao.calcularValorLocacao(valorDiario, dias);

        assertEquals(valorEsperado, valorLocacao, "O cálculo do valor da locação quando sem dias está incorreto!");
    }

    @Test
    void testValidarDataFormatoIncorreto() {
        String dataInvalida = "2023/12/01";
        assertThrows(DateTimeParseException.class, () -> {
            LocalDate.parse(dataInvalida, dateFormatter);
        }, "Era esperado que uma exceção de formatação fosse lançada com uma data inválida!");
    }

    @Test
    void testValidarCondiçãoEquipamentoDisponível() {
        Equipamento equipamento = new Equipamento("Equipamento Teste", "Descrição Teste", 50.0);
        equipamento.setStatus(Status.DISPONIVEL); // Marca como disponível

        assertSame(equipamento.getStatus(), Status.DISPONIVEL, "O equipamento deveria estar disponível!");
    }

    @Test
    void testValidarCondiçãoEquipamentoIndisponível() {
        Equipamento equipamento = new Equipamento("Equipamento Teste", "Descrição Teste", 50.0);
        equipamento.setStatus(Status.ALUGADO); // Marca como alugado

        assertNotSame(equipamento.getStatus(), Status.DISPONIVEL, "O equipamento não deveria estar disponível!");
    }

    @Test
    void testValidarClienteEncontradoPorCpf() {
        String cpfExistente = "123.456.789-00";
        Cliente clienteMock = new Cliente("Nome Teste", cpfExistente, "123456789");
        clienteMock.setCpfCli(cpfExistente);

        GerenciadorDados.adicionarCliente(clienteMock); // Simula cliente no sistema

        Cliente clienteEncontrado = regLocacao.buscarClientePorCpf(cpfExistente);
        assertNotNull(clienteEncontrado, "O cliente não foi encontrado, mas deveria existir na base de dados!");
        assertEquals(clienteMock.getCpfCli(), clienteEncontrado.getCpfCli(), "Os CPFs não correspondem!");
    }

    @Test
    void testValidarClienteNaoEncontradoPorCpf() {
        String cpfNaoExistente = "111.222.333-44";

        Cliente clienteEncontrado = regLocacao.buscarClientePorCpf(cpfNaoExistente);
        assertNull(clienteEncontrado, "Nenhum cliente deveria ser encontrado para esse CPF!");
    }
}