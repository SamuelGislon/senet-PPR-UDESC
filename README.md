# SENET — JavaFX (JDK 17) • MVC + Observer

Jogo **Senet** implementado em **Java 17** com **JavaFX**, seguindo os padrões **MVC** e **Observer**.
Interface com tema egípcio, regras clássicas (bloqueios, capturas, casas especiais) e telas de **Menu**, **Jogo** e **Vitória**.

---

## Requisitos

- **JDK 17** (ex.: Temurin, Microsoft, Oracle).  
  Verifique com:
  ```bash
  java -version
  ```
- **(opcional)** Maven instalado. O projeto inclui **Maven Wrapper**, então você pode usar `./mvnw` / `mvnw.cmd` sem instalar Maven.

> O JavaFX é resolvido pelo **Maven** (OpenJFX 17.x); não é necessário instalar SDK JavaFX manualmente.

---

## Como executar

### 1) IntelliJ IDEA (recomendado)

1. **Open** o projeto (a pasta que contém o `pom.xml`).
2. Espere o **Maven** sincronizar. Se necessário: _Maven tool window_ → **Reload All Maven Projects**.
3. Garanta que o **Project SDK** é **17** (_File → Project Structure → Project_).
4. Rode a classe principal:
    - `br.com.senet.Aplicacao` (Run ▶).
5. A janela **SENET** abrirá no **menu principal**.

> A aplicação está configurada para **não permitir redimensionamento** (menu, jogo, vitória).

---
## Estrutura relevante do projeto

```
src/
 └─ main/
    ├─ java/
    │   └─ br/com/senet/
    │       ├─ Aplicacao.java         # Main (abre o menu)
    │       ├─ controller/            # Controladores (Menu, Jogo)
    │       ├─ model/                 # Modelo (Tabuleiro, EstadoJogo, etc.)
    │       └─ view/                  # Visões (VisaoMenu, VisaoJogo, VisaoVitoria)
    └─ resources/
        ├─ styles/                    # CSS (menu.css, jogo.css, vitoria.css)
        └─ fonts/                     # Fontes (ex.: CinzelDecorative-*.ttf)
```

> Recursos (CSS, imagens, fontes) ficam em **`src/main/resources`** e são carregados via classpath (ex.: `/styles/menu.css`).

---

## Controles e regras principais

- **Dado**: 1 a 5.
    - **1** e **5** dão direito a **jogar novamente** (após movimento válido).
- **Proteção**: duas peças adjacentes do mesmo jogador não podem ser capturadas.
- **Bloqueio**: sequências de **3+** peças do adversário **bloqueiam a passagem**.
- **Casas especiais** (casas 26..30):
    - **26**: só sai com **5** (protegida).
    - **27**: **Água** — volta para a casa **15**.
    - **28**: só sai com **3**.
    - **29**: só sai com **2** (protegida).
    - **30**: só sai com **1**.
- **Fim de jogo**: vence quem remover **todas as peças** primeiro (tela de vitória).

---

## Dicas e problemas comuns

- **Classes JavaFX “unnamed module” (WARNING)**: é um aviso benigno ao executar pelo Maven; pode ser ignorado.
- Se o IntelliJ mostrar erros de `javafx.*`:
    - Recarregue o Maven (**Reload All Maven Projects**).
    - Confira o SDK 17 e a Language level (Project Structure).
- Se o `exec:java` não encontrar a classe principal, garanta que é **`br.com.senet.Aplicacao`** e que o Maven sincronizou.

---

## Licença

Este projeto é licenciado sob a **MIT License** — veja o arquivo [LICENSE](./LICENSE) para detalhes.

---

## Créditos

Projeto acadêmico SENET (UDESC) — Engenharia de Software. UI temática egípcia, JavaFX + MVC + Observer.
