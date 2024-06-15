package com.autobots.automanager.selecionadores;

import java.util.List;

import com.autobots.automanager.entitades.Documento;

public class SelecionadorDocumento {
    public Documento selecionar(List<Documento> documentos, Long id) {
        Documento selecionado = null;
        for(Documento documento:  documentos) {
            if(documento.getId() == id) {
                selecionado = documento;
            }
        }
        return selecionado;
    }
}
