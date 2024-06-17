package com.autobots.automanager.atualizadores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.utils.StringVerificadorNulo;

@Component
public class AtualizadorEmpresa {
    @Autowired
    StringVerificadorNulo verificador;
    @Autowired
    AtualizadorEndereco atualizadorEndereco;
    @Autowired
    AtualizadorEmpresaMercadorias atualizadorMercadorias;

    private void atualizarDados(Empresa empresa, Empresa atualizacao) {
        if(!verificador.verificar(atualizacao.getNomeFantasia())) {
            empresa.setNomeFantasia(atualizacao.getNomeFantasia());
        }
        if(!verificador.verificar(atualizacao.getRazaoSocial())) {
            empresa.setRazaoSocial(atualizacao.getRazaoSocial());
        }
        if (atualizacao.getTelefones() != null) {
            empresa.getTelefones().removeIf(telefone -> !atualizacao.getTelefones().contains(telefone));
            for (Telefone telefone : atualizacao.getTelefones()) {
                if (!empresa.getTelefones().contains(telefone)) {
                    empresa.getTelefones().add(telefone);
                }
            }
        }
    }

    public void atualizar(Empresa empresa, Empresa atualizacao) {
        atualizarDados(empresa, atualizacao);
        atualizadorEndereco.atualizar(empresa.getEndereco(), atualizacao.getEndereco());
        atualizadorMercadorias.atualizarMercadorias(empresa, atualizacao.getMercadorias());
    }
}
