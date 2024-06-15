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

import com.autobots.automanager.atualizadores.AtualizadorMercadoria;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.hateoas.AdicionadorLinkMercadoria;
import com.autobots.automanager.repositorios.RepositorioMercadoria;
import com.autobots.automanager.selecionadores.SelecionadorMercadoria;

public class MercadoriaControle {
    @Autowired
    RepositorioMercadoria repositorio;
    @Autowired
    SelecionadorMercadoria selecionador;
    @Autowired
    AtualizadorMercadoria atualizador;
    @Autowired
    AdicionadorLinkMercadoria adicionadorLink;

    @PostMapping
    public ResponseEntity<?> cadastrarMercadoria(@RequestBody Mercadoria mercadoria) {
        try {
            HttpStatus status = HttpStatus.CONFLICT;
            if (mercadoria.getId() == null) {
                repositorio.save(mercadoria);
                status = HttpStatus.CREATED;
            }
		return new ResponseEntity<>(status);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> obterMercadorias() {
        try {
            List<Mercadoria> mercadorias = repositorio.findAll();
            if (!mercadorias.isEmpty()) {
                adicionadorLink.adicionarLink(mercadorias);
                ResponseEntity<List<Mercadoria>> resposta = new ResponseEntity<>(mercadorias, HttpStatus.OK);
                return resposta;
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> obterMercadoria(@PathVariable Long id) {
        try {
            List<Mercadoria> mercadorias = repositorio.findAll();
            Mercadoria mercadoria = selecionador.selecionar(mercadorias, id);
            if (mercadoria == null) {
                ResponseEntity<Mercadoria> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
                return resposta; 
            } else {
                adicionadorLink.adicionarLink(mercadoria);
                ResponseEntity<Mercadoria> resposta = new ResponseEntity<Mercadoria>(mercadoria, HttpStatus.FOUND);
                return resposta;
            }
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> excluirMercadoria(@RequestBody Mercadoria exclusao) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
		List<Mercadoria> mercadorias = repositorio.findAll();
        Mercadoria mercadoria = selecionador.selecionar(mercadorias, exclusao.getId());
		if (mercadoria != null) {
			repositorio.delete(mercadoria);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}

    @PutMapping
    public ResponseEntity<?> atualizarMercadoria(@RequestBody Mercadoria atualizacao) {
        HttpStatus status = HttpStatus.CONFLICT;
		Optional<Mercadoria> mercadoria = repositorio.findById(
            atualizacao.getId()
        );
		if (mercadoria != null) {
			Mercadoria mercadoriaExistente = mercadoria.get();
			atualizador.atualizar(mercadoriaExistente, atualizacao);
			repositorio.save(mercadoriaExistente);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
    }
}
