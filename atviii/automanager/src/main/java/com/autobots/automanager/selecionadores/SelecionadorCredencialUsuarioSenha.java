package com.autobots.automanager.selecionadores;

import java.util.List;

import com.autobots.automanager.entitades.CredencialUsuarioSenha;

public class SelecionadorCredencialUsuarioSenha {
    public CredencialUsuarioSenha selecionar(List<CredencialUsuarioSenha> credenciais, Long id) {
        CredencialUsuarioSenha selecionado = null;
        for(CredencialUsuarioSenha credencial:  credenciais) {
            if(credencial.getId() == id) {
                selecionado = credencial;
            }
        }
        return selecionado;
    }
}
