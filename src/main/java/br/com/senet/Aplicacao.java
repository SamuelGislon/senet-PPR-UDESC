package br.com.senet;

import javafx.application.Application;
import javafx.application.Platform;
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

        ControladorMenu controladorMenu = new ControladorMenu(
                this::iniciarJogo,
                () -> abrirTelaVitoria("Jogador A"),
                () -> getHostServices().showDocument("https://www.google.com"),
                Platform::exit
        );

        visaoMenu.onJogar(controladorMenu::jogar);
        visaoMenu.onVerVitoria(controladorMenu::verVitoria);
        visaoMenu.onAjuda(controladorMenu::ajuda);
        visaoMenu.onSair(controladorMenu::sair);

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
        ControladorJogo controladorJogo = new ControladorJogo(estado);

        estado.anexar(visaoJogo);
        estado.notificarMudanca();

        visaoJogo.onRolarDado(controladorJogo::rolarDado);
        visaoJogo.onPassar(controladorJogo::passarVez);
        visaoJogo.onClickCasa(controladorJogo::clicarCasa);

        visaoJogo.onVoltarMenu(this::voltarMenu);
        visaoJogo.onAjuda(() -> getHostServices().showDocument("https://www.google.com"));
        visaoJogo.onVitoria(this::abrirTelaVitoria);

        cena.setRoot(visaoJogo.getRoot());
        stage.setWidth(900);
        stage.setHeight(520);
    }

    private void abrirTelaVitoria(String vencedor) {
        VisaoVitoria vv = new VisaoVitoria();
        vv.setVencedor(vencedor);
        vv.onJogarNovamente(this::iniciarJogo);
        vv.onVoltarMenu(this::voltarMenu);
        cena.setRoot(vv.getRoot());
        stage.setWidth(900);
        stage.setHeight(360);
    }

    private void voltarMenu() {
        cena.setRoot(visaoMenu.getRoot());
        stage.setWidth(900);
        stage.setHeight(360);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
