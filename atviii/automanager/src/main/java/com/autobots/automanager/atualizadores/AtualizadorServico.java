package com.autobots.automanager.atualizadores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.utils.StringVerificadorNulo;

@Component
public class AtualizadorServico {
    @Autowired
    StringVerificadorNulo verificador;

    public void atualizar(Servico servico, Servico atualizacao) {
        if(!verificador.verificar(atualizacao.getDescricao())) {
            servico.setDescricao(atualizacao.getDescricao());
        }
        if(!verificador.verificar(atualizacao.getNome())) {
            servico.setNome(atualizacao.getNome());
        }
        if(atualizacao.getValor() != servico.getValor()) {
            servico.setValor(atualizacao.getValor());
        }
    }
}
