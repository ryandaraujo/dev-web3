package com.autobots.automanager.selecionadores;

import java.util.List;

import com.autobots.automanager.entitades.Endereco;

public class SelecionadorEndereco {
    public Endereco selecionar(List<Endereco> enderecos, Long id) {
        Endereco selecionado = null;
        for(Endereco endereco:  enderecos) {
            if(endereco.getId() == id) {
                selecionado = endereco;
            }
        }
        return selecionado;
    }
}
