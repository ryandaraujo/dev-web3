package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.atualizadores.EnderecoAtualizador;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.repositorios.EnderecoRepositorio;
import com.autobots.automanager.selecionadores.EnderecoSelecionador;

@RestController
@RequestMapping("enderecos")
public class EnderecoControle {
    @Autowired
    private EnderecoRepositorio repositorio;
    @Autowired
    private EnderecoSelecionador selecionador;

    @GetMapping("{id}")
    public ResponseEntity<Endereco> obterEndereco(@PathVariable Long id) {
        List<Endereco> enderecos = repositorio.findAll();
		Endereco endereco = selecionador.selecionar(enderecos, id);
		if (endereco == null) {
			ResponseEntity<Endereco> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta; 
		} else {
			ResponseEntity<Endereco> resposta = new ResponseEntity<>(HttpStatus.FOUND);
			return resposta;
		}
    }

    @GetMapping
    public ResponseEntity<List<Endereco>> obterEnderecos() {
        List<Endereco> enderecos = repositorio.findAll();
		if (enderecos.isEmpty()) {
			ResponseEntity<List<Endereco>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			ResponseEntity<List<Endereco>> resposta = new ResponseEntity<>(HttpStatus.FOUND);
			return resposta;
		}
    }

    @PostMapping
    public ResponseEntity<?> cadastrarEndereco(@RequestBody Endereco endereco) {
        HttpStatus status = HttpStatus.CONFLICT;
		if (endereco.getId() == null) {
			repositorio.save(endereco);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
    }

    @PutMapping
    public ResponseEntity<?> atualizarEndereco(@RequestBody Endereco atualizacao) {
        HttpStatus status = HttpStatus.CONFLICT;
		Optional<Endereco> endereco = repositorio.findById(atualizacao.getId());
		if (endereco != null) {
			Endereco enderecoExistente = endereco.get();
			EnderecoAtualizador atualizador = new EnderecoAtualizador();
			atualizador.atualizar(enderecoExistente, atualizacao);
			repositorio.save(enderecoExistente);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
    }

    @DeleteMapping
    public ResponseEntity<?> excluirEndereco(@RequestBody Endereco exclusao) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
		Optional<Endereco> endereco = repositorio.findById(exclusao.getId());
		if (endereco != null) {
			Endereco enderecoExistente = endereco.get();
			repositorio.delete(enderecoExistente);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
    }
}
