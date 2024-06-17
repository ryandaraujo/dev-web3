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

import com.autobots.automanager.atualizadores.AtualizadorTelefone;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.hateoas.AdicionadorLinkTelefone;
import com.autobots.automanager.repositorios.RepositorioTelefone;
import com.autobots.automanager.selecionadores.SelecionadorTelefone;

@RestController
@RequestMapping("telefones")
public class TelefoneControle {
    @Autowired
    RepositorioTelefone repositorio;
    @Autowired
    AtualizadorTelefone atualizador;
    @Autowired
    SelecionadorTelefone selecionador;
    @Autowired
    AdicionadorLinkTelefone adicionadorLink;

        @PostMapping
    public ResponseEntity<?> cadastrarTelefone(@RequestBody Telefone servico) {
        try {
            HttpStatus status = HttpStatus.CONFLICT;
            if (servico.getId() == null) {
                repositorio.save(servico);
                status = HttpStatus.CREATED;
            }
		return new ResponseEntity<>(status);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> obterTelefones() {
        try {
            List<Telefone> servicos = repositorio.findAll();
            if (!servicos.isEmpty()) {
                adicionadorLink.adicionarLink(servicos);
                ResponseEntity<List<Telefone>> resposta = new ResponseEntity<>(servicos, HttpStatus.OK);
                return resposta;
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> obterTelefone(@PathVariable Long id) {
        try {
            List<Telefone> servicos = repositorio.findAll();
            Telefone servico = selecionador.selecionar(servicos, id);
            if (servico == null) {
                ResponseEntity<Telefone> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
                return resposta; 
            } else {
                adicionadorLink.adicionarLink(servico);
                ResponseEntity<Telefone> resposta = new ResponseEntity<Telefone>(servico, HttpStatus.FOUND);
                return resposta;
            }
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> excluirTelefone(@RequestBody Telefone exclusao) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
		List<Telefone> servicos = repositorio.findAll();
        Telefone servico = selecionador.selecionar(servicos, exclusao.getId());
		if (servico != null) {
			repositorio.delete(servico);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}

    @PutMapping
    public ResponseEntity<?> atualizarTelefone(@RequestBody Telefone atualizacao) {
        HttpStatus status = HttpStatus.CONFLICT;
		Optional<Telefone> servico = repositorio.findById(
            atualizacao.getId()
        );
		if (servico != null) {
			Telefone servicoExistente = servico.get();
			atualizador.atualizar(servicoExistente, atualizacao);
			repositorio.save(servicoExistente);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
    }
}
