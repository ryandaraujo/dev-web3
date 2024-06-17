package com.autobots.automanager.selecionadores;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Mercadoria;

@Component
public class SelecionadorMercadoria {
    public Mercadoria selecionar(List<Mercadoria> mercadorias, Long id) {
        Mercadoria selecionado = null;
        for(Mercadoria mercadoria:  mercadorias) {
            if(mercadoria.getId() == id) {
                selecionado = mercadoria;
            }
        }
        return selecionado;
    }
}
