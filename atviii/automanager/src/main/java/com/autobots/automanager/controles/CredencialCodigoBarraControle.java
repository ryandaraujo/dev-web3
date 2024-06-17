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

import com.autobots.automanager.atualizadores.AtualizadorCredencialCodigoBarra;
import com.autobots.automanager.entidades.CredencialCodigoBarra;
import com.autobots.automanager.hateoas.AdicionadorLinkCredencialCodigo;
import com.autobots.automanager.repositorios.RepositorioCredencialCodigoBarra;
import com.autobots.automanager.selecionadores.SelecionadorCredencialCodigoBarra;

@RestController
@RequestMapping("credenciais/codigoBarra")
public class CredencialCodigoBarraControle {

    @Autowired
    RepositorioCredencialCodigoBarra repositorioCodigoBarra;

    @Autowired
    SelecionadorCredencialCodigoBarra selecionador;

    @Autowired
    AdicionadorLinkCredencialCodigo adicionadorLink;

    @Autowired
    AtualizadorCredencialCodigoBarra atualizador;

    @PostMapping
    public ResponseEntity<?> cadastrarCodigoBarra(
        @RequestBody CredencialCodigoBarra credencial
    ) {
        try {
            HttpStatus status = HttpStatus.CONFLICT;
            if (credencial.getId() == null) {
                repositorioCodigoBarra.save(credencial);
                status = HttpStatus.CREATED;
            }
		return new ResponseEntity<>(status);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> obterCodigosBarra() {
        try {
            List<CredencialCodigoBarra> credenciais = repositorioCodigoBarra.findAll();
            if (!credenciais.isEmpty()) {
                adicionadorLink.adicionarLink(credenciais);
                ResponseEntity<List<CredencialCodigoBarra>> resposta = new ResponseEntity<>(credenciais, HttpStatus.OK);
                return resposta;
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> obterCodigoBarra(@PathVariable Long id) {
        try {
            List<CredencialCodigoBarra> credenciais = repositorioCodigoBarra.findAll();
            CredencialCodigoBarra credencial = selecionador.selecionar(credenciais, id);
            if (credencial == null) {
                ResponseEntity<CredencialCodigoBarra> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
                return resposta; 
            } else {
                adicionadorLink.adicionarLink(credencial);
                ResponseEntity<CredencialCodigoBarra> resposta = new ResponseEntity<CredencialCodigoBarra>(credencial, HttpStatus.FOUND);
                return resposta;
            }
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> excluirCodigoBarra(@RequestBody CredencialCodigoBarra exclusao) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
		Optional<CredencialCodigoBarra> credencial = repositorioCodigoBarra.findById(
            exclusao.getId()
        );
		if (credencial != null) {
			CredencialCodigoBarra credencialExistente = credencial.get();
			repositorioCodigoBarra.delete(credencialExistente);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}

    @PutMapping
    public ResponseEntity<?> atualizarCredencial(@RequestBody CredencialCodigoBarra atualizacao) {
        HttpStatus status = HttpStatus.CONFLICT;
		Optional<CredencialCodigoBarra> credencial = repositorioCodigoBarra.findById(
            atualizacao.getId()
        );
		if (credencial != null) {
			CredencialCodigoBarra credencialExistente = credencial.get();
			atualizador.atualizar(credencialExistente, atualizacao);
			repositorioCodigoBarra.save(credencialExistente);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
    }
}
