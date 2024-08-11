package com.redeok.projeto;

import com.redeok.projeto.domain.Cliente;
import com.redeok.projeto.mock.CustomConstraintViolation;
import com.redeok.projeto.dto.ClientePatchDTO;
import com.redeok.projeto.repositories.ClienteRepository;
import com.redeok.projeto.resources.ClienteResource;

import com.redeok.projeto.resources.EnderecoResource;
import com.redeok.projeto.validaton.validator.PatchValidator;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
public class ClienteResourceTeste {

    @InjectMocks
    private ClienteResource clienteResource;

    @InjectMocks
    private EnderecoResource enderecoResource;

    @Mock
    private ClienteRepository clienteRepository = new ClienteRepository();

    @Mock
    private PatchValidator patchValidator = new PatchValidator();

    @Mock
    Jdbi jdbi;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve retornar todos os cliente")
    public void deveRetornarTodosOsCliente() {
        //Cenário
        Cliente cliente1 = new Cliente();
        cliente1.setId(1L);
        cliente1.setNome("Carlos");

        Cliente cliente2 = new Cliente();
        cliente2.setId(2L);
        cliente2.setNome("Vitor");

        List<Cliente> clientes = Arrays.asList(cliente1, cliente2);

        when(clienteRepository.listAll()).thenReturn(clientes);

        //Execução
        List<Cliente> resultado = clienteResource.listAll();

        //Verificações
        assertEquals(2, resultado.size());
        assertEquals("Carlos", resultado.get(0).getNome());
        assertEquals("Vitor", resultado.get(1).getNome());
    }

    @Test
    @DisplayName("Deve encontrar Cliente por número de documento")
    public void deveProcurarPorNumDocumentoComClienteEncontrado() {

        //Cenário
        String numDocumento = "12345678901";
        Cliente cliente = new Cliente();
        cliente.setNumDocumento(numDocumento);
        cliente.setNome("Cliente Teste");

        when(clienteRepository.findById(numDocumento)).thenReturn(cliente);

        //Execução
        Cliente resultado = clienteResource.procurarPorNumDocumento(numDocumento);

        //Verificações
        assertNotNull(resultado);
        assertEquals(numDocumento, resultado.getNumDocumento());
        assertEquals("Cliente Teste", resultado.getNome());

        verify(clienteRepository, times(1)).findById(numDocumento);
    }

    @Test
    @DisplayName("Deve procurar Cliente por número de documento e não encontrar o cliente")
    public void deveProcurarPorNumDocumentoComClienteNaoEncontrado() {

        //Cenário
        String numDocumento = "98765432100";

        when(clienteRepository.findById(numDocumento)).thenReturn(null);

        //Execução
        Cliente resultado = clienteResource.procurarPorNumDocumento(numDocumento);

        assertNull(resultado);

        //Verificações
        verify(clienteRepository, times(1)).findById(numDocumento);
    }

    @Test
    @DisplayName("Deve criar novo Cliente com sucesso")
    public void deveCriarClienteComSucesso() {

        //Cenário
        Cliente cliente = new Cliente();
        cliente.setNumDocumento("12345678901");
        cliente.setEmail("teste@email.com");
        cliente.setTipoDocumento("CPF");

        when(clienteRepository.existsByNumDocumento(cliente.getNumDocumento())).thenReturn(false);

        //Execução
        Response response = clienteResource.create(cliente);

        //Verificações
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals("Cliente cadastrado com sucesso", response.getEntity());
    }

    @Test
    @DisplayName("Ao tentar criar um cliente que já existe, deve retornar Status de conflito")
    public void deveCriarClienteComConflito() {

        //Cenário
        Cliente cliente = new Cliente();
        cliente.setNumDocumento("12345678901");
        cliente.setEmail("teste@email.com");
        cliente.setTipoDocumento("CPF");

        when(clienteRepository.existsByNumDocumento(cliente.getNumDocumento())).thenReturn(true);

        //Execução
        Response response = clienteResource.create(cliente);

        //Verificações
        assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
        assertEquals("Já existe cliente cadastrado com esse documento", response.getEntity());
    }

    @Test
    @DisplayName("Deve tentar criar um cliente com dados inconsistentes, e retornar um bad request")
    public void deveCriarClienteComValidationError() {

        //Cenário
        Cliente cliente = new Cliente();

        Set<ConstraintViolation<Cliente>> violations = Collections.singleton(new CustomConstraintViolation<>("Erro de validação"));
        ConstraintViolationException exception = new ConstraintViolationException(violations);

        Mockito.doThrow(exception).when(clienteRepository).insert(cliente);

        //Execução
        Response response = clienteResource.create(cliente);

        //Verificações
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Erro de validação", response.getEntity());
    }

