package com.vanessa.pontointeligente.api.security.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.vanessa.pontointeligente.api.entities.Funcionario;
import com.vanessa.pontointeligente.api.security.JwtUserFactory;
import com.vanessa.pontointeligente.api.services.FuncionarioService;

//Termos também que criar um serviço para manipular essa interface do UserDetails, para
//isso​ ​ crie​ ​ a ​ ​ classe​ ​ JwtUserDetailsServiceImpl.java​

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private FuncionarioService funcionarioService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Funcionario> funcionario = funcionarioService.buscarPorEmail(username);

		if (funcionario.isPresent()) {
			return JwtUserFactory.create(funcionario.get());
		}

		throw new UsernameNotFoundException("Email não encontrado.");
	}

}