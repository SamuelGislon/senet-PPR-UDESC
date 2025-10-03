package br.com.senet.model;

import java.util.ArrayList;
import java.util.List;

public class EstadoJogo {

    private final List<ObservadorEstado> observadores = new ArrayList<>();
    public void anexar(ObservadorEstado o) {
        if (o != null && !observadores.contains(o)) observadores.add(o);
    }

    public void desanexar(ObservadorEstado o) {
        observadores.remove(o); // Não vimos necessidade de usar no projeto
    }

    public void notificar() {
        for (ObservadorEstado o : observadores) {
            o.atualizar(this);
        }
    }

    private final Tabuleiro tabuleiro;
    private final Jogador jogadorA;
    private final Jogador jogadorB;
    private final Dado dado;
    private int jogadorAtual; // 0 = A (branco), 1 = B (preto)
    private String mensagem = "Bem-vindo ao SENET!";

    public EstadoJogo(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
        this.jogadorA = new Jogador(0, tabuleiro.getPecasPorJogador());
        this.jogadorB = new Jogador(1, tabuleiro.getPecasPorJogador());
        this.dado = new Dado();
        this.jogadorAtual = 0;

        posicionarInicial();
        this.mensagem = "Peças posicionadas o " + nomeDoJogador(this.jogadorA.getId()) + " começa.";

        // notificar a tela com o estado inicial do jogo
        notificar();
    }

    private void posicionarInicial() {
        // Limpar todo o tabuleiro, caso
        for (int i = 0; i < tabuleiro.getTamanho(); i++) {
            tabuleiro.limparPosicao(i);
        }

        // Posiciona as peças alternadamente, começando com a brancas e depois a preta
        // continua assim até a casa 10 (index 9)
        for (int iIdx = 0; iIdx < 10; iIdx++) {
            // Verifica se a posição é impar ou par
            // Impar, deve posicionar a branca
            // Par, posicionar a preta
            int dono = (iIdx % 2 == 0) ? 0 : 1;

            Jogador j = (dono == 0) ? jogadorA : jogadorB;

            Peca pecaPosicionada = null;
            // Buscar a peça do jogador que ainda não foi posicionada
            for (Peca px : j.getPecas()) {
                if (px.getPosicao() == -1) {
                    pecaPosicionada = px;
                    break;
                }
            }

            // Depois de mover a peça, coloca a peça para o jogador
            if (pecaPosicionada != null) {
                pecaPosicionada.moverPara(iIdx);
                tabuleiro.colocarPeca(iIdx, dono);
            }
        }
    }

    public Tabuleiro getTabuleiro() { return tabuleiro; }
    public Jogador getJogadorAtual() { return jogadorAtual == 0 ? jogadorA : jogadorB; }
    public Jogador getOutroJogador() { return jogadorAtual == 0 ? jogadorB : jogadorA; }
    public int getJogadorAtualId() { return jogadorAtual; }
    public String getMensagem() { return mensagem; }
    public Jogador getJogador(int id) { return id == 0 ? jogadorA : jogadorB; }

    public void trocarTurno() {
        // Se for a vez do jogador 1, então o adversário é 0
        // Logo 1 - 1(jogadorAtual) = 0
        // Se for o jogador 0 então 1 - 0 = 1
        jogadorAtual = 1 - jogadorAtual;
        notificar();
    }

    public void setMensagem(String msg) {
        mensagem = msg;
        notificar();
    }

    public int rolar() {
        return dado.rolarD5();
    }

    public boolean alguemVenceu() {
        return jogadorA.venceu(tabuleiro.getTamanho()) || jogadorB.venceu(tabuleiro.getTamanho());
    }

    public String nomeDoJogador(int id) { return id == 0 ? "Jogador A" : "Jogador B"; }

    // Para notificar todas as mudanças do tabuleiro de uma vez só
    public void notificarMudanca() {
        notificar();
    }
}
