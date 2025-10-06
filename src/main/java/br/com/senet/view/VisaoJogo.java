package br.com.senet.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import br.com.senet.model.EstadoJogo;
import br.com.senet.model.Tabuleiro;
import br.com.senet.model.ObservadorEstado;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class VisaoJogo implements ObservadorEstado {

    private final BorderPane root = new BorderPane();
    private final GridPane grid = new GridPane();

    private final Label lblTopo = new Label("SENET");
    private final Label lblMensagem = new Label();
    private final Label lblDado = new Label("Dado: -");
    private final Button btnRolar = new Button("Rolar Dado");

    private final Button btnVoltar = new Button("Voltar ao Menu");
    private final Button btnAjuda  = new Button("Ajuda");

    private final Button btnPassar = new Button("Passar Vez");

    private Runnable onRolarDado;
    private Consumer<Integer> onClickCasa;
    private Runnable onVoltarMenu;
    private Runnable onAjuda;
    private Runnable onPassar;

    private java.util.function.Consumer<String> onVitoria;

    private final Map<Integer, Image> iconesPorIndice = new HashMap<>();
    private static final Object[][] ICON_DEFS = new Object[][]{
            {14, "/images/icon_casa15.png"},
            {25, "/images/icon_casa26.png"},
            {26, "/images/icon_casa27.png"},
            {27, "/images/icon_casa28.png"},
            {28, "/images/icon_casa29.png"},
            {29, "/images/icon_casa30.png"}
    };

    public void onVitoria(java.util.function.Consumer<String> c) { this.onVitoria = c; }
    public void dispararVitoria(String vencedor) { if (onVitoria != null) onVitoria.accept(vencedor); }

    public VisaoJogo() {
        root.setPadding(new Insets(12));

        HBox linhaTopo = new HBox(12, lblTopo, btnVoltar, btnAjuda);
        linhaTopo.setAlignment(Pos.CENTER_LEFT);

        VBox top = new VBox(6, linhaTopo, lblMensagem);
        top.setAlignment(Pos.CENTER_LEFT);
        root.setTop(top);

        grid.setHgap(4);
        grid.setVgap(4);
        root.setCenter(grid);

        btnPassar.setDisable(true);
        HBox bottom = new HBox(12, btnRolar, lblDado, btnPassar);
        bottom.setAlignment(Pos.CENTER_LEFT);
        bottom.setPadding(new Insets(8, 0, 0, 0));
        root.setBottom(bottom);

        btnRolar.setOnAction(e  -> { if (onRolarDado != null) onRolarDado.run(); });
        btnVoltar.setOnAction(e -> { if (onVoltarMenu != null) onVoltarMenu.run(); });
        btnAjuda.setOnAction(e  -> { if (onAjuda != null) onAjuda.run();  });
        btnPassar.setOnAction(e -> { if (onPassar != null) onPassar.run(); });

        root.getStyleClass().add("game-root");
        grid.getStyleClass().add("board");
        lblTopo.getStyleClass().add("game-title");
        lblMensagem.getStyleClass().add("game-status");
        lblDado.getStyleClass().add("game-status");

        btnRolar.getStyleClass().add("btn-egypt");
        btnAjuda.getStyleClass().add("btn-egypt-secondary");
        btnVoltar.getStyleClass().add("btn-egypt-secondary");
        btnPassar.getStyleClass().add("btn-egypt-outline");

        String cssJogo = getClass().getResource("/styles/jogo.css") != null
                ? getClass().getResource("/styles/jogo.css").toExternalForm()
                : null;
        if (cssJogo != null) root.getStylesheets().add(cssJogo);

        for (Object[] def : ICON_DEFS) {
            int idx = (Integer) def[0];
            String path = (String) def[1];
            var url = getClass().getResource(path);
            if (url != null) iconesPorIndice.put(idx, new Image(url.toExternalForm()));
        }
    }

    public Pane getRoot() { return root; }

    public void onRolarDado(Runnable action) { this.onRolarDado = action; }
    public void onClickCasa(Consumer<Integer> action) { this.onClickCasa = action; }
    public void onVoltarMenu(Runnable action) { this.onVoltarMenu = action; }
    public void onAjuda(Runnable action) { this.onAjuda = action; }
    public void onPassar(Runnable action) { this.onPassar = action; }

    public void setPassarVezHabilitado(boolean habilitar) { btnPassar.setDisable(!habilitar); }

    @Override
    public void atualizar(EstadoJogo estado) {
        Tabuleiro t = estado.getTabuleiro();
        grid.getChildren().clear();

        int cols = 10;
        int jogadorAtual = estado.getJogadorAtualId();

        boolean[] protegidas = calcularProtegidas(t);
        boolean[] bloqueios  = calcularBloqueios(t);

        for (int i = 0; i < t.getTamanho(); i++) {
            int r = i / cols;
            int c = (r % 2 == 0) ? i % cols : (cols - 1) - (i % cols);
            StackPane cell = criarCelula(i, t.donoNaPosicao(i), jogadorAtual, protegidas[i], bloqueios[i]);
            grid.add(cell, c, r);
        }

        lblMensagem.setText(estado.getMensagem() + "  |  Turno: " + estado.nomeDoJogador(estado.getJogadorAtualId()));
        int dado = estado.getUltimoDado(); // <<< lê do Model
        lblDado.setText("Dado: " + (dado == 0 ? "-" : dado));
    }

    private StackPane criarCelula(int indice, int dono, int jogadorAtual, boolean protegida, boolean bloqueio) {
        StackPane p = new StackPane();
        p.setPrefSize(80, 80);

        p.getStyleClass().add("cell");
        if (bloqueio) p.getStyleClass().add("cell-block");
        if (protegida) p.getStyleClass().add("cell-protected");
        if (dono == jogadorAtual && dono != -1) p.getStyleClass().add("cell-turn");

        Label lbl = new Label(String.valueOf(indice + 1));
        lbl.getStyleClass().add("cell-index");
        StackPane.setAlignment(lbl, Pos.TOP_LEFT);
        lbl.setPadding(new Insets(4));

        Node marcador;
        if (dono == -1) {
            marcador = new Label("");
        } else if (dono == 0) {
            Circle circ = new Circle(18.0);
            circ.setFill(Color.WHITE);
            circ.setStroke(Color.BLACK);
            circ.setStrokeWidth(2.0);
            marcador = circ;
        } else {
            Polygon tri = new Polygon(0.0, -18.0, -16.0, 14.0, 16.0, 14.0);
            tri.setFill(Color.BLACK);
            tri.setStroke(Color.WHITE);
            tri.setStrokeWidth(2.5);
            marcador = tri;
        }

        if (iconesPorIndice.containsKey(indice)) {
            ImageView iv = new ImageView(iconesPorIndice.get(indice));
            iv.setPreserveRatio(true);
            iv.setFitWidth(34);
            iv.setOpacity(0.70);
            iv.setMouseTransparent(true);
            StackPane.setAlignment(iv, Pos.CENTER);
            p.getChildren().addAll(lbl, iv, marcador);
        } else {
            p.getChildren().addAll(lbl, marcador);
        }

        p.setOnMouseClicked(me -> {
            if (me.getButton() == MouseButton.PRIMARY && onClickCasa != null) {
                onClickCasa.accept(indice);
            }
        });
        return p;
    }

    private boolean[] calcularProtegidas(Tabuleiro tab) {
        int n = tab.getTamanho();
        int[] casas = tab.getCasas();
        boolean[] res = new boolean[n];
        for (int i = 0; i < n; i++) {
            int dono = casas[i];
            if (dono < 0) continue;
            boolean esq = (i - 1 >= 0) && casas[i - 1] == dono;
            boolean dir = (i + 1 < n) && casas[i + 1] == dono;
            res[i] = esq || dir;
        }
        return res;
    }

    private boolean[] calcularBloqueios(Tabuleiro tab) {
        int n = tab.getTamanho();
        int[] casas = tab.getCasas();
        boolean[] res = new boolean[n];

        int i = 0;
        while (i < n) {
            if (casas[i] == -1) { i++; continue; }
            int dono = casas[i];
            int j = i + 1;
            while (j < n && casas[j] == dono) j++;
            int len = j - i;
            if (len >= 3) for (int k = i; k < j; k++) res[k] = true;
            i = j;
        }
        return res;
    }
}
