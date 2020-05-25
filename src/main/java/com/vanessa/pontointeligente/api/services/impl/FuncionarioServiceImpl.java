package com.vanessa.pontointeligente.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vanessa.pontointeligente.api.entities.Funcionario;
import com.vanessa.pontointeligente.api.repositories.FuncionarioRepository;
import com.vanessa.pontointeligente.api.services.FuncionarioService;

@Service
public class FuncionarioServiceImpl implements FuncionarioService {

	@Autowired
	FuncionarioRepository funcionarioRepository;
	
	private static final Logger log = LoggerFactory.getLogger(FuncionarioServiceImpl.class);
	
	@Override
	public Funcionario persistir(Funcionario funcionario) {
		log.info("Persistindo funcionario: {}", funcionario);
		return this.funcionarioRepository.save(funcionario);		
	}

	@Override
	public Optional<Funcionario> buscarPorCpf(String cpf) {
		log.info("Buscando funcionario pelo CPF: {}",cpf);
		return Optional.ofNullable(this.funcionarioRepository.findByCpf(cpf));
	}

	@Override
	public Optional<Funcionario> buscarPoremail(String email) {
		log.info("BUscando funcionario pelo email: {}",email);
		return Optional.ofNullable(this.funcionarioRepository.findByEmail(email));
	}
	
	@Override
	public Optional<Funcionario> buscarPorId(Long id) {
		log.info("Buscando funcionario pelo id: {}",id);
	return this.funcionarioRepository.findById(id); //funcionou e não sei pq =(
	}
	

}
