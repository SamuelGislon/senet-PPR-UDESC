package br.com.senet.model;

import java.util.ArrayList;
import java.util.List;

public class EstadoJogo {

    // ==== Observer ====
    private final List<ObservadorEstado> observadores = new ArrayList<>();
    public void anexar(ObservadorEstado o) { if (o != null && !observadores.contains(o)) observadores.add(o); }
    public void desanexar(ObservadorEstado o) { observadores.remove(o); }
    private void notificar() { for (ObservadorEstado o : observadores) o.atualizar(this); }
    public void notificarMudanca() { notificar(); }

    // ==== Estado básico ====
    private final Tabuleiro tabuleiro;
    private final Jogador jogadorA;
    private final Jogador jogadorB;
    private final Dado dado;
    private int jogadorAtual; // 0=A, 1=B
    private String mensagem = "Bem-vindo ao SENET!";

    // <<< Dado agora pertence ao Model
    private int ultimoDado = 0;
    public int  getUltimoDado() { return ultimoDado; }
    public void setUltimoDado(int v) { this.ultimoDado = v; notificar(); }

    public EstadoJogo(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
        this.jogadorA = new Jogador(0, tabuleiro.getPecasPorJogador());
        this.jogadorB = new Jogador(1, tabuleiro.getPecasPorJogador());
        this.dado = new Dado();
        this.jogadorAtual = 0;

        posicionarInicial();
        this.mensagem = "Peças posicionadas o " + nomeDoJogador(this.jogadorA.getId()) + " começa.";
        notificar();
    }

    private void posicionarInicial() {
        for (int i = 0; i < tabuleiro.getTamanho(); i++) tabuleiro.limparPosicao(i);
        for (int i = 0; i < 10; i++) {
            int dono = (i % 2 == 0) ? 0 : 1;
            Jogador j = (dono == 0) ? jogadorA : jogadorB;

            Peca livre = null;
            for (Peca p : j.getPecas()) { if (p.getPosicao() == -1) { livre = p; break; } }

            if (livre != null) {
                livre.moverPara(i);
                tabuleiro.colocarPeca(i, dono);
            }
        }
    }

    // ==== API pública para Controller / View ====
    public Tabuleiro getTabuleiro() { return tabuleiro; }
    public Jogador getJogadorAtual() { return jogadorAtual == 0 ? jogadorA : jogadorB; }
    public Jogador getOutroJogador() { return jogadorAtual == 0 ? jogadorB : jogadorA; }
    public int getJogadorAtualId() { return jogadorAtual; }
    public String getMensagem() { return mensagem; }
    public Jogador getJogador(int id) { return id == 0 ? jogadorA : jogadorB; }

    public void trocarTurno() { jogadorAtual = 1 - jogadorAtual; notificar(); }

    public void setMensagem(String msg) { mensagem = msg; notificar(); }

    public int rolar() { return dado.rolarD5(); }

    public boolean alguemVenceu() {
        return jogadorA.venceu(tabuleiro.getTamanho()) || jogadorB.venceu(tabuleiro.getTamanho());
    }

    public String nomeDoJogador(int id) { return id == 0 ? "Jogador A" : "Jogador B"; }

    public boolean existeAlgumMovimento(int valorDado) {
        int dono = getJogadorAtualId();
        int[] casas = tabuleiro.getCasas();
        int n = tabuleiro.getTamanho();

        for (int origem = 0; origem < n; origem++) {
            if (casas[origem] != dono) continue;
            int destino = origem + valorDado;
            if (movimentoValidoSemEfeito(origem, destino, dono, valorDado)) return true;
        }
        return false;
    }

    public void passarVezSemMovimentos(int valorDado) {
        setMensagem("Não existem movimentos válidos para o valor " + valorDado + ", perdeu a vez.");
        trocarTurno();
    }

