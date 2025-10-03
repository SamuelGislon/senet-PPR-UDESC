package br.com.senet.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class VisaoVitoria {

    private final BorderPane root = new BorderPane();

    private final Label lblTitulo    = new Label("☥  V I T Ó R I A  ☥");
    private final Label lblVencedor  = new Label();
    private final Button btnJogarNovamente = new Button("Jogar novamente");
    private final Button btnVoltarMenu     = new Button("Voltar ao Menu");

    private Runnable onJogarNovamente;
    private Runnable onVoltarMenu;

    public VisaoVitoria() {
        root.getStyleClass().add("menu-root");
        lblTitulo.getStyleClass().add("title-egypt");

        btnJogarNovamente.getStyleClass().add("btn-egypt");
        btnVoltarMenu.getStyleClass().add("btn-egypt");

        btnJogarNovamente.setPrefWidth(360);
        btnVoltarMenu.setPrefWidth(360);
        btnJogarNovamente.setMaxWidth(Region.USE_PREF_SIZE);
        btnVoltarMenu.setMaxWidth(Region.USE_PREF_SIZE);

        lblVencedor.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-text-fill: #3b2e1a;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 8, 0.2, 0, 2);" +
                        "-fx-font-family: 'Cinzel Decorative', 'Times New Roman', serif;" +
                        "-fx-letter-spacing: 0.5px;"
        );

        VBox card = new VBox(14, lblVencedor, btnJogarNovamente, btnVoltarMenu);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(18));
        card.getStyleClass().add("menu-card");
        card.setMaxWidth(420);

        VBox topo = new VBox(8, lblTitulo, card);
        topo.setAlignment(Pos.TOP_CENTER);
        topo.setPadding(new Insets(6, 0, 0, 0));

        root.setTop(topo);
        root.setCenter(null);
        root.setPadding(new Insets(4, 24, 12, 24));

        btnJogarNovamente.setOnAction(e -> { if (onJogarNovamente != null) onJogarNovamente.run(); });
        btnVoltarMenu.setOnAction(e -> { if (onVoltarMenu != null) onVoltarMenu.run(); });

        String css = getClass().getResource("/styles/menu.css") != null
                ? getClass().getResource("/styles/menu.css").toExternalForm()
                : null;
        if (css != null) root.getStylesheets().add(css);
    }

    public BorderPane getRoot() { return root; }

    public void setVencedor(String nome) {
        lblVencedor.setText("Parabéns, " + nome + "!");
    }

    public void onJogarNovamente(Runnable r) { this.onJogarNovamente = r; }
    public void onVoltarMenu(Runnable r)     { this.onVoltarMenu = r; }
}
