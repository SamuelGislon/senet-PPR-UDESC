package br.com.senet.controller;

import br.com.senet.model.*;
import br.com.senet.view.VisaoJogo;

public class ControladorJogo {

    private final EstadoJogo estado;
    private final VisaoJogo visao;

    public ControladorJogo(EstadoJogo estado, VisaoJogo visao) {
        this.estado = estado;
        this.visao = visao;

        this.visao.onRolarDado(this::acaoRolarDado);
        this.visao.onClickCasa(this::acaoClickCasa);
        this.visao.onPassar(this::acaoPassarVez); // NOVO
    }

    private void acaoRolarDado() {
        if (estado.alguemVenceu())
            return;

        if (visao.getValorDado() != 0) {
            estado.setMensagem("Você já rolou o dado. Faça um movimento.");
            return;
        }

        int valor = estado.rolar();
        visao.setValorDado(valor);

        // Habilita "Passar Vez" APENAS se não houver NENHUM movimento válido
        boolean temMov = haMovimentoPossivel(valor);
        visao.setPassarVezHabilitado(!temMov);

        if (!temMov) {
            estado.setMensagem("Não existem movimentos válidos para o valor " + valor + ". Você pode 'Passar Vez'.");
            return;
        }

        estado.setMensagem(estado.nomeDoJogador(estado.getJogadorAtualId()) +
                           " rolou " + valor + ". Selecione uma peça.");
    }

    private void acaoPassarVez() {
        // Caso habilite errado o botão, valida se o jogador não rolou o dado
        int valor = visao.getValorDado();
        if (valor == 0) {
            estado.setMensagem("Você ainda não rolou o dado.");
            visao.setPassarVezHabilitado(false);
            return;
        }

        // Por segurança valida novamente se não existe movimento possível
        if (haMovimentoPossivel(valor)) {
            estado.setMensagem("Existe movimento possível. Jogue a peça.");
            visao.setPassarVezHabilitado(false);
            return;
        }

        // Por fim, realmente passa a vez
        estado.setMensagem("Não existem movimentos válidos para o valor " + valor + ", perdeu a vez.");
        visao.zerarDado();
        visao.setPassarVezHabilitado(false);
        estado.trocarTurno();
    }

    private void acaoClickCasa(int indiceCasa) {
        if (estado.alguemVenceu()) return; // SAMUEL - REMOVER

        int valorDado = visao.getValorDado();
        if (valorDado == 0) {
            estado.setMensagem("Role o dado antes de mover.");
            return;
        }

        boolean sucesso;

        Jogador jogador = estado.getJogadorAtual();

        Peca pecaSelecionada = null;
        for (Peca p : jogador.getPecas()) {
            if (p.getPosicao() == indiceCasa) {
                pecaSelecionada = p;
                break; // achou a peça da casa selecionada
            }
        }

        if (pecaSelecionada == null) {
            estado.setMensagem("Selecione uma peça sua para mover.");
            sucesso = false;
        } else {
            int origem = pecaSelecionada.getPosicao();
            int destino = origem + valorDado;
            sucesso = moverOuCapturar(pecaSelecionada, origem, destino);
        }

        // Se a jogada foi feita com sucesso no metodo moverOuCapturar,
        // então prossegue, se não continua na vez do jogador
        if (sucesso) {
            visao.zerarDado();
            visao.setPassarVezHabilitado(false); // Desabilita o botão novamente por segurança

            if (!estado.alguemVenceu()) {
                if (valorDado == 1 || valorDado == 5) {
                    String nome = estado.nomeDoJogador(estado.getJogadorAtualId());
                    estado.setMensagem(nome + " ganhou o direito de jogar novamente por ter tirado " + valorDado + " no dado. Role o dado!");
                } else {
                    estado.trocarTurno();
                }
            } else {
                String vencedor = estado.nomeDoJogador(estado.getJogadorAtualId());
                visao.dispararVitoria(vencedor);
            }
        }
    }

