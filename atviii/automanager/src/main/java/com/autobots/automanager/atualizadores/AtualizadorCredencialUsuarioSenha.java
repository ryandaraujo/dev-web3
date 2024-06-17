package com.autobots.automanager.atualizadores;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.CredencialUsuarioSenha;

@Component
public class AtualizadorCredencialUsuarioSenha {
    public void atualizar(CredencialUsuarioSenha credencial, CredencialUsuarioSenha atualizacao) {
        if(atualizacao.isInativo() != credencial.isInativo()){
            credencial.setInativo(!credencial.isInativo());
        }
    }
}
