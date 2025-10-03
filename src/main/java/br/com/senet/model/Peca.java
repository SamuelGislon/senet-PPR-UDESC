package br.com.senet.model;

public class Peca {
    private int posicao; // -1 = ainda não foi posicionada
                         // >= tamanho do tabuleiro que dizer que já saiu
    private final int dono; // 0 (Jogador A) ou 1 (Jogador B)

    public Peca(int dono) {
        this.dono = dono;
        this.posicao = -1; // Obviamente sempre cria como uma peça ainda não posicionada
    }

    public int getPosicao() {
        return posicao;
    }

    public int getDono() {
        return dono;
    }

    public boolean saiuDoTabuleiro(int tamanhoTabuleiro) {
        return posicao >= tamanhoTabuleiro;
    }

    public void moverPara(int novaPosicao) {
        this.posicao = novaPosicao;
    }
}
