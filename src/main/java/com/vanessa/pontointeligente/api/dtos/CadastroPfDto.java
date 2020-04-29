package com.vanessa.pontointeligente.api.dtos;

import java.util.Optional;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

public class CadastroPfDto {
	
	private long id;
	private String nome;
	private String email;
	private String senha;
	private String cpf;
	private Optional<String> valorHora = Optional.empty(); //pq é string ??
	private Optional<String> qtdHorasTrabalhoDia = Optional.empty();
	private Optional<String> qtdHorasAlmoco = Optional.empty();
	private String cnpj; //para associar com uma empresa
	
	public CadastroPfDto() {
		
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	@NotEmpty(message="Nome não pode ser vazio.")
	@Length(min=3, max=200, message="O tamanho deve ser entre 3 a 200 caracteres." )
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@NotEmpty(message="Email não pode ser vazio.")
	@Length(min=5, max=200, message="O tamanho deve ser entre 53 a 200 caracteres." )
	@Email(message="Email invalido")
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	@NotEmpty(message="A senha não pode ser vazia.")
	public String getSenha() {
		return senha;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	@NotEmpty(message="Cpf não deve ser vazio.")
	@CPF(message="Cpf invalido.")
	public String getCpf() {
		return cpf;
	}
	
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public Optional<String> getValorHora() {
		return valorHora;
	}
	
	public void setValorHora(Optional<String> valorHora) {
		this.valorHora = valorHora;
	}
	
	public Optional<String> getQtdHorasTrabalhoDia() {
		return qtdHorasTrabalhoDia;
	}
	
	public void setQtdHorasTrabalhoDia(Optional<String> qtdHorasTrabalhoDia) {
		this.qtdHorasTrabalhoDia = qtdHorasTrabalhoDia;
	}
	
	public Optional<String> getQtdHorasAlmoco() {
		return qtdHorasAlmoco;
	}
	
	public void setQtdHorasAlmoco(Optional<String> qtdHorasAlmoço) {
		this.qtdHorasAlmoco = qtdHorasAlmoço;
	}
	
	@NotEmpty(message="Cnpj não deve ser vazio.")
	@CNPJ
	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	
	@Override
	public String toString() {
		return "FuncionarioDto [id=" + id + ", nome=" + nome + ", email=" + email + ", senha=" + senha + ", cpf=" + cpf
				+ ", valorHora=" + valorHora + ", qtdHorasTrabalhoDia=" + qtdHorasTrabalhoDia + ", qtdHorasAlmoco="
				+ qtdHorasAlmoco + ", cnpj=" + cnpj + "]";
	}
	
}
