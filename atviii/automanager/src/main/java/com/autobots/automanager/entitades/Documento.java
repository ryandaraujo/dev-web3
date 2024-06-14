package com.autobots.automanager.entitades;

import java.util.Date;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import com.autobots.automanager.enumeracoes.TipoDocumento;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Documento extends RepresentationModel<Documento>{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private TipoDocumento tipo;

	@Column(nullable = false)
	private Date dataEmissao;

	@Column(unique = true, nullable = false)
	private String numero;
}