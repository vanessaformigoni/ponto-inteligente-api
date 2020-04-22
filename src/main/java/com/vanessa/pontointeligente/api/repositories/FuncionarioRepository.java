package com.vanessa.pontointeligente.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.vanessa.pontointeligente.api.entities.Funcionario;

@Transactional(readOnly=true)
interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
	
	Funcionario findByCpf(String cpf);
	
	Funcionario findByEmail(String email);
	
	Funcionario findByCpfOrEmail(String cpf, String email);

}