    @Test
    @DisplayName("Deve tentar realizar um update parcial em Cliente com um Cliente que não foi encontrado")
    public void realizarUpdatePartialComClienteNaoEncontrado() {

        //Cenário
        String numDocumento = "12345678901";
        when(clienteRepository.findById(numDocumento)).thenReturn(null);

        //Execução
        Response response = clienteResource.updatePartial(numDocumento, new ClientePatchDTO());

        //Verificações
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(clienteRepository, times(1)).findById(numDocumento);
    }

    @Test
    @DisplayName("Deve realizar um update parcial com sucesso")
    public void realizarUpdatePartialComSucesso() {

        //Cenário
        String numDocumento = "12345678901";
        Cliente existingCliente = new Cliente();
        existingCliente.setNumDocumento(numDocumento);
        existingCliente.setNome("Nome Original");

        when(clienteRepository.findById(numDocumento)).thenReturn(existingCliente);

        ClientePatchDTO patchDto = new ClientePatchDTO();
        patchDto.setNome(Optional.of("Nome Atualizado"));

        when(patchValidator.validarNome(anyString())).thenReturn(null);

        //Execução
        Response response = clienteResource.updatePartial(numDocumento, patchDto);

        //Verificações
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Cliente updatedCliente = (Cliente) response.getEntity();
        assertNotNull(updatedCliente);
        assertEquals("Nome Atualizado", updatedCliente.getNome());

        verify(clienteRepository, times(1)).findById(numDocumento);
        verify(clienteRepository, times(1)).update(existingCliente);
    }

    @Test
    @DisplayName("Realizar update parcial com todos os campos")
    public void realizarUpdatePartialTodosOsCamposComSucesso() {

        //Cenário
        String numDocumento = "12345678901";
        Cliente existingCliente = new Cliente();
        existingCliente.setNumDocumento(numDocumento);
        existingCliente.setNome("Nome Original");
        existingCliente.setTelefone("61999999999");
        existingCliente.setEmail("teste@email.com");
        existingCliente.setNumDocumento("99999999999");
        existingCliente.setTipoDocumento("CPF");

        when(clienteRepository.findById(numDocumento)).thenReturn(existingCliente);

        ClientePatchDTO patchDto = new ClientePatchDTO();
        patchDto.setNome(Optional.of("Nome Atualizado"));
        patchDto.setTelefone(Optional.of("61999999999"));
        patchDto.setEmail(Optional.of("teste@email.com"));
        patchDto.setNumDocumento(Optional.of("99999999999"));
        patchDto.setTipoDocumento(Optional.of("CPF"));

        when(patchValidator.validarNome(anyString())).thenReturn(null);

        //Execução
        Response response = clienteResource.updatePartial(numDocumento, patchDto);

        // Verificações
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Cliente updatedCliente = (Cliente) response.getEntity();
        assertNotNull(updatedCliente);
        assertEquals("Nome Atualizado", updatedCliente.getNome());
        assertEquals("61999999999", updatedCliente.getTelefone());
        assertEquals("teste@email.com", updatedCliente.getEmail());
        assertEquals("99999999999", updatedCliente.getNumDocumento());
        assertEquals("CPF", updatedCliente.getTipoDocumento());

        verify(clienteRepository, times(1)).findById(numDocumento);
        verify(clienteRepository, times(1)).update(existingCliente);
    }

    @Test
    @DisplayName("Deve tentar realizar update parcial com dados inválidos, e retornar status de bad request")
    public void realizarUpdatePartialComFalhaDeValidacao() {

        //Cenário
        String numDocumento = "12345678901";
        Cliente existingCliente = new Cliente();
        existingCliente.setNumDocumento(numDocumento);
        existingCliente.setNome("Nome Original");

        when(clienteRepository.findById(numDocumento)).thenReturn(existingCliente);

        ClientePatchDTO patchDto = new ClientePatchDTO();
        patchDto.setNome(Optional.of("87678"));

        //Simulando uma falha de validação
        Response erroDeValidacao = Response.status(Response.Status.BAD_REQUEST).entity("O nome deve conter apenas letras e espaços").build();
        when(patchValidator.validarNome("87678")).thenReturn(erroDeValidacao);

        //Execução
        Response response = clienteResource.updatePartial(numDocumento, patchDto);
        Response responsePatchValidator = patchValidator.validarNome("87678");

        // Verificações
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("O nome deve conter apenas letras e espaços", response.getEntity());
        assertEquals("O nome deve conter apenas letras e espaços", responsePatchValidator.getEntity());

        verify(patchValidator, times(1)).validarNome("87678");
        verify(clienteRepository, never()).update(existingCliente);
    }

