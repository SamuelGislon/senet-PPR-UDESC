package br.com.senet.controller;

import br.com.senet.model.EstadoJogo;
import br.com.senet.model.ResultadoJogada;
import br.com.senet.view.VisaoJogo;

public class ControladorJogo {

    private final EstadoJogo estado;
    private final VisaoJogo visao;

    public ControladorJogo(EstadoJogo estado, VisaoJogo visao) {
        this.estado = estado;
        this.visao = visao;

        // Eventos da View -> Controller
        this.visao.onRolarDado(this::acaoRolarDado);
        this.visao.onClickCasa(this::acaoClickCasa);
        this.visao.onPassar(this::acaoPassarVez);

        // A View observa o Estado (se ainda não foi feito externamente)
        estado.anexar(visao);
    }

    private void acaoRolarDado() {
        if (estado.alguemVenceu()) return;

        if (estado.getUltimoDado() != 0) {
            estado.setMensagem("Você já rolou o dado. Faça um movimento.");
            return;
        }

        int valor = estado.rolar();
        estado.setUltimoDado(valor); // <<< dado agora mora no Model

        boolean temMov = estado.existeAlgumMovimento(valor);
        visao.setPassarVezHabilitado(!temMov);

        if (!temMov) {
            estado.setMensagem("Não existem movimentos válidos para o valor " + valor + ". Você pode 'Passar Vez'.");
            return;
        }

        estado.setMensagem(estado.nomeDoJogador(estado.getJogadorAtualId())
                + " rolou " + valor + ". Selecione uma peça.");
    }

    private void acaoPassarVez() {
        int valor = estado.getUltimoDado();
        if (valor == 0) {
            estado.setMensagem("Você ainda não rolou o dado.");
            visao.setPassarVezHabilitado(false);
            return;
        }

        // Segurança: se houver movimento, não permite passar
        if (estado.existeAlgumMovimento(valor)) {
            estado.setMensagem("Existe movimento possível. Jogue a peça.");
            visao.setPassarVezHabilitado(false);
            return;
        }

        // Model decide mensagem e troca de turno
        estado.passarVezSemMovimentos(valor);
        estado.setUltimoDado(0);               // <<< zera no Model
        visao.setPassarVezHabilitado(false);
    }

    private void acaoClickCasa(int indiceCasa) {
        if (estado.alguemVenceu()) return;

        int valorDado = estado.getUltimoDado();
        if (valorDado == 0) {
            estado.setMensagem("Role o dado antes de mover.");
            return;
        }

        ResultadoJogada r = estado.tentarMover(indiceCasa, valorDado);
        if (!r.sucesso()) return; // mensagem já setada no model

        // Jogada realizada
        estado.setUltimoDado(0);               // <<< zera no Model
        visao.setPassarVezHabilitado(false);

        if (r.vitoria()) {
            visao.dispararVitoria(estado.nomeDoJogador(estado.getJogadorAtualId()));
            return;
        }

        if (r.jogaDeNovo()) {
            estado.setMensagem(estado.nomeDoJogador(estado.getJogadorAtualId())
                    + " ganhou o direito de jogar novamente por ter tirado " + valorDado + ". Role o dado!");
        } else {
            estado.trocarTurno();
        }
    }
}
