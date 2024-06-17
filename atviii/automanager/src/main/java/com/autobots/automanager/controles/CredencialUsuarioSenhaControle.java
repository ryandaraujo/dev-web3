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

import com.autobots.automanager.atualizadores.AtualizadorCredencialUsuarioSenha;
import com.autobots.automanager.entidades.CredencialUsuarioSenha;
import com.autobots.automanager.hateoas.AdicionadorLinkCredencialUsuario;
import com.autobots.automanager.repositorios.RepositorioCredencialUsuarioSenha;
import com.autobots.automanager.selecionadores.SelecionadorCredencialUsuarioSenha;

@RestController
@RequestMapping("credenciais/usuarioSenha")
public class CredencialUsuarioSenhaControle {
    @Autowired
    RepositorioCredencialUsuarioSenha repositorio;

    @Autowired
    SelecionadorCredencialUsuarioSenha selecionador;

    @Autowired
    AdicionadorLinkCredencialUsuario adicionadorLink;

    @Autowired
    AtualizadorCredencialUsuarioSenha atualizador;

    @PostMapping
    public ResponseEntity<?> cadastrarUsuarioSenha(
        @RequestBody CredencialUsuarioSenha credencial
    ) {
        try {
            HttpStatus status = HttpStatus.CONFLICT;
            if (credencial.getId() == null) {
                repositorio.save(credencial);
                status = HttpStatus.CREATED;
            }
		return new ResponseEntity<>(status);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> obterUsuariosSenha() {
        try {
            List<CredencialUsuarioSenha> credenciais = repositorio.findAll();
            if (!credenciais.isEmpty()) {
                adicionadorLink.adicionarLink(credenciais);
                ResponseEntity<List<CredencialUsuarioSenha>> resposta = new ResponseEntity<>(credenciais, HttpStatus.OK);
                return resposta;
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> obterUsuarioSenha(@PathVariable Long id) {
        try {
            List<CredencialUsuarioSenha> credenciais = repositorio.findAll();
            CredencialUsuarioSenha credencial = selecionador.selecionar(credenciais, id);
            if (credencial == null) {
                ResponseEntity<CredencialUsuarioSenha> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
                return resposta; 
            } else {
                adicionadorLink.adicionarLink(credencial);
                ResponseEntity<CredencialUsuarioSenha> resposta = new ResponseEntity<CredencialUsuarioSenha>(credencial, HttpStatus.FOUND);
                return resposta;
            }
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> excluirUsuarioSenha(@RequestBody CredencialUsuarioSenha exclusao) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
		Optional<CredencialUsuarioSenha> credencial = repositorio.findById(
            exclusao.getId()
        );
		if (credencial != null) {
			CredencialUsuarioSenha credencialExistente = credencial.get();
			repositorio.delete(credencialExistente);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}

    @PutMapping
    public ResponseEntity<?> atualizarUsuarioSenha(@RequestBody CredencialUsuarioSenha atualizacao) {
        HttpStatus status = HttpStatus.CONFLICT;
		Optional<CredencialUsuarioSenha> credencial = repositorio.findById(
            atualizacao.getId()
        );
		if (credencial != null) {
			CredencialUsuarioSenha credencialExistente = credencial.get();
			atualizador.atualizar(credencialExistente, atualizacao);
			repositorio.save(credencialExistente);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
    }
}
