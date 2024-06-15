package com.autobots.automanager.atualizadores;

import org.springframework.beans.factory.annotation.Autowired;

import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.utils.StringVerificadorNulo;

public class AtualizadorMercadoria {
    @Autowired
    StringVerificadorNulo verificador;

    public void atualizar(Mercadoria mercadoria, Mercadoria atualizacao) {
        if(!verificador.verificar(atualizacao.getDescricao())) {
            mercadoria.setDescricao(atualizacao.getDescricao());
        }
        if(!verificador.verificar(atualizacao.getNome())) {
            mercadoria.setNome(atualizacao.getNome());
        }
        if(atualizacao.getFabricao() != mercadoria.getFabricao() && atualizacao.getFabricao() != null) {
            mercadoria.setFabricao(atualizacao.getFabricao());
        }
        if(atualizacao.getValidade() != mercadoria.getValidade() && atualizacao.getValidade() != null) {
            mercadoria.setValidade(atualizacao.getValidade());
        }
        if(atualizacao.getQuantidade() != mercadoria.getQuantidade()) {
            mercadoria.setQuantidade(atualizacao.getQuantidade());
        }
        if(atualizacao.getValor() != mercadoria.getValor()) {
            mercadoria.setValor(atualizacao.getValor());
        }
    }
}
