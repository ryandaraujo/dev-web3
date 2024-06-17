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

import com.autobots.automanager.atualizadores.AtualizadorServico;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.hateoas.AdicionadorLinkServico;
import com.autobots.automanager.repositorios.RepositorioServico;
import com.autobots.automanager.selecionadores.SelecionadorServico;

@RestController
@RequestMapping("servicos")
public class ServicoControle {
    @Autowired
    RepositorioServico repositorio;
    @Autowired
    SelecionadorServico selecionador;
    @Autowired
    AtualizadorServico atualizador;
    @Autowired
    AdicionadorLinkServico adicionadorLink;

    @PostMapping
    public ResponseEntity<?> cadastrarServico(@RequestBody Servico servico) {
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
    public ResponseEntity<?> obterServicos() {
        try {
            List<Servico> servicos = repositorio.findAll();
            if (!servicos.isEmpty()) {
                adicionadorLink.adicionarLink(servicos);
                ResponseEntity<List<Servico>> resposta = new ResponseEntity<>(servicos, HttpStatus.OK);
                return resposta;
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> obterServico(@PathVariable Long id) {
        try {
            List<Servico> servicos = repositorio.findAll();
            Servico servico = selecionador.selecionar(servicos, id);
            if (servico == null) {
                ResponseEntity<Servico> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
                return resposta; 
            } else {
                adicionadorLink.adicionarLink(servico);
                ResponseEntity<Servico> resposta = new ResponseEntity<Servico>(servico, HttpStatus.FOUND);
                return resposta;
            }
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> excluirServico(@RequestBody Servico exclusao) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
		List<Servico> servicos = repositorio.findAll();
        Servico servico = selecionador.selecionar(servicos, exclusao.getId());
		if (servico != null) {
			repositorio.delete(servico);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}

    @PutMapping
    public ResponseEntity<?> atualizarServico(@RequestBody Servico atualizacao) {
        HttpStatus status = HttpStatus.CONFLICT;
		Optional<Servico> servico = repositorio.findById(
            atualizacao.getId()
        );
		if (servico != null) {
			Servico servicoExistente = servico.get();
			atualizador.atualizar(servicoExistente, atualizacao);
			repositorio.save(servicoExistente);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
    }
}
