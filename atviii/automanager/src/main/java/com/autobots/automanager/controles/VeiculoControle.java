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

import com.autobots.automanager.atualizadores.AtualizadorVeiculo;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.hateoas.AdicionadorLinkVeiculo;
import com.autobots.automanager.repositorios.RepositorioVeiculo;
import com.autobots.automanager.selecionadores.SelecionadorVeiculo;

@RestController
@RequestMapping("veiculos")
public class VeiculoControle {
    @Autowired
    RepositorioVeiculo repositorio;
    @Autowired
    SelecionadorVeiculo selecionador;
    @Autowired
    AtualizadorVeiculo atualizador;
    @Autowired
    AdicionadorLinkVeiculo adicionadorLink;

    @PostMapping
    public ResponseEntity<?> cadastrarVeiculo(@RequestBody Veiculo veiculo) {
        try {
            HttpStatus status = HttpStatus.CONFLICT;
            if (veiculo.getId() == null) {
                repositorio.save(veiculo);
                status = HttpStatus.CREATED;
            }
		return new ResponseEntity<>(status);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> obterVeiculos() {
        try {
            List<Veiculo> veiculos = repositorio.findAll();
            if (!veiculos.isEmpty()) {
                adicionadorLink.adicionarLink(veiculos);
                ResponseEntity<List<Veiculo>> resposta = new ResponseEntity<>(veiculos, HttpStatus.OK);
                return resposta;
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> obterVeiculo(@PathVariable Long id) {
        try {
            List<Veiculo> veiculos = repositorio.findAll();
            Veiculo veiculo = selecionador.selecionar(veiculos, id);
            if (veiculo == null) {
                ResponseEntity<Veiculo> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
                return resposta; 
            } else {
                adicionadorLink.adicionarLink(veiculo);
                ResponseEntity<Veiculo> resposta = new ResponseEntity<Veiculo>(veiculo, HttpStatus.FOUND);
                return resposta;
            }
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> excluirVeiculo(@RequestBody Veiculo exclusao) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
		List<Veiculo> veiculos = repositorio.findAll();
        Veiculo veiculo = selecionador.selecionar(veiculos, exclusao.getId());
		if (veiculo != null) {
			repositorio.delete(veiculo);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}

    @PutMapping
    public ResponseEntity<?> atualizarVeiculo(@RequestBody Veiculo atualizacao) {
        HttpStatus status = HttpStatus.CONFLICT;
		Optional<Veiculo> veiculo = repositorio.findById(
            atualizacao.getId()
        );
		if (veiculo != null) {
			Veiculo veiculoExistente = veiculo.get();
			atualizador.atualizar(veiculoExistente, atualizacao);
			repositorio.save(veiculoExistente);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
    }
}
