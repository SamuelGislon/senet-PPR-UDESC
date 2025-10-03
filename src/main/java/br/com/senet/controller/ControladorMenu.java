package br.com.senet.controller;

import javafx.application.HostServices;
import javafx.application.Platform;
import br.com.senet.view.VisaoMenu;

public class ControladorMenu {
    private final VisaoMenu visao;
    private final HostServices hostServices; // para abrir o link das regras do jogo que estão no Git
    private final Runnable acaoJogar;
    private final Runnable acaoVerVitoria;

    public ControladorMenu(VisaoMenu visao, HostServices hostServices, Runnable acaoJogar, Runnable acaoVerVitoria) {
        this.visao = visao;
        this.hostServices = hostServices;
        this.acaoJogar = acaoJogar;
        this.acaoVerVitoria = acaoVerVitoria; // NOVO

        this.visao.onJogar(this::jogar);
        this.visao.onAjuda(this::ajuda);
        this.visao.onVerVitoria(this::verVitoria); // NOVO
        this.visao.onSair(this::sair);
    }

    private void jogar() {
        if (acaoJogar != null)
            acaoJogar.run();
    }

    private void verVitoria() {
        if (acaoVerVitoria != null)
            acaoVerVitoria.run();
    }

    private void ajuda() {
        hostServices.showDocument("https://www.google.com");
    }

    private void sair() {
        Platform.exit();
    }
}
