package br.com.senet.model;

import java.util.Arrays;

public class Tabuleiro {
    private final int tamanho;
    private final int pecasPorJogador;
    // Posições da casa:
    // -1 vazio
    // 0 Jogador A
    // 1 peça do Jogador B
    private final int[] casas;

    public Tabuleiro(int tamanho, int pecasPorJogador) {
        this.tamanho = tamanho;
        this.pecasPorJogador = pecasPorJogador;
        this.casas = new int[tamanho];
        // Setar valor para todas as posições como -1, só para não precisar usar o for e ficar extenso
        Arrays.fill(casas, -1);
    }

    public int getTamanho() { return tamanho; }
    public int[] getCasas() { return casas; }
    public int getPecasPorJogador() { return pecasPorJogador; }

    public boolean posicaoLivre(int idx) {
        return idx >= 0 &&
               idx < tamanho &&
               casas[idx] == -1;
    }

    public int donoNaPosicao(int idx) {
        if (idx < 0 || idx >= tamanho)
            return -2; // inválido

        return casas[idx];
    }

    public void colocarPeca(int idx, int dono) {
        casas[idx] = dono;
    }

    public void limparPosicao(int idx) {
        casas[idx] = -1;
    }
}
