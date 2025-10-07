package br.com.senet.controller;

public class ControladorMenu {
    private final Runnable acaoJogar;
    private final Runnable acaoVerVitoria;
    private final Runnable acaoAjuda;
    private final Runnable acaoSair;

    public ControladorMenu(Runnable acaoJogar,
                           Runnable acaoVerVitoria,
                           Runnable acaoAjuda,
                           Runnable acaoSair) {
        this.acaoJogar = acaoJogar;
        this.acaoVerVitoria = acaoVerVitoria;
        this.acaoAjuda = acaoAjuda;
        this.acaoSair = acaoSair;
    }

    public void jogar() {
        if (acaoJogar != null) acaoJogar.run();
    }

    public void verVitoria() {
        if (acaoVerVitoria != null) acaoVerVitoria.run();
    }

    public void ajuda() {
        if (acaoAjuda != null) acaoAjuda.run();
    }

    public void sair() {
        if (acaoSair != null) acaoSair.run();
    }
}
