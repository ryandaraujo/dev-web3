package com.autobots.automanager.selecionadores;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.CredencialUsuarioSenha;

@Component
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
