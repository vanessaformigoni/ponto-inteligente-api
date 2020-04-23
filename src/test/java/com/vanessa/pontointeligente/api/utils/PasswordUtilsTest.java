package com.vanessa.pontointeligente.api.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class PasswordUtilsTest {

	private static final String SENHA = "12345";
	private final BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder(); 
	
	@Test
	public void testSenhaNulla() throws Exception {
		 assertNull(PasswordUtils.gerarBCrypt(null));
		
	}
	
	@Test
	public void testSenhaValida() {
		String hash = PasswordUtils.gerarBCrypt(SENHA); 
		assertTrue(bCryptEncoder.matches(SENHA, hash));
		//assertEquals(bCryptEncoder.encode(SENHA), hash); //não funfa =(
	}
	
	

}
