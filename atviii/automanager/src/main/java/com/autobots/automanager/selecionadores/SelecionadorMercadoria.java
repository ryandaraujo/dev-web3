package com.autobots.automanager.selecionadores;

import java.util.List;

import com.autobots.automanager.entitades.Mercadoria;


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