    private boolean moverOuCapturar(Peca pecaOrigem, int posOrigem, int posDestino) {
        Tabuleiro tab = estado.getTabuleiro();
        int donoPeca = pecaOrigem.getDono();
        int tamTab = tab.getTamanho();

        int valorDado = (posDestino - posOrigem);

        Integer valorExigido = valorExatoObrigatorio(posOrigem);
        if (valorExigido != null && valorDado != valorExigido) {
            estado.setMensagem("Movimento inválido: a casa " + posOrigem + " só se move com valor exato " + valorExigido + ".");
            return false;
        }

        if (existeBloqueioNoCaminho(tab, posOrigem, posDestino, donoPeca)) {
            estado.setMensagem("Movimento inválido: há um bloqueio de 3 peças do adversário no caminho.");
            return false;
        }

        if (posDestino >= tamTab && posOrigem >= 0) {
            tab.limparPosicao(posOrigem);
            pecaOrigem.moverPara(tamTab);
            estado.setMensagem("Peça saiu do tabuleiro!");
            estado.notificarMudanca();
            return true;
        }

        int ocupante = tab.donoNaPosicao(posDestino);
        if (ocupante == donoPeca) {
            estado.setMensagem("Movimento inválido: casa ocupada por sua peça.");
            return false;
        }

        if (ocupante != -1 && pecaEstaProtegida(tab, posDestino)) {
            estado.setMensagem("Captura inválida: peça protegida.");
            return false;
        }

        // Se não tem ninguém na posição destino pode mover sem problema
        if (ocupante == -1) {
            if (posOrigem >= 0) tab.limparPosicao(posOrigem);
            tab.colocarPeca(posDestino, donoPeca);
            pecaOrigem.moverPara(posDestino);

            aplicarEfeitoPosMovimento_Agua(tab, pecaOrigem);

            estado.setMensagem("Peça movida para " + pecaOrigem.getPosicao() + ".");
            estado.notificarMudanca();
            return true;

        } else {
            Jogador adversario = estado.getOutroJogador();
            Peca pecaAdv = null;
            for (Peca px : adversario.getPecas()) {
                if (px.getPosicao() == posDestino) {
                    pecaAdv = px;
                    break; // achou a peça do adversário nessa casa
                }
            }

            tab.limparPosicao(posOrigem);
            tab.colocarPeca(posDestino, donoPeca);
            pecaOrigem.moverPara(posDestino);

            tab.colocarPeca(posOrigem, pecaAdv.getDono());
            pecaAdv.moverPara(posOrigem);

            aplicarEfeitoPosMovimento_Agua(tab, pecaOrigem);

            estado.setMensagem("Peça do adversário capturada (trocou de lugar).");
            estado.notificarMudanca();
            return true;
        }
    }

    private boolean pecaEstaProtegida(Tabuleiro tab, int idx) {
        if (casaProtegidaPorRegra(idx))
            return true;

        int dono = tab.donoNaPosicao(idx);
        if (dono < 0)
            return false;

        int[] casas = tab.getCasas();
        int n = tab.getTamanho();

        // Valida se a peça da esquerda ou da direita é do adversario, assim forma uma dupla que não pode ser capturada
        boolean vizinhoEsq = (idx - 1 >= 0) && casas[idx - 1] == dono;
        boolean vizinhoDir = (idx + 1 < n) && casas[idx + 1] == dono;

        return vizinhoEsq || vizinhoDir;
    }

