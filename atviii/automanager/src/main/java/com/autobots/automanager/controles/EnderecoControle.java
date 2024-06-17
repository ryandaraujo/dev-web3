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

import com.autobots.automanager.atualizadores.AtualizadorEndereco;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.hateoas.AdicionadorLinkEndereco;
import com.autobots.automanager.repositorios.RepositorioEndereco;
import com.autobots.automanager.selecionadores.SelecionadorEndereco;

public class EnderecoControle {
    @Autowired
    RepositorioEndereco repositorio;
    @Autowired
    SelecionadorEndereco selecionador;
    @Autowired
    AtualizadorEndereco atualizador;
    @Autowired
    AdicionadorLinkEndereco adicionadorLink;

    @PostMapping
    public ResponseEntity<?> cadastrarEndereco(@RequestBody Endereco endereco) {
        try {
            HttpStatus status = HttpStatus.CONFLICT;
            if (endereco.getId() == null) {
                repositorio.save(endereco);
                status = HttpStatus.CREATED;
            }
		return new ResponseEntity<>(status);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> obterEnderecos() {
        try {
            List<Endereco> enderecos = repositorio.findAll();
            if (!enderecos.isEmpty()) {
                adicionadorLink.adicionarLink(enderecos);
                ResponseEntity<List<Endereco>> resposta = new ResponseEntity<>(enderecos, HttpStatus.OK);
                return resposta;
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> obterEndereco(@PathVariable Long id) {
        try {
            List<Endereco> enderecos = repositorio.findAll();
            Endereco endereco = selecionador.selecionar(enderecos, id);
            if (endereco == null) {
                ResponseEntity<Endereco> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
                return resposta; 
            } else {
                adicionadorLink.adicionarLink(endereco);
                ResponseEntity<Endereco> resposta = new ResponseEntity<Endereco>(endereco, HttpStatus.FOUND);
                return resposta;
            }
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> excluirEndereco(@RequestBody Endereco exclusao) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
		Optional<Endereco> endereco = repositorio.findById(
            exclusao.getId()
        );
		if (endereco != null) {
			Endereco enderecoExistente = endereco.get();
			repositorio.delete(enderecoExistente);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}

    @PutMapping
    public ResponseEntity<?> atualizarEndereco(@RequestBody Endereco atualizacao) {
        HttpStatus status = HttpStatus.CONFLICT;
		Optional<Endereco> endereco = repositorio.findById(
            atualizacao.getId()
        );
		if (endereco != null) {
			Endereco enderecoExistente = endereco.get();
			atualizador.atualizar(enderecoExistente, atualizacao);
			repositorio.save(enderecoExistente);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
    }
}
