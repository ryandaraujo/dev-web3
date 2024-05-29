package com.autobots.automanager.hateoas;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import com.autobots.automanager.controles.EnderecoControle;
import com.autobots.automanager.entidades.Endereco;

public class AdicionadorLinkEndereco implements AdicionadorLink<Endereco>{

    @Override
    public void adicionarLink(List<Endereco> lista) {
        for (Endereco endereco: lista) {
            Long id = endereco.getId();
            Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                .methodOn(EnderecoControle.class)
                .obterEndereco(id))
                .withSelfRel();
            endereco.add(linkProprio);
        }
    }

    @Override
    public void adicionarLink(Endereco objeto) {
        Link linkProprio = WebMvcLinkBuilder
            .linkTo(WebMvcLinkBuilder
            .methodOn(EnderecoControle.class)
            .obterEnderecos())
            .withRel("enderecos");
        objeto.add(linkProprio);
    }
}
