package com.autobots.automanager.atualizadores;

import com.autobots.automanager.entitades.Documento;

public class AtualizadorDocumento {
    public void atualizar(Documento documento, Documento atualizacao) {
        if(documento.getTipo() != atualizacao.getTipo() && atualizacao.getTipo() != null) {
            documento.setTipo(atualizacao.getTipo());
        }
        if(documento.getDataEmissao() != atualizacao.getDataEmissao() && atualizacao.getDataEmissao() != null) {
            documento.setDataEmissao(atualizacao.getDataEmissao());
        }
        if (
            documento.getNumero() != atualizacao.getNumero() && atualizacao.getNumero() != null
            ) {
            documento.setNumero(atualizacao.getNumero());
        }
    }
}
