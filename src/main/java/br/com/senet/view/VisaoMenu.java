package br.com.senet.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class VisaoMenu {
    private final BorderPane root = new BorderPane();
    private final Button btnJogar = new Button("Jogar");
    private final Button btnAjuda = new Button("Ajuda");
    private final Button btnSair  = new Button("Sair");
    private final Button btnVerVitoria = new Button("Ver Vitória");

    private Runnable onJogar;
    private Runnable onAjuda;
    private Runnable onVerVitoria;
    private Runnable onSair;

    public VisaoMenu() {
        Label titulo = new Label("☥  S E N E T  ☥");
        titulo.getStyleClass().add("title-egypt");

        StackPane header = new StackPane(titulo, btnVerVitoria);
        StackPane.setAlignment(titulo, Pos.TOP_CENTER);
        StackPane.setAlignment(btnVerVitoria, Pos.TOP_RIGHT);
        header.setPadding(new Insets(6, 0, 0, 0));

        btnJogar.getStyleClass().add("btn-egypt");
        btnAjuda.getStyleClass().add("btn-egypt");
        btnSair.getStyleClass().add("btn-egypt");
        btnVerVitoria.getStyleClass().add("btn-mini");

        double w = 440;
        btnJogar.setPrefWidth(w);
        btnAjuda.setPrefWidth(w);
        btnSair.setPrefWidth(w);
        btnJogar.setMaxWidth(Region.USE_PREF_SIZE);
        btnAjuda.setMaxWidth(Region.USE_PREF_SIZE);
        btnSair.setMaxWidth(Region.USE_PREF_SIZE);

        VBox card = new VBox(14, btnJogar, btnAjuda, btnSair);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(18));
        card.getStyleClass().add("menu-card");
        card.setMaxWidth(500);

        VBox topo = new VBox(6, header, card);
        topo.setAlignment(Pos.TOP_CENTER);
        topo.setPadding(new Insets(6, 0, 0, 0));

        root.setTop(topo);
        root.setCenter(null);
        root.setPadding(new Insets(4, 24, 12, 24));
        root.getStyleClass().add("menu-root");

        btnJogar.setOnAction(e -> { if (onJogar != null) onJogar.run(); });
        btnAjuda.setOnAction(e -> { if (onAjuda != null) onAjuda.run(); });
        btnVerVitoria.setOnAction(e -> { if (onVerVitoria != null) onVerVitoria.run(); });
        btnSair.setOnAction(e -> { if (onSair != null) onSair.run(); });

        String css = getClass().getResource("/styles/menu.css") != null
                ? getClass().getResource("/styles/menu.css").toExternalForm()
                : null;
        if (css != null) root.getStylesheets().add(css);
    }

    public BorderPane getRoot() { return root; }

    public void onJogar(Runnable r) { this.onJogar = r; }
    public void onAjuda(Runnable r) { this.onAjuda = r; }
    public void onVerVitoria(Runnable r) { this.onVerVitoria = r; }
    public void onSair(Runnable r) { this.onSair = r; }
}
