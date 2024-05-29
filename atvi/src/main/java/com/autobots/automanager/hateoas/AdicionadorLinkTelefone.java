package com.autobots.automanager.hateoas;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import com.autobots.automanager.controles.TelefoneControle;
import com.autobots.automanager.entidades.Telefone;

public class AdicionadorLinkTelefone implements AdicionadorLink<Telefone>{
    
    @Override
    public void adicionarLink(List<Telefone> lista) {
        for(Telefone telefone:lista) {
            Long id = telefone.getId();
            Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                .methodOn(TelefoneControle.class)
                .obterTelefone(id))
                .withSelfRel();
            telefone.add(linkProprio);
        }
    }

    @Override
    public void adicionarLink(Telefone objeto) {
        Link linkProprio = WebMvcLinkBuilder
            .linkTo(WebMvcLinkBuilder
            .methodOn(TelefoneControle.class)
            .obterTelefones())
            .withRel("telefones");
        objeto.add(linkProprio);
    } 
}
