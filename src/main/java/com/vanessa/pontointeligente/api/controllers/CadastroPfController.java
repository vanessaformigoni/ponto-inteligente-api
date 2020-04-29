package com.vanessa.pontointeligente.api.controllers;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

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

import com.vanessa.pontointeligente.api.dtos.CadastroPfDto;
import com.vanessa.pontointeligente.api.entities.Empresa;
import com.vanessa.pontointeligente.api.entities.Funcionario;
import com.vanessa.pontointeligente.api.enums.PerfilEnum;
import com.vanessa.pontointeligente.api.response.Response;
import com.vanessa.pontointeligente.api.services.EmpresaService;
import com.vanessa.pontointeligente.api.services.FuncionarioService;
import com.vanessa.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/cadastrar-pf")
@CrossOrigin(origins = "*")
public class CadastroPfController {

	private static final Logger log = LoggerFactory.getLogger(CadastroPfController.class);

	@Autowired
	EmpresaService empresaService;

	@Autowired
	FuncionarioService funcionarioService;

	public CadastroPfController() {

	}

	/**
	 * Cadastra um funcionário pessoa física no sistema.
	 * 
	 * @param cadastroPFDto
	 * @param result
	 * @return ResponseEntity<Response<CadastroPFDto>>
	 * @throws NoSuchAlgorithmException
	 */

	@PostMapping
	ResponseEntity<Response<CadastroPfDto>> cadastrar(@Valid @RequestBody CadastroPfDto cadastroPfDto,
			BindingResult result) throws NoSuchAlgorithmException {
		log.info("Cadastrando PF: {}", cadastroPfDto.toString());
		Response<CadastroPfDto> response = new Response<CadastroPfDto>();

		validarDadosExistentes(cadastroPfDto, result);
		Funcionario funcionario = converterDtoParaFuncionario(cadastroPfDto); // ele coloca this aqui
		
		if(result.hasErrors()) {
			log.error("Erro ao validar os dados de cadastro da PF{}: ",result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));	 //não entendi
			return ResponseEntity.badRequest().body(response);
		}
		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cadastroPfDto.getCnpj());
		empresa.ifPresent(emp -> funcionario.setEmpresa(emp)); //nao faz sentido pra mim
		funcionarioService.persistir(funcionario);
		
		response.setData(this.converterCadastroPfDto(funcionario));
		return ResponseEntity.ok(response); // testar asiim .body(response);	

	}

	/**
	 * Converte os dados do DTO para funcionário.
	 * 
	 * @param cadastroPFDto
	 * @return Funcionario
	 * @throws NoSuchAlgorithmException
	 */
	private Funcionario converterDtoParaFuncionario(@Valid CadastroPfDto cadastroPfDto)
			throws NoSuchAlgorithmException {
		Funcionario funcionario = new Funcionario();
		funcionario.setNome(cadastroPfDto.getNome());
		funcionario.setEmail(cadastroPfDto.getEmail());
		funcionario.setCpf(cadastroPfDto.getCpf());
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setSenha(PasswordUtils.gerarBCrypt(cadastroPfDto.getSenha()));
		cadastroPfDto.getQtdHorasAlmoco() //entender melhor isso
				.ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));
		cadastroPfDto.getQtdHorasTrabalhoDia()
				.ifPresent(qtdHorasTrabDia -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabDia)));
		cadastroPfDto.getValorHora().ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));
	
		return funcionario;
	}

	/**
	 * Verifica se a empresa esta cadastrada e se o funcionario nao existe na base
	 * de dados.
	 * 
	 * @param cadastroPfDto
	 * @param result
	 */
	private void validarDadosExistentes(CadastroPfDto cadastroPfDto, BindingResult result) {

		if (!empresaService.buscarPorCnpj(cadastroPfDto.getCnpj()).isPresent()) {
			result.addError(new ObjectError("Empresa", "Empresa não cadastrada."));
		}

		this.funcionarioService.buscarPorCpf(cadastroPfDto.getCpf())
				.ifPresent(func -> result.addError(new ObjectError("Funcionario", "Funcionario já cadastrado.")));

		this.funcionarioService.buscarPoremail(cadastroPfDto.getEmail())
				.ifPresent(func -> result.addError(new ObjectError("Funcionario", "Email já cadastrado.")));
	}
	
	/**
	 * Popula o DTO de cadastro com os dados do funcionário e empresa.
	 * 
	 * @param funcionario
	 * @return CadastroPFDto
	 */
	private CadastroPfDto converterCadastroPfDto(Funcionario funcionario) {
		CadastroPfDto cadastroPfDto = new CadastroPfDto();
		cadastroPfDto.setId(funcionario.getId());
		cadastroPfDto.setNome(funcionario.getNome());
		cadastroPfDto.setEmail(funcionario.getEmail());
		cadastroPfDto.setCpf(funcionario.getCpf());
		cadastroPfDto.setCnpj(funcionario.getEmpresa().getCnpj());
		funcionario.getQtdHorasAlmocoOpt().ifPresent(qtdHorasAlmoco -> cadastroPfDto
				.setQtdHorasAlmoco(Optional.of(Float.toString(qtdHorasAlmoco))));
		funcionario.getQtdHorasTrabalhoDiaOpt().ifPresent(
				qtdHorasTrabDia -> cadastroPfDto.setQtdHorasTrabalhoDia(Optional.of(Float.toString(qtdHorasTrabDia))));
		funcionario.getValorHoraOpt()
				.ifPresent(valorHora -> cadastroPfDto.setValorHora(Optional.of(valorHora.toString())));

		return cadastroPfDto;
	}

}
