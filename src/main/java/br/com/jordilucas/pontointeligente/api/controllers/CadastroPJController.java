package br.com.jordilucas.pontointeligente.api.controllers;

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

import br.com.jordilucas.pontointeligente.api.dtos.CadastroPJDto;
import br.com.jordilucas.pontointeligente.api.entities.Empresa;
import br.com.jordilucas.pontointeligente.api.entities.Funcionario;
import br.com.jordilucas.pontointeligente.api.enums.PerfilEnum;
import br.com.jordilucas.pontointeligente.api.response.Response;
import br.com.jordilucas.pontointeligente.api.services.EmpresaService;
import br.com.jordilucas.pontointeligente.api.services.FuncionarioService;
import br.com.jordilucas.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/cadastropj")
@CrossOrigin("*")
public class CadastroPJController {
	
	private static final Logger log = LoggerFactory.getLogger(CadastroPJController.class);
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private EmpresaService empresaService;
	
	public CadastroPJController() {}
	
	/**
	 * Cadastra uma pessoa juridica no sistema
	 * 
	 * @param cadastroPJDto
	 * @param result
	 * @return ResponseEntity<Response<CadastroPJDto>>
	 * @throws NoSuchAlgorithmException
	 * 
	 * **/
	@PostMapping
	public ResponseEntity<Response<CadastroPJDto>> cadastrar(@Valid @RequestBody CadastroPJDto cadastroPJDto, BindingResult result) throws NoSuchAlgorithmException {
		
		log.info("Cadastrando PJ: {}", cadastroPJDto.toString());
		Response<CadastroPJDto> response = new Response<CadastroPJDto>();
		
		validarDadosExistentes(cadastroPJDto, result);
		Empresa empresa = this.converterDtoParaEmpresa(cadastroPJDto, result);
		Funcionario funcionario = this.converterDtoParaFuncionario(cadastroPJDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados de cadastro PJ: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		this.empresaService.persistir(empresa);
		funcionario.setEmpresa(empresa);
		this.funcionarioService.persistir(funcionario);
		response.setData(this.converterCadastroPJDto(funcionario));
		return ResponseEntity.ok(response);
		
	}
	
	/**
	 * Popula o DTO de cadastro com os dados de funcionario e empresa
	 * 
	 * @param funcionario
	 * @return CadastroPJDto
	 * 
	 * **/
	private CadastroPJDto converterCadastroPJDto(Funcionario funcionario) {
		
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
	 * Converte os dados DTO para empresa
	 * 
	 * @param cadastroPJDto
	 * @param Empresa
	 * @return empresa
	 * 
	 * **/
	private Empresa converterDtoParaEmpresa(CadastroPJDto cadastroPJDto, BindingResult result) {
		
		Empresa empresa = new Empresa();
		empresa.setCnpj(cadastroPJDto.getCnpj());
		empresa.setRazaoSocial(cadastroPJDto.getRazaoSocial());
		
		return empresa;
	}
	
	/**
	 * Converte os dados DTO para funcionario
	 * 
	 * @param cadastroPJDto
	 * @param funcionario
	 * @return funcionario
	 * @throws NoSuchAlgorithmException
	 * 
	 * **/
	private Funcionario converterDtoParaFuncionario(CadastroPJDto cadastroPJDto, BindingResult result) throws NoSuchAlgorithmException {
		
		Funcionario funcionario = new Funcionario();
		funcionario.setNome(cadastroPJDto.getNome());
		funcionario.setEmail(cadastroPJDto.getEmail());
		funcionario.setCpf(cadastroPJDto.getCpf());
		funcionario.setPerfil(PerfilEnum.ROLE_ADMIN);
		funcionario.setSenha(PasswordUtils.gerarBCrypt(cadastroPJDto.getSenha()));
		
		return funcionario;
	}
	
	/**
	 * Verifica se empresa ou funcionario ja existe no banco de dados
	 * @param cadastroPJDto
	 * @param result
	 * 
	 * **/
	private void validarDadosExistentes(CadastroPJDto cadastroPJDto, BindingResult result) {
		
		this.empresaService.buscarPorCnpj(cadastroPJDto.getCnpj())
				.ifPresent(emp -> result.addError(new ObjectError("empresa", "Empresa ja existe")));
		this.funcionarioService.buscaPorCpf(cadastroPJDto.getCpf())
				.ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF ja existe")));
		this.funcionarioService.buscaPorEmail(cadastroPJDto.getEmail())
				.ifPresent(func -> result.addError(new ObjectError("funcionario", "Email ja existe")));
	}
	
}
