package com.autobots.automanager.atualizadores;

import com.autobots.automanager.entitades.CredencialUsuarioSenha;

public class AtualizadorCredencialUsuarioSenha {
    public void atualizar(CredencialUsuarioSenha credencial, CredencialUsuarioSenha atualizacao) {
        if(atualizacao.isInativo() != credencial.isInativo()){
            credencial.setInativo(!credencial.isInativo());
        }
    }
}
