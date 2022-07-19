package com.spring.padroesdeprojeto.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.padroesdeprojeto.model.Cliente;
import com.spring.padroesdeprojeto.model.ClienteRepository;
import com.spring.padroesdeprojeto.model.Endereco;
import com.spring.padroesdeprojeto.model.EnderecoRepository;
import com.spring.padroesdeprojeto.service.ClienteService;
import com.spring.padroesdeprojeto.service.ViaCepService;

@Service
public class ClienteServiceImpl implements ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private ViaCepService viaCepService;
	
	
	public Iterable<Cliente> buscarTodos(){
		return clienteRepository.findAll();
	};

	public Cliente buscarPorId(Long id) {
		return clienteRepository.findById(id).get();
	};

	public void inserir(Cliente cliente) {
		salvarClienteComCep(cliente);
	}


	public void atualizar(Long id, Cliente cliente) {
		Optional<Cliente> clienteBD = clienteRepository.findById(id);
		if(clienteBD.isPresent()) {
			salvarClienteComCep(cliente);
		}
	};

	public void deletar(Long id) {
		clienteRepository.deleteById(id);
	};

	private void salvarClienteComCep(Cliente cliente) {
		String cep = cliente.getEndereco().getCep();
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
			Endereco novoEndereco = viaCepService.consultarCep(cep);
			enderecoRepository.save(novoEndereco);
			return novoEndereco;
		});
		cliente.setEndereco(endereco);
		
		clienteRepository.save(cliente);
	};
}
