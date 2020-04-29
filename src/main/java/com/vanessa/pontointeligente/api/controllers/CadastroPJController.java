package com.vanessa.pontointeligente.api.controllers;

import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vanessa.pontointeligente.api.dtos.CadastroPJDto;
import com.vanessa.pontointeligente.api.entities.Empresa;
import com.vanessa.pontointeligente.api.entities.Funcionario;
import com.vanessa.pontointeligente.api.enums.PerfilEnum;
import com.vanessa.pontointeligente.api.response.Response;
import com.vanessa.pontointeligente.api.services.EmpresaService;
import com.vanessa.pontointeligente.api.services.FuncionarioService;
import com.vanessa.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/cadastrar-pj")
@CrossOrigin(origins = "*")
public class CadastroPJController { // esse controller fara cadastro de empresa e funcionario

	private static final Logger log = LoggerFactory.getLogger(CadastroPJController.class);

	@Autowired
	FuncionarioService funcionarioService;

	@Autowired
	EmpresaService empresaService;

	public CadastroPJController() { // porque esse construtor padrao ?

	}

	/**
	 * Cadastra uma pessoa juridica no sistema
	 * 
	 * @param cadastroPJDto
	 * @param result
	 * @return ResponseEntity<Response<CadastroPJDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping
	ResponseEntity<Response<CadastroPJDto>> cadastrar(@Valid @RequestBody CadastroPJDto cadastroPJDto,
			BindingResult result) throws NoSuchAlgorithmException {
		log.info("Cadastrando PJ: {}", cadastroPJDto.toString());
		Response<CadastroPJDto> response = new Response<CadastroPJDto>(); // crio pois a resposta estara aqui

		validarDadosExistentes(cadastroPJDto, result);
		Empresa empresa = this.converterDtoParaEmpresa(cadastroPJDto); // testar sem o this
		Funcionario funcionario = this.converterDtoParaFuncionario(cadastroPJDto); // entender porque esse passa o result

		if(result.hasErrors()) {
			log.error("Erro validando o cadastro de PJ: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage())); //entender essa linha
			return ResponseEntity.badRequest().body(response);
			
		}
		
		this.empresaService.persistir(empresa);
		funcionario.setEmpresa(empresa);
		this.funcionarioService.persistir(funcionario);
		
		response.setData(this.converterCadastroPjDto(funcionario)); //não entendi pra que isso.
		return ResponseEntity.ok(response);
		
	}

	private CadastroPJDto converterCadastroPjDto(Funcionario funcionario) {
		
		CadastroPJDto cadastroPJDto = new CadastroPJDto();
		cadastroPJDto.setId(funcionario.getId());
		cadastroPJDto.setNome(funcionario.getNome());
		cadastroPJDto.setEmail(funcionario.getEmail());
		cadastroPJDto.setCpf(funcionario.getCpf());
		cadastroPJDto.setRazaoSocial(funcionario.getEmpresa().getRazaoSocial());
		cadastroPJDto.setCnpj(funcionario.getEmpresa().getCnpj());

		return cadastroPJDto;
		
	}

	/**
	 * Converter os dados do DTO para Funcionario.
	 * 
	 * @param cadastroPJDto
	 * @return Funcionario
	 * @throws NoSuchAlgorithmException
	 */
	private Funcionario converterDtoParaFuncionario(CadastroPJDto cadastroPJDto)
			throws NoSuchAlgorithmException {
		Funcionario funcionario = new Funcionario();
		funcionario.setNome(cadastroPJDto.getNome());
		funcionario.setEmail(cadastroPJDto.getEmail());
		funcionario.setCpf(cadastroPJDto.getCpf());
		funcionario.setPerfil(PerfilEnum.ROLE_ADMIN);
		funcionario.setSenha(PasswordUtils.gerarBCrypt(cadastroPJDto.getSenha()));
		
		return funcionario;
	}

	/**
	 * Converte os dados do DTO para Empresa.
	 * 
	 * @param cadastroPJDto
	 * @return Empresa
	 */
	private Empresa converterDtoParaEmpresa(CadastroPJDto cadastroPJDto) {
		Empresa empresa = new Empresa();
		empresa.setCnpj(cadastroPJDto.getCnpj());
		empresa.setRazaoSocial(cadastroPJDto.getRazaoSocial());

		return empresa;
	}

	/**
	 * Verifica se a empresa ou funcionario já existe na base de dados.
	 * 
	 * @param cadastroPJDto
	 * @param result
	 */
	private void validarDadosExistentes(CadastroPJDto cadastroPJDto, BindingResult result) {

		this.empresaService.buscarPorCnpj(cadastroPJDto.getCnpj())
				.ifPresent(emp -> result.addError(new ObjectError("Empresa", "Empresa já existente.")));

		this.funcionarioService.buscarPorCpf(cadastroPJDto.getCpf())
				.ifPresent(func -> result.addError(new ObjectError("Funcionario", "CPF já existente")));

		this.funcionarioService.buscarPoremail(cadastroPJDto.getEmail())
				.ifPresent(func -> result.addError(new ObjectError("Funcionario", "Email já existente")));

	}

}
