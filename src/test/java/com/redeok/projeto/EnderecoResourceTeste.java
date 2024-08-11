package com.redeok.projeto;

import com.redeok.projeto.domain.Cliente;
import com.redeok.projeto.domain.Endereco;
import com.redeok.projeto.repositories.ClienteRepository;
import com.redeok.projeto.repositories.EnderecoRepository;
import com.redeok.projeto.resources.EnderecoResource;
import com.redeok.projeto.validaton.validator.EnderecoValidator;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@QuarkusTest
public class EnderecoResourceTeste {

    @InjectMocks
    private EnderecoResource enderecoResource;

    @Mock
    private EnderecoRepository enderecoRepository = new EnderecoRepository();

    @Mock
    private ClienteRepository clienteRepository = new ClienteRepository();

    @Mock
    private EnderecoValidator enderecoValidator = new EnderecoValidator();

    @Mock
    Jdbi jdbi;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve buscar endereços a partir do número de documento de um cliente")
    public void deveBuscarEnderecosPorCliente() {

        //Cenário
        String numDocumento = "12345678910";
        Long clienteId = 1L;

        Cliente cliente = new Cliente();
        cliente.setId(clienteId);

        Endereco endereco1 = new Endereco();
        endereco1.setId(1L);
        endereco1.setIdCliente(clienteId);

        Endereco endereco2 = new Endereco();
        endereco2.setId(2L);
        endereco2.setIdCliente(clienteId);

        List<Endereco> enderecos = Arrays.asList(endereco1, endereco2);

        when(clienteRepository.findById(numDocumento)).thenReturn(cliente);
        when(enderecoRepository.listarEnderecosPorCliente(clienteId)).thenReturn(enderecos);

        //Execução
        List<Endereco> result = enderecoResource.getEnderecos(numDocumento);

        //Verificações
        assertEquals(2, result.size(), "O número de endereços retornados deve ser 2.");
        assertEquals(enderecos, result, "Os endereços retornados devem corresponder aos mockados.");

        verify(clienteRepository, times(1)).findById(numDocumento);
        verify(enderecoRepository, times(1)).listarEnderecosPorCliente(clienteId);
    }

    @Test
    @DisplayName("Deve criar um novo endereço a partir de um cliente com sucesso")
    public void deveCriarEnderecoComSuccess() {

        //Cenário
        String numDocumento = "12345678910";
        Long clienteId = 1L;
        Cliente cliente = new Cliente();
        cliente.setId(clienteId);

        Endereco endereco = new Endereco();
        endereco.setIdCliente(clienteId);
        endereco.setCep("12345678");
        endereco.setUf("DF");
        endereco.setCidade("Brasília");
        endereco.setLogradouro("Rua Exemplo");
        endereco.setNumero("123");

        when(clienteRepository.findById(numDocumento)).thenReturn(cliente);
        when(enderecoValidator.validarCep(anyString())).thenReturn(null);
        when(enderecoValidator.validarUf(anyString())).thenReturn(null);
        when(enderecoValidator.validarCidade(anyString())).thenReturn(null);
        when(enderecoValidator.validarLogradouro(anyString())).thenReturn(null);
        when(enderecoValidator.validarNumero(anyString())).thenReturn(null);

        //Execução
        Response response = enderecoResource.createEndereco(numDocumento, endereco);

        //Verificações
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals("Endereco cadastrado com sucesso", response.getEntity());

        verify(clienteRepository, times(1)).findById(numDocumento);
        verify(enderecoRepository, times(1)).insert(endereco);
    }

    @Test
    @DisplayName("Deve tentar criar um endereço a partir de um cliente que não existe, e retornar status not found")
    public void deveCriarEnderecoComClienteNaoEncontrado() {
        //Cenário
        String numDocumento = "12345678910";
        Endereco endereco = new Endereco();

        when(clienteRepository.findById(numDocumento)).thenReturn(null);

        //Execução
        Response response = enderecoResource.createEndereco(numDocumento, endereco);

        //Verificações
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Cliente com o número de documento " + numDocumento + " não encontrado.", response.getEntity());

        verify(clienteRepository, times(1)).findById(numDocumento);
        verify(enderecoRepository, never()).insert(any(Endereco.class));
    }

