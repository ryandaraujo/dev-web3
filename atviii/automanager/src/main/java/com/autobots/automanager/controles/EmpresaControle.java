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

import com.autobots.automanager.atualizadores.AtualizadorEmpresa;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.hateoas.AdicionadorLinkEmpresa;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.selecionadores.SelecionadorEmpresa;

public class EmpresaControle {
    @Autowired
    RepositorioEmpresa repositorio;
    @Autowired
    SelecionadorEmpresa selecionador;
    @Autowired
    AdicionadorLinkEmpresa adicionadorLink;
    @Autowired
    AtualizadorEmpresa atualizador;

    @PostMapping
    public ResponseEntity<?> cadastrarEmpresa(
        @RequestBody Empresa empresa
    ) {
        try {
            HttpStatus status = HttpStatus.CONFLICT;
            if (empresa.getId() == null) {
                repositorio.save(empresa);
                status = HttpStatus.CREATED;
            }
		return new ResponseEntity<>(status);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> obterEmpresas() {
        try {
            List<Empresa> empresas = repositorio.findAll();
            if (!empresas.isEmpty()) {
                adicionadorLink.adicionarLink(empresas);
                ResponseEntity<List<Empresa>> resposta = new ResponseEntity<>(empresas, HttpStatus.OK);
                return resposta;
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> obterEmpresa(@PathVariable Long id) {
        try {
            List<Empresa> empresas = repositorio.findAll();
            Empresa empresa = selecionador.selecionar(empresas, id);
            if (empresa == null) {
                ResponseEntity<Empresa> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
                return resposta; 
            } else {
                adicionadorLink.adicionarLink(empresa);
                ResponseEntity<Empresa> resposta = new ResponseEntity<Empresa>(empresa, HttpStatus.FOUND);
                return resposta;
            }
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> excluirEmpresa(@RequestBody Empresa exclusao) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
		Optional<Empresa> empresa = repositorio.findById(
            exclusao.getId()
        );
		if (empresa != null) {
			Empresa empresaExistente = empresa.get();
			repositorio.delete(empresaExistente);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}

    @PutMapping
    public ResponseEntity<?> atualizarEmpresa(@RequestBody Empresa atualizacao) {
        HttpStatus status = HttpStatus.CONFLICT;
		Optional<Empresa> empresa = repositorio.findById(
            atualizacao.getId()
        );
		if (empresa != null) {
			Empresa empresaExistente = empresa.get();
			atualizador.atualizar(empresaExistente, atualizacao);
			repositorio.save(empresaExistente);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
    }
}
