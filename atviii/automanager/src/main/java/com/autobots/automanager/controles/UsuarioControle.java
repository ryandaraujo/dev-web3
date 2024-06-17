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

import com.autobots.automanager.atualizadores.AtualizadorUsuario;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.hateoas.AdicionadorLinkUsuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.selecionadores.SelecionadorUsuario;

@RestController
@RequestMapping("usuarios")
public class UsuarioControle {
    @Autowired
    RepositorioUsuario repositorio;
    @Autowired
    AtualizadorUsuario atualizador;
    @Autowired
    SelecionadorUsuario selecionador;
    @Autowired
    AdicionadorLinkUsuario adicionadorLink;

    @PostMapping
    public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario) {
        try {
            HttpStatus status = HttpStatus.CONFLICT;
            if (usuario.getId() == null) {
                repositorio.save(usuario);
                status = HttpStatus.CREATED;
            }
		return new ResponseEntity<>(status);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> obterUsuarios() {
        try {
            List<Usuario> usuarios = repositorio.findAll();
            if (!usuarios.isEmpty()) {
                adicionadorLink.adicionarLink(usuarios);
                ResponseEntity<List<Usuario>> resposta = new ResponseEntity<>(usuarios, HttpStatus.OK);
                return resposta;
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> obterUsuario(@PathVariable Long id) {
        try {
            List<Usuario> usuarios = repositorio.findAll();
            Usuario usuario = selecionador.selecionar(usuarios, id);
            if (usuario == null) {
                ResponseEntity<Usuario> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
                return resposta; 
            } else {
                adicionadorLink.adicionarLink(usuario);
                ResponseEntity<Usuario> resposta = new ResponseEntity<Usuario>(usuario, HttpStatus.FOUND);
                return resposta;
            }
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> excluirUsuario(@RequestBody Usuario exclusao) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
		List<Usuario> usuarios = repositorio.findAll();
        Usuario usuario = selecionador.selecionar(usuarios, exclusao.getId());
		if (usuario != null) {
			repositorio.delete(usuario);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}

    @PutMapping
    public ResponseEntity<?> atualizarUsuario(@RequestBody Usuario atualizacao) {
        HttpStatus status = HttpStatus.CONFLICT;
		Optional<Usuario> usuario = repositorio.findById(
            atualizacao.getId()
        );
		if (usuario != null) {
			Usuario usuarioExistente = usuario.get();
			atualizador.atualizar(usuarioExistente, atualizacao);
			repositorio.save(usuarioExistente);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
    }
}