    @Test
    @DisplayName("Deve criar um novo endereço a partir de um cliente com dados inválidos, e retornar status bad request")
    public void deveCriarEnderecoComFalhaDeValidacao() {

        //Cenário
        String numDocumento = "12345678910";
        Long clienteId = 1L;
        Cliente cliente = new Cliente();
        cliente.setId(clienteId);

        Endereco endereco = new Endereco();
        endereco.setCep("1234567");
        endereco.setUf("b5");
        endereco.setCidade("Bras1lia");
        endereco.setLogradouro("");
        endereco.setNumero("teste");

        when(clienteRepository.findById(numDocumento)).thenReturn(cliente);

        //Execução
        Response response = enderecoResource.createEndereco(numDocumento, endereco);

        Response erroDeValidacaoCep = Response.status(Response.Status.BAD_REQUEST).entity("CEP inválido").build();
        Response erroDeValidacaoUf = Response.status(Response.Status.BAD_REQUEST).entity("UF inválida").build();
        Response erroDeValidacaoCidade = Response.status(Response.Status.BAD_REQUEST).entity("Cidade inválida").build();
        Response erroDeValidacaoLougradouro = Response.status(Response.Status.BAD_REQUEST).entity("Campo logradouro é obrigatório").build();
        Response erroDeValidacaoNumero = Response.status(Response.Status.BAD_REQUEST).entity("Campo número inválido").build();

        when(enderecoValidator.validarCep("1234567")).thenReturn(erroDeValidacaoCep);
        when(enderecoValidator.validarUf("b5")).thenReturn(erroDeValidacaoUf);
        when(enderecoValidator.validarCidade("Bras1lia")).thenReturn(erroDeValidacaoCidade);
        when(enderecoValidator.validarLogradouro("")).thenReturn(erroDeValidacaoLougradouro);
        when(enderecoValidator.validarNumero("teste")).thenReturn(erroDeValidacaoNumero);

        Response responseValidacaoCep = enderecoValidator.validarCep(endereco.getCep());
        Response responseValidacaoUf = enderecoValidator.validarUf("b5");
        Response responseValidacaoCidade = enderecoValidator.validarCidade("Bras1lia");
        Response responseValidacaoLogradouro = enderecoValidator.validarLogradouro("");
        Response responseValidacaoNumero = enderecoValidator.validarNumero("teste");

        //Verificações
        assertEquals("CEP inválido", responseValidacaoCep.getEntity());
        assertEquals("UF inválida", responseValidacaoUf.getEntity());
        assertEquals("Cidade inválida", responseValidacaoCidade.getEntity());
        assertEquals("Campo logradouro é obrigatório", responseValidacaoLogradouro.getEntity());
        assertEquals("Campo número inválido", responseValidacaoNumero.getEntity());

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

        verify(enderecoValidator, times(1)).validarCep(anyString());
        verify(enderecoValidator, times(1)).validarUf(anyString());
        verify(enderecoValidator, times(1)).validarCidade(anyString());
        verify(enderecoValidator, times(1)).validarLogradouro(anyString());
        verify(enderecoValidator, times(1)).validarNumero(anyString());
        verify(enderecoRepository, never()).insert(any(Endereco.class));
    }

    @Test
    @DisplayName("Deve tentar deletar o endereço de um cliente que não foi encontrado, e retornar status not found")
    void deveDeletarEnderecoComClienteNaoEncontrado() {

        //Cenário
        String numDocumento = "123456789";
        Long idEndereco = 1L;
        when(clienteRepository.findById(numDocumento)).thenReturn(null);

        //Execução
        Response response = enderecoResource.deleteEndereco(numDocumento, idEndereco);

        //Verificações
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Cliente com o número de documento " + numDocumento + " não encontrado.", response.getEntity());
        verify(clienteRepository, times(1)).findById(numDocumento);
        verifyNoMoreInteractions(clienteRepository, enderecoRepository);
    }

    @Test
    @DisplayName("Deve tentar deletar um endereço não encontrado a partir de um id recebido, e retornar status not found")
    void deveDeletarEnderecoNaoEncontrado() {
        Long idCliente = 100L;
        String numDocumento = "123456789";
        Long idEndereco = 1L;

        Cliente cliente = new Cliente();
        cliente.setId(idCliente);
        when(clienteRepository.findById(numDocumento)).thenReturn(cliente);
        when(enderecoRepository.findById(idEndereco)).thenReturn(null);

        Response response = enderecoResource.deleteEndereco(numDocumento, idEndereco);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Endereço não encontrado ou não pertence ao cliente especificado.", response.getEntity());
        verify(clienteRepository, times(1)).findById(numDocumento);
        verify(enderecoRepository, times(1)).findById(idEndereco);
        verifyNoMoreInteractions(clienteRepository, enderecoRepository);
    }

    @Test
    @DisplayName("Deve tentar deletar um endereço que não pertence ao cliente fornecido, e retornar status not found")
    void deveDeletarEnderecoNaoPertenceAoCliente() {

        //Cenário
        String numDocumento = "123456789";
        Long idEndereco = 1L;
        Long idCliente = 100L;

        Cliente cliente = new Cliente();
        cliente.setId(idCliente);
        Endereco endereco = new Endereco();
        endereco.setIdCliente(999L); // ID diferente do cliente

        when(clienteRepository.findById(numDocumento)).thenReturn(cliente);
        when(enderecoRepository.findById(idEndereco)).thenReturn(endereco);

        //Execução
        Response response = enderecoResource.deleteEndereco(numDocumento, idEndereco);

        //Verificações
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Endereço não encontrado ou não pertence ao cliente especificado.", response.getEntity());
        verify(clienteRepository, times(1)).findById(numDocumento);
        verify(enderecoRepository, times(1)).findById(idEndereco);
        verifyNoMoreInteractions(clienteRepository, enderecoRepository);
    }

    @Test
    @DisplayName("Deve receber o id de um endereco a partir de um cliente e deletar o endereço")
    void deveDeletarEnderecoComSucesso() {

        //Cenário
        String numDocumento = "123456789";
        Long idEndereco = 1L;
        Long idCliente = 100L;

        Cliente cliente = new Cliente();
        cliente.setId(idCliente);
        Endereco endereco = new Endereco();
        endereco.setIdCliente(idCliente);

        when(clienteRepository.findById(numDocumento)).thenReturn(cliente);
        when(enderecoRepository.findById(idEndereco)).thenReturn(endereco);

        //Execução
        Response response = enderecoResource.deleteEndereco(numDocumento, idEndereco);

        //Verificações
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(clienteRepository, times(1)).findById(numDocumento);
        verify(enderecoRepository, times(1)).findById(idEndereco);
        verify(enderecoRepository, times(1)).delete(idEndereco);
        verifyNoMoreInteractions(clienteRepository, enderecoRepository);
    }
}
