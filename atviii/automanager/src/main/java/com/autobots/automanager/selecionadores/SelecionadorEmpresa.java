package com.autobots.automanager.selecionadores;

import java.util.List;

import com.autobots.automanager.entitades.Empresa;

public class SelecionadorEmpresa {
    public Empresa selecionar(List<Empresa> empresas, Long id) {
        Empresa selecionado = null;
        for(Empresa empresa:  empresas) {
            if(empresa.getId() == id) {
                selecionado = empresa;
            }
        }
        return selecionado;
    }
}
