//package com.vanessa.pontointeligente.api.security.services.impl;
//
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.vanessa.pontointeligente.api.security.entities.Usuario;
//import com.vanessa.pontointeligente.api.security.repositories.UsuarioRepository;
//
//@Service
//public class UsuarioServiceImpl { //essa classe nao sera usada
//	
//	@Autowired
//	private UsuarioRepository usuarioRepository; 
//	
//	public Optional<Usuario> buscarPorEmail(String email) {
//		return Optional.ofNullable(this.usuarioRepository.findByEmail(email));
//	}
//
//	// Esse serviço será utilizado para carregar os dados do usuário, e será utilizado no processo
//	//de​ ​ autenticação​ ​ e ​ ​ autorização.
//}
