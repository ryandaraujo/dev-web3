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

import com.autobots.automanager.atualizadores.TelefoneAtualizador;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.repositorios.TelefoneRepositorio;
import com.autobots.automanager.selecionadores.TelefoneSelecionador;

@RestController
@RequestMapping("telefones")
public class TelefoneControle {
    @Autowired
    private TelefoneRepositorio repositorio;
    @Autowired
    private TelefoneSelecionador selecionador;

    @GetMapping("{id}")
    public Telefone obterTelefone(@PathVariable Long id) {
        List<Telefone> telefones = repositorio.findAll();
        return selecionador.selecionar(telefones, id);
    }

    @GetMapping
    public List<Telefone> obterTelefones() {
        List<Telefone> telefones = repositorio.findAll();
        return telefones;
    }

    @PostMapping
    public void cadastrarTelefone(@RequestBody Telefone telefone) {
        repositorio.save(telefone);
    }

    @PutMapping
    public void atualizarTelefone(@RequestBody Telefone atualizacao) {
        Telefone telefone = repositorio.getById(atualizacao.getId());
        TelefoneAtualizador atualizador = new TelefoneAtualizador();
        atualizador.atualizar(telefone, atualizacao);
        repositorio.save(telefone);
    }

    @DeleteMapping
    public void excluirTelefone(@RequestBody Telefone exclusao) {
        Telefone telefone = repositorio.getById(exclusao.getId());
        repositorio.delete(telefone);
    }
}
