package com.autobots.automanager.atualizadores;

import org.springframework.beans.factory.annotation.Autowired;

import com.autobots.automanager.entitades.Telefone;
import com.autobots.automanager.utils.StringVerificadorNulo;

public class AtualizadorTelefone {
    @Autowired
    StringVerificadorNulo verificador;

    public void atualizar(Telefone telefone, Telefone atualizacao) {
        if(!verificador.verificar(atualizacao.getDdd())) {
            telefone.setDdd(atualizacao.getDdd());
        }
        if(!verificador.verificar(atualizacao.getNumero())) {
            telefone.setNumero(atualizacao.getNumero());
        }
    }
}
