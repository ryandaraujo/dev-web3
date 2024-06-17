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

import com.autobots.automanager.atualizadores.AtualizadorVenda;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.hateoas.AdicionadorLinkVenda;
import com.autobots.automanager.repositorios.RepositorioVenda;
import com.autobots.automanager.selecionadores.SelecionadorVenda;

@RestController
@RequestMapping("vendas")
public class VendaControle {
    @Autowired
    RepositorioVenda repositorio;
    @Autowired
    SelecionadorVenda selecionador;
    @Autowired
    AtualizadorVenda atualizador;
    @Autowired
    AdicionadorLinkVenda adicionadorLink;

    @PostMapping
    public ResponseEntity<?> cadastrarVenda(@RequestBody Venda venda) {
        try {
            HttpStatus status = HttpStatus.CONFLICT;
            if (venda.getId() == null) {
                repositorio.save(venda);
                status = HttpStatus.CREATED;
            }
		return new ResponseEntity<>(status);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> obterVendas() {
        try {
            List<Venda> vendas = repositorio.findAll();
            if (!vendas.isEmpty()) {
                adicionadorLink.adicionarLink(vendas);
                ResponseEntity<List<Venda>> resposta = new ResponseEntity<>(vendas, HttpStatus.OK);
                return resposta;
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> obterVenda(@PathVariable Long id) {
        try {
            List<Venda> vendas = repositorio.findAll();
            Venda venda = selecionador.selecionar(vendas, id);
            if (venda == null) {
                ResponseEntity<Venda> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
                return resposta; 
            } else {
                adicionadorLink.adicionarLink(venda);
                ResponseEntity<Venda> resposta = new ResponseEntity<Venda>(venda, HttpStatus.FOUND);
                return resposta;
            }
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> excluirVenda(@RequestBody Venda exclusao) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
		List<Venda> vendas = repositorio.findAll();
        Venda venda = selecionador.selecionar(vendas, exclusao.getId());
		if (venda != null) {
			repositorio.delete(venda);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}

    @PutMapping
    public ResponseEntity<?> atualizarVenda(@RequestBody Venda atualizacao) {
        HttpStatus status = HttpStatus.CONFLICT;
		Optional<Venda> venda = repositorio.findById(
            atualizacao.getId()
        );
		if (venda != null) {
			Venda vendaExistente = venda.get();
			atualizador.atualizar(vendaExistente, atualizacao);
			repositorio.save(vendaExistente);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
    }
}