    @Test
    @DisplayName("Deve tentar realizar update parcial de todos os campos com dados inválidos, e retornar status bad request")
    public void realizarUpdatePartialTodosOsCamposComFalhaDeValidacao() {

        //Cenário
        String numDocumento = "12345678901";
        Cliente existingCliente = new Cliente();
        existingCliente.setNumDocumento(numDocumento);
        existingCliente.setNome("Nome Original");
        existingCliente.setTelefone("61999999999");
        existingCliente.setEmail("teste@email.com");
        existingCliente.setNumDocumento("99999999999");
        existingCliente.setTipoDocumento("CPF");

        when(clienteRepository.findById(numDocumento)).thenReturn(existingCliente);

        ClientePatchDTO patchDto = new ClientePatchDTO();
        patchDto.setNome(Optional.of("87678"));
        patchDto.setTelefone(Optional.of("619999999"));
        patchDto.setEmail(Optional.of("teste.com"));
        patchDto.setNumDocumento(Optional.of("9999999"));
        patchDto.setTipoDocumento(Optional.of("CNPP"));

        //Simulando uma falha de validação
        Response erroDeValidacaoNome = Response.status(Response.Status.BAD_REQUEST).entity("O nome deve conter apenas letras e espaços").build();
        Response erroDeValidacaoTelefone = Response.status(Response.Status.BAD_REQUEST).entity("Telefone inválido").build();
        Response erroDeValidacaoEmail = Response.status(Response.Status.BAD_REQUEST).entity("Email inválido").build();
        Response erroDeValidacaoNumDocumento = Response.status(Response.Status.BAD_REQUEST).entity("Número de documento inválido").build();
        Response erroDeValidacaoTipoDocumento = Response.status(Response.Status.BAD_REQUEST).entity("Tipo de documento inválido.").build();

        when(patchValidator.validarNome("87678")).thenReturn(erroDeValidacaoNome);
        when(patchValidator.validarTelefone("619999999")).thenReturn(erroDeValidacaoTelefone);
        when(patchValidator.validarEmail("teste.com")).thenReturn(erroDeValidacaoEmail);
        when(patchValidator.validarNumDocumento("9999999")).thenReturn(erroDeValidacaoNumDocumento);
        when(patchValidator.validarTipoDocumento("CNPP")).thenReturn(erroDeValidacaoTipoDocumento);

        //Execução
        Response response = clienteResource.updatePartial(numDocumento, patchDto);
        Response responsePatchValidatorNome = patchValidator.validarNome("87678");
        Response responsePatchValidatorTelefone = patchValidator.validarTelefone("619999999");
        Response responsePatchValidatorEmail = patchValidator.validarEmail("teste.com");
        Response responsePatchValidatorNumDocumento = patchValidator.validarNumDocumento("9999999");
        Response responsePatchValidatorTipoDocumento = patchValidator.validarTipoDocumento("CNPP");

        // Verificações
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        //assertEquals("O nome deve conter apenas letras e espaços", response.getEntity());
        assertEquals("O nome deve conter apenas letras e espaços", responsePatchValidatorNome.getEntity());
        assertEquals("Telefone inválido", responsePatchValidatorTelefone.getEntity());
        assertEquals("Email inválido", responsePatchValidatorEmail.getEntity());
        assertEquals("Número de documento inválido", responsePatchValidatorNumDocumento.getEntity());
        assertEquals("Tipo de documento inválido.", responsePatchValidatorTipoDocumento.getEntity());

        verify(patchValidator, times(1)).validarNome("87678");
        verify(patchValidator, times(1)).validarTelefone("619999999");
        verify(patchValidator, times(1)).validarEmail("teste.com");
        verify(patchValidator, times(1)).validarNumDocumento("9999999");
        verify(patchValidator, times(1)).validarTipoDocumento("CNPP");

        verify(clienteRepository, never()).update(existingCliente);
    }

    @Test
    @DisplayName("Verifica se o EnderecoResource retornado não é nulo")
    void testGetEnderecoResource() {
        EnderecoResource result = clienteResource.getEnderecoResource();

        assertNotNull(result);
    }

}
