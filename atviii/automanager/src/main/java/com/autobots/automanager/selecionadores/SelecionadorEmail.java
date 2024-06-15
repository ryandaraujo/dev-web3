package com.autobots.automanager.selecionadores;

import java.util.List;

import com.autobots.automanager.entitades.Email;

public class SelecionadorEmail {
    public Email selecionar(List<Email> emails, Long id) {
        Email selecionado = null;
        for(Email email:  emails) {
            if(email.getId() == id) {
                selecionado = email;
            }
        }
        return selecionado;
    }
}
