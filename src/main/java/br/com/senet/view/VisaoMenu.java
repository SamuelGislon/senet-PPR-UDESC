package br.com.senet.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class VisaoMenu {
    private final BorderPane root = new BorderPane();
    private final Button btnJogar = new Button("Jogar");
    private final Button btnAjuda = new Button("Ajuda");
    private final Button btnSair  = new Button("Sair");

    // Atalho para visualizar a tela de vitória e não precisar jogar toda a partida novamente
    private final Button btnVerVitoria = new Button("Ver Vitória");

    private Runnable onJogar;
    private Runnable onAjuda;
    private Runnable onVerVitoria;
    private Runnable onSair;

    public VisaoMenu() {
        Label titulo = new Label("☥  S E N E T  ☥");
        titulo.getStyleClass().add("title-egypt");

        btnVerVitoria.getStyleClass().add("btn-mini-atalho");

        StackPane header = new StackPane(titulo, btnVerVitoria);
        StackPane.setAlignment(titulo, Pos.TOP_CENTER);
        StackPane.setAlignment(btnVerVitoria, Pos.TOP_RIGHT);
        header.setPadding(new Insets(6, 0, 0, 0));

        btnJogar.getStyleClass().add("btn-egypt");
        btnAjuda.getStyleClass().add("btn-egypt");
        btnSair.getStyleClass().add("btn-egypt");

        double larguraBotoes = 440;
        btnJogar.setPrefWidth(larguraBotoes);
        btnAjuda.setPrefWidth(larguraBotoes);
        btnSair.setPrefWidth(larguraBotoes);
        btnJogar.setMaxWidth(Region.USE_PREF_SIZE);
        btnAjuda.setMaxWidth(Region.USE_PREF_SIZE);
        btnSair.setMaxWidth(Region.USE_PREF_SIZE);

        VBox painelBotoes = new VBox(14, btnJogar, btnAjuda, btnSair);
        painelBotoes.setAlignment(Pos.CENTER);
        painelBotoes.setPadding(new Insets(18));
        painelBotoes.getStyleClass().add("menu-card");
        painelBotoes.setMaxWidth(500);

        VBox painelPrincipal = new VBox(6, header, painelBotoes);
        painelPrincipal.setAlignment(Pos.TOP_CENTER);
        painelPrincipal.setPadding(new Insets(6, 0, 0, 0));

        // Root
        root.setTop(painelPrincipal);
        root.setCenter(null);
        root.setPadding(new Insets(4, 24, 12, 24));
        root.getStyleClass().add("menu-root");

        btnJogar.setOnAction(e -> { if (onJogar != null) onJogar.run(); });
        btnAjuda.setOnAction(e -> { if (onAjuda != null) onAjuda.run(); });
        btnVerVitoria.setOnAction(e -> { if (onVerVitoria != null) onVerVitoria.run(); });
        btnSair.setOnAction(e  -> { if (onSair  != null) onSair.run(); });

        String css = getClass().getResource("/styles/menu.css") != null
                     ? getClass().getResource("/styles/menu.css").toExternalForm()
                     : null;
        if (css != null)
            root.getStylesheets().add(css);
    }

    public BorderPane getRoot() { return root; }

    public void onJogar(Runnable run) { this.onJogar = run; }
    public void onAjuda(Runnable run) { this.onAjuda = run; }
    public void onVerVitoria(Runnable run) { this.onVerVitoria = run; }
    public void onSair (Runnable run) { this.onSair  = run; }
}
