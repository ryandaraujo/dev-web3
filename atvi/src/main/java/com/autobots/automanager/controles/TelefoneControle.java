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

import com.autobots.automanager.atualizadores.TelefoneAtualizador;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.hateoas.AdicionadorLinkTelefone;
import com.autobots.automanager.repositorios.TelefoneRepositorio;
import com.autobots.automanager.selecionadores.TelefoneSelecionador;

@RestController
@RequestMapping("telefones")
public class TelefoneControle {
    @Autowired
    private TelefoneRepositorio repositorio;
    @Autowired
    private TelefoneSelecionador selecionador;
	@Autowired
	private AdicionadorLinkTelefone adicionadorLink;

    @GetMapping("{id}")
    public ResponseEntity<Telefone> obterTelefone(@PathVariable Long id) {
        List<Telefone> telefones = repositorio.findAll();
		Telefone telefone = selecionador.selecionar(telefones, id);
		if (telefone == null) {
			ResponseEntity<Telefone> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta; 
		} else {
			adicionadorLink.adicionarLink(telefone);
			ResponseEntity<Telefone> resposta = new ResponseEntity<Telefone>(telefone, HttpStatus.FOUND);
			return resposta;
		}
    }

    @GetMapping
    public ResponseEntity<List<Telefone>> obterTelefones() {
        List<Telefone> telefones = repositorio.findAll();
        if (telefones.isEmpty()) {
			ResponseEntity<List<Telefone>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(telefones);
			ResponseEntity<List<Telefone>> resposta = new ResponseEntity<List<Telefone>>(telefones, HttpStatus.FOUND);
			return resposta;
		}
    }

    @PostMapping
    public ResponseEntity<?> cadastrarTelefone(@RequestBody Telefone telefone) {
        HttpStatus status = HttpStatus.CONFLICT;
		if (telefone.getId() == null) {
			repositorio.save(telefone);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
    }

    @PutMapping
    public ResponseEntity<?> atualizarTelefone(@RequestBody Telefone atualizacao) {
        HttpStatus status = HttpStatus.CONFLICT;
		Optional<Telefone> telefone = repositorio.findById(atualizacao.getId());
		if (telefone != null) {
			TelefoneAtualizador atualizador = new TelefoneAtualizador();
			Telefone telefoneExistente = telefone.get();
			atualizador.atualizar(telefoneExistente, atualizacao);
			repositorio.save(telefoneExistente);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
    }

    @DeleteMapping
    public ResponseEntity<?> excluirTelefone(@RequestBody Telefone exclusao) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
		Optional<Telefone> telefone = repositorio.findById(exclusao.getId());
		if (telefone != null) {
			Telefone telefoneExistente = telefone.get();
			repositorio.delete(telefoneExistente);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
    }
}
