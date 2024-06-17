package com.autobots.automanager.hateoas;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.CredencialUsuarioSenhaControle;
import com.autobots.automanager.entidades.CredencialUsuarioSenha;

@Component
public class AdicionadorLinkCredencialUsuario implements AdicionadorLink<CredencialUsuarioSenha>{
    @Override
    public void adicionarLink(List<CredencialUsuarioSenha> lista){
		for (CredencialUsuarioSenha codigo : lista) {
			long id = codigo.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(CredencialUsuarioSenhaControle.class)
							.obterUsuarioSenha(id))
					.withSelfRel();
			codigo.add(linkProprio);
		}
	}
    @Override
	public void adicionarLink(CredencialUsuarioSenha objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(CredencialUsuarioSenhaControle.class)
						.obterUsuariosSenha())
				.withRel("usuarioSenha");
		objeto.add(linkProprio);
    }
}
