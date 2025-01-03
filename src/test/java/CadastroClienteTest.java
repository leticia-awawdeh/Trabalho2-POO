import com.Backend.Cliente;
import com.Backend.GerenciadorDados;
import com.GUI.CadastroCliente;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CadastroClienteTest {

    private CadastroCliente cadastroCliente;

    @BeforeEach
    void setUp() {
        GerenciadorDados.getListaClientes().clear();
        cadastroCliente = new CadastroCliente(GerenciadorDados.getListaClientes());
    }

    @AfterEach
    void tearDown() {
        GerenciadorDados.getListaClientes().clear();
    }

    @Test
    void testCadastroClienteComSucesso() {

        JTextField txtNomeCli = new JTextField("João Silva");
        JFormattedTextField txtCpf = new JFormattedTextField("123.456.789-00");
        JFormattedTextField txtTelefone = new JFormattedTextField("(11) 98765-4321");

        JButton btnSalvarCadCli = new JButton();
        btnSalvarCadCli.addActionListener(cadastroCliente.btnSalvarCadCli.getActionListeners()[0]);

        GerenciadorDados.adicionarCliente(new Cliente(
                txtNomeCli.getText(),
                txtCpf.getText().replaceAll("[^\\d]", ""), // Remove formatação
                txtTelefone.getText()
        ));

        List<Cliente> listaClientes = GerenciadorDados.getListaClientes();
        assertEquals(1, listaClientes.size(), "O cliente deve ser adicionado com sucesso.");

        Cliente cliente = listaClientes.get(0);
        assertEquals("João Silva", cliente.getNomeCli());
        assertEquals("12345678900", cliente.getCpfCli());
        assertEquals("(11) 98765-4321", cliente.getTelefoneCli());
    }

    @Test
    void testCadastroClienteFalhaCamposObrigatorios() {

        JTextField txtNomeCli = new JTextField("");
        JFormattedTextField txtCpf = new JFormattedTextField("");
        JFormattedTextField txtTelefone = new JFormattedTextField("");

        JButton btnSalvarCadCli = new JButton();
        btnSalvarCadCli.addActionListener(cadastroCliente.btnSalvarCadCli.getActionListeners()[0]);

        assertEquals(0, GerenciadorDados.getListaClientes().size(),
                "A lista de clientes deve estar vazia, pois os campos estão incompletos.");
    }

    @Test
    void testNaoPermitirCadastroDeCpfDuplicado() {

        Cliente clienteExistente = new Cliente("Maria Oliveira", "12345678900", "(11) 98765-0000");
        GerenciadorDados.adicionarCliente(clienteExistente);

        JTextField txtNomeCli = new JTextField("João Silva");
        JFormattedTextField txtCpf = new JFormattedTextField("123.456.789-00");
        JFormattedTextField txtTelefone = new JFormattedTextField("(11) 98765-4321");

        boolean isCpfCadastrado = GerenciadorDados.isCpfCadastrado(txtCpf.getText().replaceAll("[^\\d]", ""));
        assertTrue(isCpfCadastrado, "O CPF informado já deve estar cadastrado, impedindo o cadastro duplicado.");
    }

    @Test
    void testNaoPermitirCadastroDeTelefoneDuplicado() {

        Cliente clienteExistente = new Cliente("Maria Oliveira", "12345678900", "(11) 98765-4321");
        GerenciadorDados.adicionarCliente(clienteExistente);

        JTextField txtNomeCli = new JTextField("João Silva");
        JFormattedTextField txtCpf = new JFormattedTextField("987.654.321-00");
        JFormattedTextField txtTelefone = new JFormattedTextField("(11) 98765-4321"); // Telefone duplicado

        boolean isTelefoneCadastrado = GerenciadorDados.isTelefoneCadastrado(txtTelefone.getText());
        assertTrue(isTelefoneCadastrado, "O telefone informado já deve estar cadastrado, impedindo o cadastro duplicado.");
    }

    @Test
    void testCancelarCadastro() {

        JTextField txtNomeCli = new JTextField("João Silva");
        JFormattedTextField txtCpf = new JFormattedTextField("123.456.789-00");
        JFormattedTextField txtTelefone = new JFormattedTextField("(11) 98765-4321");

        JButton btnCancelarCadastro = new JButton();
        btnCancelarCadastro.addActionListener(cadastroCliente.btnCancelarCadastro.getActionListeners()[0]);

        txtNomeCli.setText("");
        txtCpf.setValue(null);
        txtTelefone.setValue(null);

        assertEquals("", txtNomeCli.getText(), "O campo Nome deve estar vazio após cancelar.");
        assertNull(txtCpf.getValue(), "O campo CPF deve estar vazio após cancelar.");
        assertNull(txtTelefone.getValue(), "O campo Telefone deve estar vazio após cancelar.");
    }
}