    //Essa função valida se existe uma sequência de três peças no caminho
    private boolean existeBloqueioNoCaminho(Tabuleiro tab, int posOrigem, int posDestino, int donoQueMove) {
        // Só inicia a contar o "andar" da peça na casa seguinte da posição de origem, por isso o "origem + 1"
        int posInicioContagem = (posOrigem >= 0) ? posOrigem + 1 : 0;
        int posFimContagem = posDestino;

        int[] casas = tab.getCasas();
        int tamTab = tab.getTamanho();
        // Se for a vez do jogador 1, então o adversário é 0
        // Logo 1 - 1 (donoQueMove) = 0
        // Se for o jogador 0 então 1 - 0 = 1
        int adversario = 1 - donoQueMove;

        // Por validar se existe 3 peças no caminho, é feito iIdx + 2 < tamTab
        // Porque se a posição for 28 (index 27) é impossível ter uma trinca na frente da peça
        for (int iIdx = 0; iIdx + 2 < tamTab; iIdx++) {
            // Só valida se a peça das 3 posições seguintes são do adversário
            if (casas[iIdx] == adversario && casas[iIdx + 1] == adversario && casas[iIdx + 2] == adversario) {
                boolean comecaAntes = (posOrigem < iIdx);
                boolean terminaDepois = (posDestino > iIdx + 2);
                boolean blocoEntre = (posInicioContagem <= iIdx) && (posFimContagem > iIdx + 2);
                if (comecaAntes && terminaDepois && blocoEntre)
                    return true;
            }
        }
        return false;
    }

    // As casas 26 e 29 não podemo ser capturadas (index 25 e 28 respectivamente)
    private boolean casaProtegidaPorRegra(int idx) {
        return idx == 25 || idx == 28;
    }

    // Conforme regra, todas as casas após a 25 (index 24) deve ser o valor exato
    // A não ser a casa 27 (index 26) que retorna a peça que cair nela para a casa 15 (index 14)
    private Integer valorExatoParaSair(int origemIdx) {
        switch (origemIdx) {
            case 25: return 5;
            case 27: return 3;
            case 28: return 2;
            case 29: return 1;
            default: return null;
        }
    }

    // Se a peça se movimento para a casa 27 (index 26) deve voltar para a casa 15 (index 14)
    // ou na primeira casa livre anterior a casa 15
    private void aplicarEfeitoPosMovimento_Agua(Tabuleiro tab, Peca p) {
        if (p.getPosicao() == 26) {
            int posDestino = 14;
            int posPossivel = posDestino;

            while (posPossivel >= 0 && !tab.posicaoLivre(posPossivel))
                posPossivel--;

            tab.limparPosicao(26);

            tab.colocarPeca(posPossivel, p.getDono());
            p.moverPara(posPossivel);
            estado.setMensagem("Caiu na Casa da Água: peça retorna para " + posPossivel + ".");
        }
    }

    // Percorre todas as posições do tabuleiro
    private boolean haMovimentoPossivel(int valorDado) {
        Tabuleiro tab = estado.getTabuleiro();
        int jogadorAtual = estado.getJogadorAtualId();

        int n = tab.getTamanho();
        int[] casas = tab.getCasas();

        for (int origem = 0; origem < n; origem++) {
            if (casas[origem] != jogadorAtual)
                continue;

            int destino = origem + valorDado;

            if (movimentoValidoSemEfeito(tab, origem, destino, jogadorAtual, valorDado)) {
                return true;
            }
        }
        return false;
    }

    private boolean movimentoValidoSemEfeito(Tabuleiro tab, int posOrigem, int posDestino, int dono, int valor) {
        int tam = tab.getTamanho();

        Integer exigido = valorExatoObrigatorio(posOrigem);
        if (exigido != null && valor != exigido) return false;

        if (existeBloqueioNoCaminho(tab, posOrigem, posDestino, dono)) return false;

        if (posDestino >= tam) {
            if (posOrigem < 0) return false;
            Integer exato = valorExatoParaSair(posOrigem);
            if (exato != null && exato != valor) return false;
            return true;
        }

        if (posDestino < 0) return false;

        int ocupante = tab.donoNaPosicao(posDestino);
        if (ocupante == dono) return false;

        if (ocupante != -1 && pecaEstaProtegida(tab, posDestino)) return false;

        return true;
    }

    private Integer valorExatoObrigatorio(int origemIdx) {
        switch (origemIdx) {
            case 25: return 5;
            case 27: return 3;
            case 28: return 2;
            case 29: return 1;
            default: return null;
        }
    }
}
