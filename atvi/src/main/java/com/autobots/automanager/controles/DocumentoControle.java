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

import com.autobots.automanager.atualizadores.DocumentoAtualizador;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.repositorios.DocumentoRepositorio;
import com.autobots.automanager.selecionadores.DocumentoSelecionador;

@RestController
@RequestMapping("documentos")
public class DocumentoControle {
    @Autowired
    private DocumentoRepositorio repositorio;
    @Autowired
    private DocumentoSelecionador selecionador;
    
    @GetMapping("{id}")
    public ResponseEntity<Documento> obterDocumento(@PathVariable Long id) {
        List<Documento> documentos = repositorio.findAll();
        Documento documento = selecionador.selecionar(documentos, id);
        if (documento == null) {
			ResponseEntity<Documento> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta; 
		} else {
			ResponseEntity<Documento> resposta = new ResponseEntity<>(HttpStatus.FOUND);
			return resposta;
		}
    }

    @GetMapping
    public ResponseEntity<List<Documento>> obterDocumentos() {
        List<Documento> documentos = repositorio.findAll();
        if (documentos.isEmpty()) {
			ResponseEntity<List<Documento>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			ResponseEntity<List<Documento>> resposta = new ResponseEntity<>(HttpStatus.FOUND);
			return resposta;
		}
    }

    @PostMapping
    public ResponseEntity<?> cadastrarDocumento(@RequestBody Documento documento) {
        HttpStatus status = HttpStatus.CONFLICT;
		if (documento.getId() == null) {
			repositorio.save(documento);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
    }

    @PutMapping
    public ResponseEntity<?> atualizarDocumento(@RequestBody Documento atualizacao) {
        HttpStatus status = HttpStatus.CONFLICT;
		Optional<Documento> documento = repositorio.findById(atualizacao.getId());
		if (documento != null) {
			Documento documentoExistente = documento.get();
			DocumentoAtualizador atualizador = new DocumentoAtualizador();
			atualizador.atualizar(documentoExistente, atualizacao);
			repositorio.save(documentoExistente);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
    }

    @DeleteMapping
    public ResponseEntity<?> excluirDocumento(@RequestBody Documento exclusao) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
		Optional<Documento> documento = repositorio.findById(exclusao.getId());
		if (documento != null) {
			Documento documentoExistente = documento.get();
			repositorio.delete(documentoExistente);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
    }
}
