package com.autobots.automanager.atualizadores;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.repositorios.RepositorioMercadoria;

@Component
public class AtualizadorMercadorias {
    @Autowired
    AtualizadorMercadoria atualizadorMercadoria;
    @Autowired
    RepositorioMercadoria repositorio;

    public void atualizarMercadorias(Usuario usuario, Set<Mercadoria> novasMercadorias) {
        Set<Mercadoria> mercadoriasAtualizadas = new HashSet<>();

        for (Mercadoria novaMercadoria : novasMercadorias) {
            boolean encontrada = false;
            for (Mercadoria mercadoriaExistente : usuario.getMercadorias()) {
                if (mercadoriaExistente.getId().equals(novaMercadoria.getId())) {
                    encontrada = true;
                    atualizadorMercadoria.atualizar(mercadoriaExistente, novaMercadoria);
                    mercadoriasAtualizadas.add(mercadoriaExistente);
                    break;
                }
            }
            if (!encontrada) {
                Mercadoria mercadoriaNova = new Mercadoria();
                mercadoriaNova.setNome(novaMercadoria.getNome());
                mercadoriasAtualizadas.add(mercadoriaNova);
            }
        }

        usuario.getMercadorias().removeIf(
            mercadoria -> !mercadoriasAtualizadas.contains(mercadoria)
        );

        for (Mercadoria mercadoria : mercadoriasAtualizadas) {
            if (mercadoria.getId() == null) {
                usuario.getMercadorias().add(mercadoria);
            } else {
                repositorio.save(mercadoria);
            }
        }
    }
}
