package br.com.senet.controller;

import br.com.senet.model.EstadoJogo;
import br.com.senet.model.ResultadoJogada;

public class ControladorJogo {

    private final EstadoJogo estado;

    public ControladorJogo(EstadoJogo estado) {
        this.estado = estado;
    }

    public void rolarDado() {
        if (estado.alguemVenceu()) return;
        if (estado.getUltimoDado() != 0) {
            estado.setMensagem("Você já rolou o dado. Faça um movimento.");
            return;
        }
        int valor = estado.rolar();
        estado.setUltimoDado(valor);
        boolean temMov = estado.existeAlgumMovimento(valor);
        if (!temMov) {
            estado.setMensagem("Não existem movimentos válidos para o valor " + valor + ". Você pode 'Passar Vez'.");
            return;
        }
        estado.setMensagem(estado.nomeDoJogador(estado.getJogadorAtualId()) + " rolou " + valor + ". Selecione uma peça.");
    }

    public void passarVez() {
        int valor = estado.getUltimoDado();
        if (valor == 0) {
            estado.setMensagem("Você ainda não rolou o dado.");
            return;
        }
        if (estado.existeAlgumMovimento(valor)) {
            estado.setMensagem("Existe movimento possível. Jogue a peça.");
            return;
        }
        estado.passarVezSemMovimentos(valor);
        estado.setUltimoDado(0);
    }

    public void clicarCasa(int indiceCasa) {
        if (estado.alguemVenceu()) return;
        int valorDado = estado.getUltimoDado();
        if (valorDado == 0) {
            estado.setMensagem("Role o dado antes de mover.");
            return;
        }
        ResultadoJogada r = estado.tentarMover(indiceCasa, valorDado);
        if (!r.sucesso()) return;
        estado.setUltimoDado(0);
        if (r.vitoria()) return;
        if (r.jogaDeNovo()) {
            estado.setMensagem(estado.nomeDoJogador(estado.getJogadorAtualId())
                    + " ganhou o direito de jogar novamente por ter tirado " + valorDado + ". Role o dado!");
        } else {
            estado.trocarTurno();
        }
    }
}
