package com.autobots.automanager.atualizadores;

import com.autobots.automanager.entitades.CredencialCodigoBarra;

public class AtualizadorCredencialCodigoBarra {

    public void atualizar(CredencialCodigoBarra credencial, CredencialCodigoBarra atualizacao) {
        if(atualizacao.isInativo() != credencial.isInativo()){
            credencial.setInativo(!credencial.isInativo());
        }
    }
}
