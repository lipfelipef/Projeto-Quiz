package br.com.projetoquiz.quiz.ProjetoQuiz;

import br.com.projetoquiz.quiz.ProjetoQuiz.model.Question;
import br.com.projetoquiz.quiz.ProjetoQuiz.model.User;
import br.com.projetoquiz.quiz.ProjetoQuiz.model.Question.Difficulty;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class QuizController {

    private User currentUser;
    private List<Question> questions;
    private int currentQuestionIndex;
    private int score;

    // --- COMPONENTES DO FXML ---
    @FXML private Label questionCounterLabel;
    @FXML private Label scoreLabel;
    @FXML private Label questionLabel;
    @FXML private Button optionButton1;
    @FXML private Button optionButton2;
    @FXML private Button optionButton3;
    @FXML private Button optionButton4;
    @FXML private VBox quizPane;
    @FXML private VBox endGamePane;
    @FXML private Label finalScoreLabel;
    @FXML private Button playAgainButton;
    @FXML private Button rankingButton;

    // Método que é chamado automaticamente quando o FXML é carregado
    @FXML
    public void initialize() {
        this.questions = new ArrayList<>();
        loadQuestions();
        startQuiz();
    }

    // Método para receber o usuário da tela anterior
    public void initData(User user) {
        this.currentUser = user;
    }

    private void loadQuestions() {
        questions.clear();

        // Perguntas Fáceis
        questions.add(new Question("Em 'Bob Esponja', qual é o nome do melhor amigo dele?",
                Arrays.asList("Lula Molusco", "Patrick Estrela", "Seu Sirigueijo", "Sandy Bochechas"),
                1, Difficulty.FACIL, 20));
        questions.add(new Question("Em 'Os Padrinhos Mágicos', qual a cor do cabelo do Cosmo?",
                Arrays.asList("Rosa", "Roxo", "Verde", "Azul"),
                2, Difficulty.FACIL, 20));
        questions.add(new Question("Qual animal o Pernalonga é?",
                Arrays.asList("Pato", "Lobo", "Coelho", "Coiote"),
                2, Difficulty.FACIL, 20));
        questions.add(new Question("Em 'O Rei Leão', qual é o nome do pai de Simba?",
                Arrays.asList("Scar", "Mufasa", "Timão", "Pumba"),
                1, Difficulty.FACIL, 20));

        // Perguntas Médias
        questions.add(new Question("Em 'As Meninas Superpoderosas', qual ingrediente secreto foi adicionado para criá-las?",
                Arrays.asList("Açúcar", "Tempero", "Tudo que há de bom", "Elemento X"),
                3, Difficulty.MEDIO, 50));
        questions.add(new Question("Qual é o nome do dono do Woody e do Buzz em 'Toy Story'?",
                Arrays.asList("Sid", "Andy", "Molly", "Dave"),
                1, Difficulty.MEDIO, 50));
        questions.add(new Question("Em 'Procurando Nemo', qual o endereço que Dory não consegue esquecer?",
                Arrays.asList("P. Sherman, 42 Wallaby Way, Sydney", "Rua dos Bobos, número 0", "Avenida Atlântida, 123", "Recife de Corais, 10"),
                0, Difficulty.MEDIO, 50));
        questions.add(new Question("Em 'Shrek', qual criatura de contos de fadas se torna o melhor amigo dele?",
                Arrays.asList("Gato de Botas", "Biscoito", "Pinóquio", "Burro"),
                3, Difficulty.MEDIO, 50));

        // Perguntas Difíceis
        questions.add(new Question("Qual o nome do laboratório do rival de Dexter em 'O Laboratório de Dexter'?",
                Arrays.asList("ACME Labs", "Stark Industries", "Mandark's Lab", "Globex Corporation"),
                2, Difficulty.DIFICIL, 100));
        questions.add(new Question("Em 'A Vaca e o Frango', qual é o nome do personagem vermelho sem calças?",
                Arrays.asList("Bum de Fora", "Frango", "Vaca", "O Demônio"),
                0, Difficulty.DIFICIL, 100));
        questions.add(new Question("No desenho 'Coragem, o Cão Covarde', qual o nome da cidade onde eles moram?",
                Arrays.asList("Lugar Nenhum", "Qualquer Lugar", "Cidade Grande", "Fazenda Distante"),
                0, Difficulty.DIFICIL, 100));
        questions.add(new Question("Em 'Avatar: A Lenda de Aang', qual o nome da primeira Dobradora de Água?",
                Arrays.asList("Katara", "Korra", "Yue", "Hama"),
                2, Difficulty.DIFICIL, 100));
    }

    private void startQuiz() {
        currentQuestionIndex = 0;
        score = 0;
        Collections.shuffle(questions);
        displayQuestion();
        
        quizPane.setVisible(true);
        quizPane.setManaged(true);
        endGamePane.setVisible(false);
        endGamePane.setManaged(false);
    }

    private void displayQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            questionLabel.setText(currentQuestion.getQuestionText());
            optionButton1.setText(currentQuestion.getOptions().get(0));
            optionButton2.setText(currentQuestion.getOptions().get(1));
            optionButton3.setText(currentQuestion.getOptions().get(2));
            optionButton4.setText(currentQuestion.getOptions().get(3));
            questionCounterLabel.setText("Questão: " + (currentQuestionIndex + 1) + " de " + questions.size());
            scoreLabel.setText("Pontuação: " + score);
        } else {
            showFinalResults();
        }
    }

    @FXML
    private void handleOptionClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonId = clickedButton.getId();
        int selectedOptionIndex = Integer.parseInt(buttonId.substring(buttonId.length() - 1)) - 1;

        Question currentQuestion = questions.get(currentQuestionIndex);

        if (selectedOptionIndex == currentQuestion.getCorrectAnswerIndex()) {
            score += currentQuestion.getPoints();
            playSound("correct.mp3"); // Toca som de acerto
        } else {
            playSound("wrong.mp3"); // Toca som de erro
        }

        currentQuestionIndex++;
        displayQuestion();
    }
    
    private void showFinalResults() {
        quizPane.setVisible(false);
        quizPane.setManaged(false);
        finalScoreLabel.setText("Você obteve um total de " + score + " pontos nessa jogada, que tal tentar novamente?");
        endGamePane.setVisible(true);
        endGamePane.setManaged(true);
    }

    @FXML
    private void handlePlayAgainAction() {
        startQuiz();
    }

    @FXML
    private void handleRankingAction() {
        System.out.println("Botão 'Ver Ranking' clicado!");
        // TODO: Adicionar lógica para abrir a tela de Ranking.
    }

    private void playSound(String soundName) {
        try {
            String soundFile = "src/main/resources/sounds/" + soundName;
            Media sound = new Media(new File(soundFile).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        } catch (Exception e) {
            System.err.println("Erro ao tocar o som: " + soundName);
            e.printStackTrace();
        }
    }
}