package br.com.senet;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import br.com.senet.controller.ControladorJogo;
import br.com.senet.controller.ControladorMenu;
import br.com.senet.model.EstadoJogo;
import br.com.senet.model.Tabuleiro;
import br.com.senet.view.VisaoJogo;
import br.com.senet.view.VisaoMenu;
import br.com.senet.view.VisaoVitoria;

public class Aplicacao extends Application {

    private Stage stage;
    private Scene cena;

    private VisaoMenu visaoMenu;

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        visaoMenu = new VisaoMenu();
        cena = new Scene(visaoMenu.getRoot(), 900, 360);

        new ControladorMenu(
                visaoMenu,
                getHostServices(),
                this::iniciarJogo,
                () -> abrirTelaVitoria("Jogador A") // atalho para testar a tela de vitória e não precisar jogar tudo
        );

        stage.setTitle("SENET - MVC (JavaFX)");
        stage.setScene(cena);
        stage.setResizable(false);
        stage.setWidth(900);
        stage.setHeight(360);
        stage.show();
    }

    private void iniciarJogo() {
        Tabuleiro tabuleiro = new Tabuleiro(30, 5);
        EstadoJogo estado   = new EstadoJogo(tabuleiro);
        VisaoJogo visaoJogo = new VisaoJogo();
        ControladorJogo controladorJogo = new ControladorJogo(estado, visaoJogo);

        estado.anexar(visaoJogo);
        estado.notificarMudanca();

        visaoJogo.onVoltarMenu(this::voltarMenu);
        visaoJogo.onAjuda(() -> getHostServices().showDocument("https://www.google.com"));

        visaoJogo.onVitoria(this::abrirTelaVitoria);

        cena.setRoot(visaoJogo.getRoot());
    }

    private void abrirTelaVitoria(String vencedor) {
        VisaoVitoria vv = new VisaoVitoria();
        vv.setVencedor(vencedor);
        vv.onJogarNovamente(this::iniciarJogo);
        vv.onVoltarMenu(this::voltarMenu);
        cena.setRoot(vv.getRoot());
    }

    private void voltarMenu() {
        cena.setRoot(visaoMenu.getRoot());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
