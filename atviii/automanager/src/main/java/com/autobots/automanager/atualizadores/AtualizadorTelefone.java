package com.autobots.automanager.atualizadores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.utils.StringVerificadorNulo;

@Component
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
