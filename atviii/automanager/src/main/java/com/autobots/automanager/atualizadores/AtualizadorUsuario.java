package com.autobots.automanager.atualizadores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Email;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.utils.StringVerificadorNulo;

@Component
public class AtualizadorUsuario {
    @Autowired
    StringVerificadorNulo verificador;
    @Autowired
    AtualizadorEndereco atualizadorEndereco;
    @Autowired
    AtualizadorMercadorias atualizadorMercadorias;

    private void atualizarDados(Usuario usuario, Usuario atualizacao) {
        if (!verificador.verificar(atualizacao.getNome())) {
            usuario.setNome(atualizacao.getNome());
        }
        if (!verificador.verificar(atualizacao.getNomeSocial())) {
            usuario.setNomeSocial(atualizacao.getNomeSocial());
        }
        if (atualizacao.getPerfis() != null && !atualizacao.getPerfis().isEmpty()) {
            usuario.getPerfis().clear();
            usuario.getPerfis().addAll(atualizacao.getPerfis());
        }
        if (atualizacao.getTelefones() != null) {
            usuario.getTelefones().removeIf(telefone -> !atualizacao.getTelefones().contains(telefone));
            for (Telefone telefone : atualizacao.getTelefones()) {
                if (!usuario.getTelefones().contains(telefone)) {
                    usuario.getTelefones().add(telefone);
                }
            }
        }
        if (atualizacao.getDocumentos() != null) {
            usuario.getDocumentos().removeIf(documento -> !atualizacao.getDocumentos().contains(documento));
            for (Documento documentos : atualizacao.getDocumentos()) {
                if (!usuario.getDocumentos().contains(documentos)) {
                    usuario.getDocumentos().add(documentos);
                }
            }
        }
        if (atualizacao.getEmails() != null) {
            usuario.getEmails().removeIf(email -> !atualizacao.getEmails().contains(email));
            for (Email email : atualizacao.getEmails()) {
                if (!usuario.getEmails().contains(email)) {
                    usuario.getEmails().add(email);
                }
            }
        }
        if (atualizacao.getCredenciais() != null) {
            usuario.getCredenciais().removeIf(Credencial -> !atualizacao.getCredenciais().contains(Credencial));
            for (Credencial Credencial : atualizacao.getCredenciais()) {
                if (!usuario.getCredenciais().contains(Credencial)) {
                    usuario.getCredenciais().add(Credencial);
                }
            }
        }
    }

    public void atualizar(Usuario usuario, Usuario atualizacao) {
        atualizarDados(usuario, atualizacao);
        atualizadorEndereco.atualizar(usuario.getEndereco(), atualizacao.getEndereco());
        atualizadorMercadorias.atualizarMercadorias(usuario, atualizacao.getMercadorias());
    }
}
