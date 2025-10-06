package br.com.senet.model;

public record ResultadoJogada(boolean sucesso, boolean jogaDeNovo, boolean vitoria) {
    public static ResultadoJogada invalida() { return new ResultadoJogada(false, false, false); }
}
