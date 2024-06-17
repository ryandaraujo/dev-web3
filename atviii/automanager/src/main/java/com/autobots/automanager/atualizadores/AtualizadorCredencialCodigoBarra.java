package com.autobots.automanager.atualizadores;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.CredencialCodigoBarra;

@Component
public class AtualizadorCredencialCodigoBarra {

    public void atualizar(CredencialCodigoBarra credencial, CredencialCodigoBarra atualizacao) {
        if(atualizacao.isInativo() != credencial.isInativo()){
            credencial.setInativo(!credencial.isInativo());
        }
    }
}
