package com.autobots.automanager.selecionadores;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Email;

@Component
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
