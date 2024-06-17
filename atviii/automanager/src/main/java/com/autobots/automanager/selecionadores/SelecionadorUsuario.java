package com.autobots.automanager.selecionadores;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Usuario;

@Component
public class SelecionadorUsuario {
    public Usuario selecionar(List<Usuario> usuarios, Long id) {
        Usuario selecionado = null;
        for(Usuario usuario:  usuarios) {
            if(usuario.getId() == id) {
                selecionado = usuario;
            }
        }
        return selecionado;
    }
}
