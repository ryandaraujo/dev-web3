package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.atualizadores.EnderecoAtualizador;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.repositorios.EnderecoRepositorio;
import com.autobots.automanager.selecionadores.EnderecoSelecionador;

@RestController
@RequestMapping("enderecos")
public class EnderecoControle {
    @Autowired
    private EnderecoRepositorio repositorio;
    @Autowired
    private EnderecoSelecionador selecionador;

    @GetMapping("{id}")
    public Endereco obterEndereco(@PathVariable Long id) {
        List<Endereco> endereco = repositorio.findAll();
        return selecionador.selecionar(endereco, id);
    }

    @GetMapping
    public List<Endereco> obterEnderecos() {
        List<Endereco> enderecos = repositorio.findAll();
        return enderecos;
    }

    @PostMapping
    public void cadastrarEndereco(@RequestBody Endereco endereco) {
        repositorio.save(endereco);
    }

    @PutMapping
    public void atualizarEndereco(@RequestBody Endereco atualizacao) {
        Endereco endereco = repositorio.getById(atualizacao.getId());
        EnderecoAtualizador atualizador = new EnderecoAtualizador();
        atualizador.atualizar(endereco, atualizacao);
        repositorio.save(endereco);
    }

    @DeleteMapping
    public void excluirEndereco(@RequestBody Endereco exclusao) {
        Endereco endereco = repositorio.getById(exclusao.getId());
        repositorio.delete(endereco);
    }
}
