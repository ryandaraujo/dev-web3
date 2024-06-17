package com.autobots.automanager.hateoas;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.CredencialCodigoBarraControle;
import com.autobots.automanager.entidades.CredencialCodigoBarra;

@Component
public class AdicionadorLinkCredencialCodigo implements AdicionadorLink<CredencialCodigoBarra>{
    @Override
    public void adicionarLink(List<CredencialCodigoBarra> lista){
		for (CredencialCodigoBarra codigo : lista) {
			long id = codigo.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(CredencialCodigoBarraControle.class)
							.obterCodigoBarra(id))
					.withSelfRel();
			codigo.add(linkProprio);
		}
	}
    @Override
	public void adicionarLink(CredencialCodigoBarra objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(CredencialCodigoBarraControle.class)
						.obterCodigosBarra())
				.withRel("codigosBarra");
		objeto.add(linkProprio);
	}
}
