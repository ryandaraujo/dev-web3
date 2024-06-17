package com.autobots.automanager.atualizadores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Email;
import com.autobots.automanager.utils.StringVerificadorNulo;

@Component
public class AtualizadorEmail {
    @Autowired
    StringVerificadorNulo verificador;
    public void atualizar(Email email, Email atualizacao) {
        if(!verificador.verificar(atualizacao.getEndereco())) {
            email.setEndereco(atualizacao.getEndereco());
        }
    }
}
