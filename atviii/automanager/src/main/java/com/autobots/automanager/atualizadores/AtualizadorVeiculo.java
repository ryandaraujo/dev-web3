package com.autobots.automanager.atualizadores;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVenda;
import com.autobots.automanager.utils.StringVerificadorNulo;

@Component
public class AtualizadorVeiculo {
    @Autowired
    StringVerificadorNulo verificador;
    @Autowired
    RepositorioVenda repositorioVenda;
    @Autowired
    RepositorioUsuario repositorioUsuario;

    public void atualizar(Veiculo veiculo, Veiculo atualizacao) {
        if(veiculo.getTipo() != atualizacao.getTipo() && atualizacao.getTipo() != null) {
            veiculo.setTipo(atualizacao.getTipo());
        }
        if (!verificador.verificar(atualizacao.getModelo())) {
            veiculo.setModelo(atualizacao.getModelo());
        }
        if (!verificador.verificar(atualizacao.getPlaca())) {
            veiculo.setModelo(atualizacao.getPlaca());
        }
        if (atualizacao.getProprietario() != null && !atualizacao.getProprietario().equals(veiculo.getProprietario())) {
            Usuario novoProprietario = repositorioUsuario.findById(atualizacao.getProprietario().getId())
                                                    .orElseThrow(() -> new IllegalArgumentException("Proprietário não encontrado: " + atualizacao.getProprietario().getId()));
            veiculo.setProprietario(novoProprietario);
        }

        // Atualizar vendas se diferente
        if (atualizacao.getVendas() != null && !atualizacao.getVendas().equals(veiculo.getVendas())) {
            Set<Venda> novasVendas = atualizacao.getVendas();
            veiculo.getVendas().clear(); // Limpar vendas atuais
            for (Venda venda : novasVendas) {
                Venda novaVenda = repositorioVenda.findById(venda.getId())
                    .orElseThrow(
                        () -> new IllegalArgumentException("Venda não encontrada: " + venda.getId())
                    );
                veiculo.getVendas().add(novaVenda);
            }
        }
    }
}
