# 🎮 Quiz Animado - Desenhos Animados (2000)

Um **quiz interativo** desenvolvido em **Java** com **JavaFX** sobre **desenhos animados dos anos 2000**.  
O projeto conta com **sistema de login e cadastro seguro**, **dificuldade adaptativa**, **pontuação e ranking**, e diversas funcionalidades pensadas para **usabilidade e experiência do usuário (UX)**.

---

## 📖 Sobre o projeto
Este quiz apresenta **12 perguntas aleatórias** sobre desenhos animados dos anos 2000, cada vez que o programa é iniciado.  
A dificuldade é **adaptativa**: ao acertar questões, aumenta para médio ou difícil; ao errar, pode cair para fácil ou médio.  
O sistema de pontuação e ranking registra o desempenho dos jogadores, com destaque visual para o **top 3**:
- 🥇 Top 1 → Amarelo  
- 🥈 Top 2 → Prata  
- 🥉 Top 3 → Marrom  

O quiz possui **barra de tempo de 15 segundos por pergunta**, efeitos sonoros para respostas corretas e incorretas, e mudanças de cor nos botões (verde para certo, vermelho para errado).  

A interface é **minimalista**, pensada para uma experiência agradável e intuitiva (UX), e o quiz abre em uma janela de **1280x720** diretamente no computador, sem depender do terminal.

---

## 🚀 Tecnologias utilizadas
- ☕ **Java**  
- 🎨 **JavaFX**  
- 🗄️ **SQLite** (`quiz.db`)  
- 🔒 **BCrypt** para encriptação de senhas  
- 🔧 **Maven**  

---

## 📂 Estrutura do projeto
- `src/main/java/br/com/projetoquiz/quiz/ProjetoQuiz/App.java` → classe principal para executar o programa  
- `src/main` → código-fonte principal  
- `bin` → arquivos compilados  
- `pom.xml` → configuração do Maven  
- `.gitignore` → arquivos ignorados pelo Git  

---

## 📌 Funcionalidades

### Sistema de login e cadastro
- Cadastro completo com validações:  
  - Nome de usuário sem caracteres especiais  
  - Senha com mais de 5 caracteres  
  - Email válido (com `@`)  
- Verificação em **dois fatores (PIN)**  
- Sistema de logout  
- Recuperação de senha com código de verificação  
- Armazenamento seguro de dados em **SQLite** com senhas encriptadas via **BCrypt**  
- Não permite criar usuários já existentes  

### Quiz
- 12 perguntas aleatórias sobre desenhos animados dos anos 2000  
- **Dificuldade adaptativa**: fácil, médio ou difícil  
- Barra de tempo de 15 segundos por questão  
- Sons para respostas corretas e incorretas  
- Feedback visual nos botões (verde/certo, vermelho/errado)  
- Pontuação e ranking com destaque para o top 3  

---
