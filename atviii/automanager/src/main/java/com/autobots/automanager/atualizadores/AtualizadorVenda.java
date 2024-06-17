package com.autobots.automanager.atualizadores;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.repositorios.RepositorioMercadoria;
import com.autobots.automanager.repositorios.RepositorioServico;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVeiculo;
import com.autobots.automanager.utils.StringVerificadorNulo;

@Component
public class AtualizadorVenda {
    @Autowired
    private RepositorioUsuario repositorioUsuario;
    @Autowired
    private RepositorioMercadoria repositorioMercadoria;
    @Autowired
    private RepositorioServico repositorioServico;
    @Autowired
    private RepositorioVeiculo repositorioVeiculo;
    @Autowired
    private StringVerificadorNulo verificador;

    public void atualizar(Venda venda, Venda atualizacao) {
        if (atualizacao.getCadastro() != null && !atualizacao.getCadastro().equals(venda.getCadastro())) {
            venda.setCadastro(atualizacao.getCadastro());
        }
        if (!verificador.verificar(atualizacao.getIdentificacao())) {
            venda.setIdentificacao(atualizacao.getIdentificacao());
        }
        if (atualizacao.getCliente() != null && !atualizacao.getCliente().equals(venda.getCliente())) {
            Usuario novoCliente = repositorioUsuario.findById(atualizacao.getCliente().getId())
                .orElseThrow(() -> new IllegalArgumentException(
                    "Cliente não encontrado: " + atualizacao.getCliente().getId()
                ));
            venda.setCliente(novoCliente);
        }
        if (atualizacao.getFuncionario() != null && !atualizacao.getFuncionario().equals(venda.getFuncionario())) {
            Usuario novoFuncionario = repositorioUsuario.findById(atualizacao.getFuncionario().getId())
                .orElseThrow(() -> new IllegalArgumentException(
                    "Funcionário não encontrado: " + atualizacao.getFuncionario().getId()
                ));
            venda.setFuncionario(novoFuncionario);
        }
        if (atualizacao.getMercadorias() != null && !atualizacao.getMercadorias().equals(venda.getMercadorias())) {
            Set<Mercadoria> novasMercadorias = atualizacao.getMercadorias();
            venda.getMercadorias().clear();
            for (Mercadoria mercadoria : novasMercadorias) {
                Mercadoria novaMercadoria = repositorioMercadoria.findById(mercadoria.getId())
                    .orElseThrow(() -> new IllegalArgumentException(
                        "Mercadoria não encontrada: " + mercadoria.getId()
                    ));
                venda.getMercadorias().add(novaMercadoria);
            }
        }
        if (atualizacao.getServicos() != null && !atualizacao.getServicos().equals(venda.getServicos())) {
            Set<Servico> novosServicos = atualizacao.getServicos();
            venda.getServicos().clear();
            for (Servico servico : novosServicos) {
                Servico novoServico = repositorioServico.findById(servico.getId())
                    .orElseThrow(() -> new IllegalArgumentException(
                        "Serviço não encontrado: " + servico.getId()
                    ));
                venda.getServicos().add(novoServico);
            }
        }
        if (atualizacao.getVeiculo() != null && !atualizacao.getVeiculo().equals(venda.getVeiculo())) {
            Veiculo novoVeiculo = repositorioVeiculo.findById(atualizacao.getVeiculo().getId())
                .orElseThrow(() -> new IllegalArgumentException(
                    "Veículo não encontrado: " + atualizacao.getVeiculo().getId()
                ));
            venda.setVeiculo(novoVeiculo);
        }
    }
}
