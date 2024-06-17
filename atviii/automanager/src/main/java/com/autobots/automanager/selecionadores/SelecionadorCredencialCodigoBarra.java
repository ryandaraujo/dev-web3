package com.autobots.automanager.selecionadores;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.CredencialCodigoBarra;

@Component
public class SelecionadorCredencialCodigoBarra {
    public CredencialCodigoBarra selecionar(List<CredencialCodigoBarra> credenciais, Long id) {
        CredencialCodigoBarra selecionado = null;
        for(CredencialCodigoBarra credencial:  credenciais) {
            if(credencial.getId() == id) {
                selecionado = credencial;
            }
        }
        return selecionado;
    }
}