    /**
     * Tenta mover a peça que está em `indiceCasa` (deve pertencer ao jogador da vez).
     * Aplica regras e notifica. Retorna ResultadoJogada: sucesso, jogaDeNovo(1/5), vitória.
     */
    public ResultadoJogada tentarMover(int indiceCasa, int valorDado) {
        if (alguemVenceu()) return ResultadoJogada.invalida();

        // pega a peça do jogador atual na casa clicada
        Peca peca = null;
        for (Peca p : getJogadorAtual().getPecas()) {
            if (p.getPosicao() == indiceCasa) { peca = p; break; }
        }
        if (peca == null) {
            setMensagem("Selecione uma peça sua para mover.");
            return ResultadoJogada.invalida();
        }

        int origem = peca.getPosicao();
        int destino = origem + valorDado;

        // 1) Validações
        Integer exigido = valorExatoObrigatorio(origem);
        if (exigido != null && valorDado != exigido) {
            setMensagem("Movimento inválido: a casa " + (origem + 1) + " só se move com valor exato " + exigido + ".");
            return ResultadoJogada.invalida();
        }

        if (existeBloqueioNoCaminho(origem, destino, getJogadorAtualId())) {
            setMensagem("Movimento inválido: há um bloqueio de 3 peças do adversário no caminho.");
            return ResultadoJogada.invalida();
        }

        // 2) Saída do tabuleiro (respeitando exatidão onde necessário)
        if (destino >= tabuleiro.getTamanho()) {
            Integer exato = valorExatoParaSair(origem);
            if (exato != null && valorDado != exato) {
                setMensagem("Movimento inválido: desta casa só sai com valor exato " + exato + ".");
                return ResultadoJogada.invalida();
            }
            tabuleiro.limparPosicao(origem);
            peca.moverPara(tabuleiro.getTamanho());
            setMensagem("Peça saiu do tabuleiro!");
            notificar();

            boolean vitoria = alguemVenceu();
            boolean jogaDeNovo = (valorDado == 1 || valorDado == 5);
            return new ResultadoJogada(true, jogaDeNovo, vitoria);
        }

        // 3) Dentro do tabuleiro
        int ocupante = tabuleiro.donoNaPosicao(destino);
        if (ocupante == peca.getDono()) {
            setMensagem("Movimento inválido: casa ocupada por sua peça.");
            return ResultadoJogada.invalida();
        }

        if (ocupante != -1 && pecaEstaProtegida(destino)) {
            setMensagem("Captura inválida: peça protegida.");
            return ResultadoJogada.invalida();
        }

        if (ocupante == -1) {
            // mover
            tabuleiro.limparPosicao(origem);
            tabuleiro.colocarPeca(destino, peca.getDono());
            peca.moverPara(destino);

            aplicarEfeitoPosMovimento_Agua(peca);

            setMensagem("Peça movida para " + (peca.getPosicao() + 1) + ".");
            notificar();
        } else {
            // captura (troca de lugar)
            Jogador adv = getOutroJogador();
            Peca pecaAdv = null;
            for (Peca px : adv.getPecas()) {
                if (px.getPosicao() == destino) { pecaAdv = px; break; }
            }
            // swap
            tabuleiro.limparPosicao(origem);
            tabuleiro.colocarPeca(destino, peca.getDono());
            peca.moverPara(destino);

            tabuleiro.colocarPeca(origem, pecaAdv.getDono());
            pecaAdv.moverPara(origem);

            aplicarEfeitoPosMovimento_Agua(peca);

            setMensagem("Peça do adversário capturada (trocou de lugar).");
            notificar();
        }

        boolean vitoria = alguemVenceu();
        boolean jogaDeNovo = (valorDado == 1 || valorDado == 5);
        return new ResultadoJogada(true, jogaDeNovo, vitoria);
    }

    // ==== Regras internas (privadas ao Model) ====

    private boolean movimentoValidoSemEfeito(int origem, int destino, int dono, int valor) {
        int tam = tabuleiro.getTamanho();

        Integer exigido = valorExatoObrigatorio(origem);
        if (exigido != null && valor != exigido) return false;

        if (existeBloqueioNoCaminho(origem, destino, dono)) return false;

        if (destino >= tam) {
            Integer exato = valorExatoParaSair(origem);
            return exato == null || exato == valor;
        }

        if (destino < 0) return false;

        int ocupante = tabuleiro.donoNaPosicao(destino);
        if (ocupante == dono) return false;

        if (ocupante != -1 && pecaEstaProtegida(destino)) return false;

        return true;
    }

    // Casas com exigência de valor exato para QUALQUER movimento a partir delas
    private Integer valorExatoObrigatorio(int origemIdx) {
        switch (origemIdx) {
            case 25: return 5; // casa 26
            case 27: return 3; // casa 28
            case 28: return 2; // casa 29
            case 29: return 1; // casa 30
            default: return null;
        }
    }

    private Integer valorExatoParaSair(int origemIdx) {
        switch (origemIdx) {
            case 25: return 5;
            case 27: return 3;
            case 28: return 2;
            case 29: return 1;
            default: return null;
        }
    }

    // casas 26 e 29 são protegidas (index 25 e 28)
    private boolean casaProtegidaPorRegra(int idx) { return idx == 25 || idx == 28; }

    private boolean pecaEstaProtegida(int idx) {
        if (casaProtegidaPorRegra(idx)) return true;

        int dono = tabuleiro.donoNaPosicao(idx);
        if (dono < 0) return false;

        int[] casas = tabuleiro.getCasas();
        int n = tabuleiro.getTamanho();

        boolean vizinhoEsq = (idx - 1 >= 0) && casas[idx - 1] == dono;
        boolean vizinhoDir = (idx + 1 < n) && casas[idx + 1] == dono;

        return vizinhoEsq || vizinhoDir;
    }

    // bloqueio de 3+ peças do adversário entre origem e destino
    private boolean existeBloqueioNoCaminho(int posOrigem, int posDestino, int donoQueMove) {
        int start = posOrigem + 1; // começa na casa seguinte à origem
        int end   = posDestino;

        if (start >= end) return false;

        int[] casas = tabuleiro.getCasas();
        int n = tabuleiro.getTamanho();
        int adversario = 1 - donoQueMove;

        for (int i = 0; i + 2 < n; i++) {
            if (casas[i] == adversario && casas[i + 1] == adversario && casas[i + 2] == adversario) {
                boolean comecaAntes = (posOrigem < i);
                boolean terminaDepois = (posDestino > i + 2);
                boolean blocoEntre = (start <= i) && (end > i + 2);
                if (comecaAntes && terminaDepois && blocoEntre) return true;
            }
        }
        return false;
    }

    // Casa da Água (27 → idx 26) volta para a 15 (idx 14) ou primeira livre antes dela
    private void aplicarEfeitoPosMovimento_Agua(Peca p) {
        if (p.getPosicao() == 26) {
            int destino = 14;
            int alvo = destino;
            while (alvo >= 0 && !tabuleiro.posicaoLivre(alvo)) alvo--;
            tabuleiro.limparPosicao(26);
            tabuleiro.colocarPeca(alvo, p.getDono());
            p.moverPara(alvo);
            setMensagem("Caiu na Casa da Água: peça retorna para " + (alvo + 1) + ".");
        }
    }
}
