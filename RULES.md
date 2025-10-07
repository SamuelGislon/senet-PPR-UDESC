# 🎲 Senet – Regras da Partida (Variante do Projeto)

Estas regras definem a variante utilizada neste projeto. Elas foram organizadas para facilitar a implementação e a leitura por quem joga e por quem mantém o código.

---

## 1) Tabuleiro e Peças

- **Tabuleiro**: 30 casas numeradas de **1 a 30**, dispostas em **3 fileiras de 10**.
- **Numeração (movimento em “S”)**:
    - Linha 1 (topo): **1 → 10** (esquerda → direita)
    - Linha 2 (meio): **20 ← 11** (direita → esquerda)
    - Linha 3 (base): **21 → 30** (esquerda → direita)
- **Jogadores**: 2 jogadores, **5 peças** para cada um.


---

## 2) Objetivo

Vence quem **remover todas as suas peças** do tabuleiro primeiro.

---

## 3) Preparação (Setup)

- **Posição inicial**: distribua as peças **alternadas** nas casas **1 a 10** (primeira fileira), começando pelo jogador inicial.  
  Ex.: 1=A, 2=B, 3=A, 4=B, …, 9=A, 10=B.
- O **jogador inicial** sempre será o Jogador A, que colocou a primeira peça.


---

## 4) Dados e Turnos

- Usa-se um **D5** (valores possíveis: **1, 2, 3, 4, 5**).
- **Sequência de turno**:
    1. Jogador rola o D5.
    2. Escolhe **uma** de suas peças para mover **exatamente** o número de casas.
    3. **Movimentos especiais** e **capturas** podem ocorrer (ver abaixo).
    4. **Nova jogada** se o resultado foi **1** ou **5**.
    5. Se **nenhuma** peça puder se mover com o resultado atual, o jogador **perde a vez**.

> Não é permitido **dividir** o resultado entre peças. O movimento sempre é com **uma única peça**.

---

## 5) Movimento, Passagem e Bloqueios

- **Movimento básico**: avance a peça **exatamente** X casas à frente no traçado “S”.
- **Passagem**:
    - Você **pode ultrapassar** peças (suas ou do oponente) **exceto** quando houver **bloqueio** (abaixo).
- **Bloqueio (muro)**:
    - **Três ou mais** peças do **mesmo jogador em sequência** formam um **bloqueio**.
    - **Bloqueios não podem ser ultrapassados pelo adversário**.
    - Não é permitido **aterrar** em nenhuma casa ocupada pelo bloqueio adversário.

> Duas peças **adjacentes** **não** formam bloqueio; veja “Proteção”.

---

## 6) Capturas e Proteções

- **Captura por troca**:
    - Se sua peça **aterrissar** numa casa com **1 peça adversária**, você **troca de lugar** com ela (sua peça ocupa a casa; a do oponente vai para a casa de origem do seu movimento).
- **Proteção (dupla)**:
    - **Duas peças adjacentes** do **mesmo jogador** ficam **protegidas**: **não podem ser capturadas**, mas **podem ser ultrapassadas**.
- **Bloqueio (3+)**:
    - Além de impedir a **passagem**, **nenhuma** das casas do bloqueio pode ser **alvo de captura**.

---

## 7) Regras Especiais de Casas (26–30)

> **Regra geral de saída**: As casas 26, 28, 29 e 30 exigem **movimento exato** para sair do tabuleiro (atingir “31”).  
> **Exatidão** significa: só sai se o resultado do dado **encaixa exatamente** na contagem necessária.

- **Casa 26 – “Casa da Beleza”**
    - **Protegida**: a peça nessa casa **não pode ser capturada**.
    - **Saída**: só pode sair com **movimento de 5**.

- **Casa 27 – “Casa da Água”**
    - Ao **cair** na 27, a peça **volta** para a **Casa 15** (“da Ressurreição”).
    - Se a **Casa 15** estiver ocupada, a peça vai para a **primeira casa vazia** antes **da casa 15**.
    - O movimento **termina** após o retorno.

- **Casa 28 – “Casa dos Três Juízes”**
    - Exige **movimento exato** para **sair** (ex.: precisa de **3** para ir a **31**).

- **Casa 29 – “Casa de Rá”**
  - **Protegida**: a peça nessa casa **não pode ser capturada**. 
  - Exige **movimento exato** para **sair** (ex.: precisa de **2** para ir a **31**).

- **Casa 30 – “Casa de Hórus”**
    - Exige **movimento exato** para **sair** (ex.: precisa de **1** para ir a **31**).

---

## 8) Restrições de Movimento

- Você **não pode**:
    - Entrar em uma casa **ocupada** por **duas peças aliadas** (proteção) ou por **bloqueio** (3+).
    - Ocupar uma casa onde possui uma **peça do adversário adjacente** a outra dele (proteção).
    - **Ultrapassar** um **bloqueio**.
    - **Sair** das casas 26, 28, 29, 30 **sem exatidão** (ver seção 7).
- Se **todas** as suas peças ficarem sem movimento legal com o resultado atual, você **perde a vez**.

---

## 9) Condições de Vitória

- **Vitória**: o primeiro jogador a **remover todas as 5 peças**.

---

## 10) Exemplos Rápidos

- **Extra turn**: resultados **1** e **5** concedem **nova jogada** após mover.
- **Captura**: sua peça em 8 rola **3** e cai em **11** ocupado por **1** inimiga → **troca de lugar** (sua peça fica em 11; a do oponente vai para 8).
- **Proteção**: inimigo tem peças em **12** e **13** → você **não captura** nenhuma delas se **aterrar** em uma delas (aterrissagem proibida). **Ultrapassar** é permitido **se não houver bloqueio** adiante.
- **Bloqueio**: inimigo em **12–13–14** → você **não pode ultrapassar** essa sequência. Qualquer trajeto que exija passar por **12–14** é **ilegal**.

---

# Boa partida! 🏁
