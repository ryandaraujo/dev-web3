package com.autobots.automanager.atualizadores;

import org.springframework.beans.factory.annotation.Autowired;

import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.utils.StringVerificadorNulo;

public class AtualizadorEmail {
    @Autowired
    StringVerificadorNulo verificador;
    public void atualizar(Email email, Email atualizacao) {
        if(!verificador.verificar(atualizacao.getEndereco())) {
            email.setEndereco(atualizacao.getEndereco());
        }
    }
}
