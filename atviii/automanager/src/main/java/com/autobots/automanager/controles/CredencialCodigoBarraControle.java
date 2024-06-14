package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entitades.CredencialCodigoBarra;
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
}
