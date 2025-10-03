package br.com.senet.model;

import java.util.ArrayList;
import java.util.List;

public class Jogador {
    private final int id; // 0 (Jogador A) ou 1 (Jogador B)
    private final List<Peca> pecas = new ArrayList<>();

    public Jogador(int id, int qtdPecas) {
        this.id = id;
        for (int i = 0; i < qtdPecas; i++) {
            pecas.add(new Peca(id));
        }
    }

    public int getId() { return id; }
    public List<Peca> getPecas() { return pecas; }

    public boolean venceu(int tamanhoTabuleiro) {
        for (Peca p : pecas) {
            // Valida se a posição da peça é = ao tamanho do tabuleiro ou maior,
            // se for quer dizer que a peça saiu do tabuleiro
            if (!p.saiuDoTabuleiro(tamanhoTabuleiro)) {
                return false;
            }
        }
        return true;
    }

}
