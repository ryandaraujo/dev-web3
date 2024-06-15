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

import com.autobots.automanager.atualizadores.AtualizadorEmail;
import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.hateoas.AdicionadorLinkEmail;
import com.autobots.automanager.repositorios.RepositorioEmail;
import com.autobots.automanager.selecionadores.SelecionadorEmail;

@RestController
@RequestMapping("emails")
public class EmailControle {
    @Autowired
    RepositorioEmail repositorio;
    @Autowired
    AtualizadorEmail atualizador;
    @Autowired
    SelecionadorEmail selecionador;
    @Autowired
    AdicionadorLinkEmail adicionadorLink;

    @PostMapping
    public ResponseEntity<?> cadastrarEmail(
        @RequestBody Email email
    ) {
        try {
            HttpStatus status = HttpStatus.CONFLICT;
            if (email.getId() == null) {
                repositorio.save(email);
                status = HttpStatus.CREATED;
            }
		return new ResponseEntity<>(status);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> obterEmails() {
        try {
            List<Email> emails = repositorio.findAll();
            if (!emails.isEmpty()) {
                adicionadorLink.adicionarLink(emails);
                ResponseEntity<List<Email>> resposta = new ResponseEntity<>(emails, HttpStatus.OK);
                return resposta;
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> obterEmail(@PathVariable Long id) {
        try {
            List<Email> emails = repositorio.findAll();
            Email email = selecionador.selecionar(emails, id);
            if (email == null) {
                ResponseEntity<Email> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
                return resposta; 
            } else {
                adicionadorLink.adicionarLink(email);
                ResponseEntity<Email> resposta = new ResponseEntity<Email>(email, HttpStatus.FOUND);
                return resposta;
            }
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> excluirEmail(@RequestBody Email exclusao) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
		Optional<Email> email = repositorio.findById(
            exclusao.getId()
        );
		if (email != null) {
			Email emailExistente = email.get();
			repositorio.delete(emailExistente);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}

    @PutMapping
    public ResponseEntity<?> atualizarEmail(@RequestBody Email atualizacao) {
        HttpStatus status = HttpStatus.CONFLICT;
		Optional<Email> email = repositorio.findById(
            atualizacao.getId()
        );
		if (email != null) {
			Email emailExistente = email.get();
			atualizador.atualizar(emailExistente, atualizacao);
			repositorio.save(emailExistente);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
    }
}
