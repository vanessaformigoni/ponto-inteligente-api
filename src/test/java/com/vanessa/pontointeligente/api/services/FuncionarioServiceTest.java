package com.vanessa.pontointeligente.api.services;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.*;

import java.util.Optional;

import org.assertj.core.api.BDDAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.vanessa.pontointeligente.api.entities.Funcionario;
import com.vanessa.pontointeligente.api.repositories.FuncionarioRepository;

@RunWith(SpringRunner.class) //esse teste esta quebrado
@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioServiceTest {
	
	@Autowired
	FuncionarioService funcionarioService;
	
	@MockBean
	FuncionarioRepository funcionarioRepository;

	@Before
	public void setUp() throws Exception {
		BDDMockito.given(this.funcionarioRepository.save(Mockito.any(Funcionario.class))).willReturn(new Funcionario());
		BDDMockito.given(this.funcionarioRepository.findByCpf(Mockito.anyString())).willReturn(new Funcionario());
		BDDMockito.given(this.funcionarioRepository.findByEmail(Mockito.anyString())).willReturn(new Funcionario());
		BDDMockito.given(this.funcionarioRepository.findById(Mockito.anyLong())).willReturn(Optional.of(new Funcionario())); 

	}

	@Test
	public void testPersistirFuncionario() {
		Funcionario funcionario = new Funcionario();
		funcionario = funcionarioService.persistir(funcionario);
		//Funcionario funcionario = this.funcionarioService.persistir(new Funcionario()); mais bonito
		assertNotNull(funcionario);
	}
	
	@Test
	public void testBuscarFuncionarioPorCpf() {
		Optional<Funcionario> funcionario = this.funcionarioService.buscarPorCpf("12345");
		assertTrue(funcionario.isPresent());
		
	}

	@Test
	public void testBuscaFuncionarioPorEmail() {
		Optional<Funcionario> funcionario = this.funcionarioService.buscarPoremail("email@email.com");
		assertTrue(funcionario.isPresent());
	}
	
	@Test
	public void testBuscaFuncionarioPorId() {
		Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(1L);
		assertTrue(funcionario.isPresent());
	}
	
	
}
