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

import com.autobots.automanager.atualizadores.AtualizadorDocumento;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.hateoas.AdicionadorLinkDocumento;
import com.autobots.automanager.repositorios.RepositorioDocumento;
import com.autobots.automanager.selecionadores.SelecionadorDocumento;

@RestController
@RequestMapping("documentos")
public class DocumentoControle {
    @Autowired
    RepositorioDocumento repositorio;
    @Autowired
    AtualizadorDocumento atualizador;
    @Autowired
    SelecionadorDocumento selecionador;
    @Autowired
    AdicionadorLinkDocumento adicionadorLink;

    @PostMapping
    public ResponseEntity<?> cadastrarDocumento(
        @RequestBody Documento documento
    ) {
        try {
            HttpStatus status = HttpStatus.CONFLICT;
            if (documento.getId() == null) {
                repositorio.save(documento);
                status = HttpStatus.CREATED;
            }
		return new ResponseEntity<>(status);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> obterDocumentos() {
        try {
            List<Documento> documentos = repositorio.findAll();
            if (!documentos.isEmpty()) {
                adicionadorLink.adicionarLink(documentos);
                ResponseEntity<List<Documento>> resposta = new ResponseEntity<>(documentos, HttpStatus.OK);
                return resposta;
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> obterDocumento(@PathVariable Long id) {
        try {
            List<Documento> documentos = repositorio.findAll();
            Documento documento = selecionador.selecionar(documentos, id);
            if (documento == null) {
                ResponseEntity<Documento> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
                return resposta; 
            } else {
                adicionadorLink.adicionarLink(documento);
                ResponseEntity<Documento> resposta = new ResponseEntity<Documento>(documento, HttpStatus.FOUND);
                return resposta;
            }
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> excluirDocumento(@RequestBody Documento exclusao) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
		Optional<Documento> documento = repositorio.findById(
            exclusao.getId()
        );
		if (documento != null) {
			Documento documentoExistente = documento.get();
			repositorio.delete(documentoExistente);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}

    @PutMapping
    public ResponseEntity<?> atualizarDocumento(@RequestBody Documento atualizacao) {
        HttpStatus status = HttpStatus.CONFLICT;
		Optional<Documento> documento = repositorio.findById(
            atualizacao.getId()
        );
		if (documento != null) {
			Documento documentoExistente = documento.get();
			atualizador.atualizar(documentoExistente, atualizacao);
			repositorio.save(documentoExistente);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
    }
}
