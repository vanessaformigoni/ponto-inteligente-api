package com.vanessa.pontointeligente.api.repositories;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.security.NoSuchAlgorithmException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.vanessa.pontointeligente.api.entities.Empresa;
import com.vanessa.pontointeligente.api.entities.Funcionario;
import com.vanessa.pontointeligente.api.enums.PerfilEnum;
import com.vanessa.pontointeligente.api.utils.PasswordUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioRepositoryTest {

	@Autowired
	EmpresaRepository empresaRepository;
	
	@Autowired
	FuncionarioRepository funcionarioRepository;
	
	private final static String CPF = ("1234578912");
	private final static String EMAIL = ("email@email.com");
	
	@Before
	public void setUp() throws Exception {
		Empresa empresa = empresaRepository.save(obterDadosEmpresa());
		funcionarioRepository.save(obterDadosFuncionario(empresa));
	}
	
	@After
	public void tearDown() {
		empresaRepository.deleteAll();
		
	}
	
	@Test
	public void testBuscaFuncionarioPorEmail() {
		Funcionario funcionario = this.funcionarioRepository.findByEmail(EMAIL);
		
		assertEquals(funcionario.getEmail(),EMAIL);
	}
	
	@Test
	public void testBuscaFuncionarioPorCpf() {
		Funcionario funcionario = this.funcionarioRepository.findByCpf(CPF);
		
		assertEquals(funcionario.getCpf(), CPF);	
	}
	
	@Test
	public void testBuscaFuncionarioPorEmailECpf() {
		Funcionario funcionario = this.funcionarioRepository.findByCpfOrEmail(CPF,EMAIL);
		
		//assertEquals(CPF, funcionario.getCpf());
		//assertEquals(EMAIL, funcionario.getEmail());
		assertNotNull(funcionario);
	}
	
	@Test
	public void testBuscarFuncionarioPorEmailOuCpfParaEmailInvalido() {
		Funcionario funcionario = this.funcionarioRepository.findByCpfOrEmail(CPF, "emailinvalido@email.com");
		
		assertNotNull(funcionario);
	}
	
	@Test
	public void testBuscarFuncionarioPorEmailECpfParaCpfInvalido() {
		Funcionario funcionario = this.funcionarioRepository.findByCpfOrEmail("12345678901", EMAIL);

		assertNotNull(funcionario);
	}
	
	@Test //Nao sei se faz sentido esse teste
	public void testBuscarFuncionarioPorEmailECpfInvalido() {
		Funcionario funcionario = this.funcionarioRepository.findByCpfOrEmail("12345678901", "emailinvalido@email.com");

		assertNull(funcionario);
		
	}
	
	private Funcionario obterDadosFuncionario(Empresa empresa) throws NoSuchAlgorithmException {
		
		Funcionario funcionario = new Funcionario();
		funcionario.setNome("Fulano de Tal");
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setSenha(PasswordUtils.gerarBCrypt("123456"));
		funcionario.setCpf(CPF);
		funcionario.setEmail(EMAIL);
		funcionario.setEmpresa(empresa);
		return funcionario;
	}
	
	private Empresa obterDadosEmpresa() {
		
		Empresa empresa = new Empresa();
		empresa.setCnpj("51463645000100");
		empresa.setRazaoSocial("Empresa de exemplo");
		return empresa;
		
	}

}
