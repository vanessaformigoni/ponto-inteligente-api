package com.vanessa.pontointeligente.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vanessa.pontointeligente.api.entities.Empresa;
import com.vanessa.pontointeligente.api.repositories.EmpresaRepository;
import com.vanessa.pontointeligente.api.services.EmpresaService;

public class EmpresaServiceImpl implements EmpresaService{

	@Autowired
	EmpresaRepository empresaRepository;
	
	private static final Logger log = LoggerFactory.getLogger(EmpresaServiceImpl.class);
	
	@Override
	public Optional<Empresa> buscaPorCnpj(String cnpj) {
		log.info("Buscando uma empresa pelo CNPJ {}",cnpj);
		return Optional.ofNullable(empresaRepository.findByCnpj(cnpj));
		
	}

	@Override
	public Empresa persistir(Empresa empresa) {
		log.info("Persistindo a empresa: {}", empresa);
		return this.empresaRepository.save(empresa);
	}
	
}
