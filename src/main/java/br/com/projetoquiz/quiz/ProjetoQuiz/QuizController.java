package br.com.projetoquiz.quiz.ProjetoQuiz;

import br.com.projetoquiz.quiz.ProjetoQuiz.dao.ScoreDAO;
import br.com.projetoquiz.quiz.ProjetoQuiz.model.Question;
import br.com.projetoquiz.quiz.ProjetoQuiz.model.Score;
import br.com.projetoquiz.quiz.ProjetoQuiz.model.User;
import br.com.projetoquiz.quiz.ProjetoQuiz.model.Question.Difficulty;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink; // Importação adicionada
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

public class QuizController {

    private static final int TIME_PER_QUESTION = 5;
    private static final String DEFAULT_BUTTON_STYLE = "-fx-background-color: #4a90e2; -fx-background-radius: 20; -fx-padding: 10 20; -fx-font-size: 16px; -fx-text-fill: white;";
    private static final String CORRECT_BUTTON_STYLE = "-fx-background-color: #2ecc71; -fx-background-radius: 20; -fx-padding: 10 20; -fx-font-size: 16px; -fx-text-fill: white;";
    private static final String WRONG_BUTTON_STYLE = "-fx-background-color: #e74c3c; -fx-background-radius: 20; -fx-padding: 10 20; -fx-font-size: 16px; -fx-text-fill: white;";

    private User currentUser;
    private List<Question> allQuestions;
    private List<Question> availableQuestions;
    private Question currentQuestion;
    private int score;
    private int consecutiveCorrectAnswers;
    private Difficulty currentDifficulty;
    private int questionsAnswered;

    private Timeline timeline;
    private IntegerProperty timeLeft;
    
    private List<Button> optionButtons;

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
    @FXML private ProgressBar timeProgressBar;
    @FXML private Label difficultyStatusLabel;
    @FXML private Hyperlink logoutLink; // Declaração do novo link

    @FXML
    public void initialize() {
        this.allQuestions = new ArrayList<>();
        optionButtons = Arrays.asList(optionButton1, optionButton2, optionButton3, optionButton4);
        loadQuestions();
        startQuiz();
    }

    public void initData(User user) {
        this.currentUser = user;
    }

    private void loadQuestions() {
        allQuestions.clear();
        allQuestions.add(new Question("Em 'Bob Esponja', qual é o nome do melhor amigo dele?", Arrays.asList("Lula Molusco", "Patrick Estrela", "Seu Sirigueijo", "Sandy Bochechas"), 1, Difficulty.FACIL, 100));
        allQuestions.add(new Question("Em 'Os Padrinhos Mágicos', qual a cor do cabelo do Cosmo?", Arrays.asList("Rosa", "Roxo", "Verde", "Azul"), 2, Difficulty.FACIL, 100));
        allQuestions.add(new Question("Qual animal o Pernalonga é?", Arrays.asList("Pato", "Lobo", "Coelho", "Coiote"), 2, Difficulty.FACIL, 100));
        allQuestions.add(new Question("Em 'O Rei Leão', qual é o nome do pai de Simba?", Arrays.asList("Scar", "Mufasa", "Timão", "Pumba"), 1, Difficulty.FACIL, 100));
        allQuestions.add(new Question("Em 'As Meninas Superpoderosas', qual ingrediente secreto foi adicionado para criá-las?", Arrays.asList("Açúcar", "Tempero", "Tudo que há de bom", "Elemento X"), 3, Difficulty.MEDIO, 200));
        allQuestions.add(new Question("Qual é o nome do dono do Woody e do Buzz em 'Toy Story'?", Arrays.asList("Sid", "Andy", "Molly", "Dave"), 1, Difficulty.MEDIO, 200));
        allQuestions.add(new Question("Em 'Procurando Nemo', qual o endereço que Dory não consegue esquecer?", Arrays.asList("P. Sherman, 42 Wallaby Way, Sydney", "Rua dos Bobos, número 0", "Avenida Atlântida, 123", "Recife de Corais, 10"), 0, Difficulty.MEDIO, 200));
        allQuestions.add(new Question("Em 'Shrek', qual criatura de contos de fadas se torna o melhor amigo dele?", Arrays.asList("Gato de Botas", "Biscoito", "Pinóquio", "Burro"), 3, Difficulty.MEDIO, 200));
        allQuestions.add(new Question("Qual o nome do laboratório do rival de Dexter em 'O Laboratório de Dexter'?", Arrays.asList("ACME Labs", "Stark Industries", "Mandark's Lab", "Globex Corporation"), 2, Difficulty.DIFICIL, 300));
        allQuestions.add(new Question("Em 'A Vaca e o Frango', qual é o nome do personagem vermelho sem calças?", Arrays.asList("Bum de Fora", "Frango", "Vaca", "O Demônio"), 0, Difficulty.DIFICIL, 300));
        allQuestions.add(new Question("No desenho 'Coragem, o Cão Covarde', qual o nome da cidade onde eles moram?", Arrays.asList("Lugar Nenhum", "Qualquer Lugar", "Cidade Grande", "Fazenda Distante"), 0, Difficulty.DIFICIL, 300));
        allQuestions.add(new Question("Em 'Avatar: A Lenda de Aang', qual o nome da primeira Dobradora de Água?", Arrays.asList("Katara", "Korra", "Yue", "Hama"), 2, Difficulty.DIFICIL, 300));
    }

    private void startQuiz() {
        score = 0;
        consecutiveCorrectAnswers = 0;
        currentDifficulty = Difficulty.FACIL;
        availableQuestions = new ArrayList<>(allQuestions);
        questionsAnswered = 0;
        displayNextQuestion();
        quizPane.setVisible(true);
        quizPane.setManaged(true);
        endGamePane.setVisible(false);
        endGamePane.setManaged(false);
    }

    private void displayNextQuestion() {
        if (timeline != null) {
            timeline.stop();
        }
        if (questionsAnswered >= allQuestions.size()) {
            showFinalResults();
            return;
        }
        resetButtonsStyle();
        currentQuestion = getNextQuestion();
        if (currentQuestion == null) {
            showFinalResults();
            return;
        }
        setupTimer();
        timeline.play();
        questionLabel.setText(currentQuestion.getQuestionText());
        optionButtons.get(0).setText(currentQuestion.getOptions().get(0));
        optionButtons.get(1).setText(currentQuestion.getOptions().get(1));
        optionButtons.get(2).setText(currentQuestion.getOptions().get(2));
        optionButtons.get(3).setText(currentQuestion.getOptions().get(3));
        questionCounterLabel.setText("Questão: " + (questionsAnswered + 1) + " / " + allQuestions.size());
        scoreLabel.setText("Pontuação: " + score);
    }

    @FXML
    private void handleOptionClick(ActionEvent event) {
        timeline.stop();
        setButtonsDisabled(true);
        Button clickedButton = (Button) event.getSource();
        int selectedOptionIndex = optionButtons.indexOf(clickedButton);
        int correctOptionIndex = currentQuestion.getCorrectAnswerIndex();
        optionButtons.get(correctOptionIndex).setStyle(CORRECT_BUTTON_STYLE);
        if (selectedOptionIndex == correctOptionIndex) {
            score += currentQuestion.getPoints();
            playSound("correct.mp3");
            consecutiveCorrectAnswers++;
            if (consecutiveCorrectAnswers >= 2 && currentDifficulty != Difficulty.DIFICIL) {
                currentDifficulty = (currentDifficulty == Difficulty.FACIL) ? Difficulty.MEDIO : Difficulty.DIFICIL;
                consecutiveCorrectAnswers = 0;
                if (currentDifficulty == Difficulty.MEDIO) {
                    showDifficultyToast("Dificuldade aumentada para: MÉDIO", "#f39c12");
                } else {
                    showDifficultyToast("Dificuldade aumentada para: DIFÍCIL", "#e74c3c");
                }
            }
        } else {
            clickedButton.setStyle(WRONG_BUTTON_STYLE);
            playSound("wrong.mp3");
            consecutiveCorrectAnswers = 0;
            if (currentDifficulty != Difficulty.FACIL) {
                currentDifficulty = (currentDifficulty == Difficulty.DIFICIL) ? Difficulty.MEDIO : Difficulty.FACIL;
                if (currentDifficulty == Difficulty.MEDIO) {
                    showDifficultyToast("Dificuldade reduzida para: MÉDIO", "#f39c12");
                } else {
                    showDifficultyToast("Dificuldade reduzida para: FÁCIL", "#2ecc71");
                }
            }
        }
        questionsAnswered++;
        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished(e -> displayNextQuestion());
        pause.play();
    }
    
    private Question getNextQuestion() {
        if (availableQuestions.isEmpty()) {
            return null;
        }
        List<Difficulty> searchOrder = new ArrayList<>();
        if (currentDifficulty == Difficulty.FACIL) {
            searchOrder.addAll(Arrays.asList(Difficulty.FACIL, Difficulty.MEDIO, Difficulty.DIFICIL));
        } else if (currentDifficulty == Difficulty.MEDIO) {
            searchOrder.addAll(Arrays.asList(Difficulty.MEDIO, Difficulty.FACIL, Difficulty.DIFICIL));
        } else {
            searchOrder.addAll(Arrays.asList(Difficulty.DIFICIL, Difficulty.MEDIO, Difficulty.FACIL));
        }
        for (Difficulty difficultyToTry : searchOrder) {
            List<Question> questionPool = availableQuestions.stream()
                    .filter(q -> q.getDifficulty() == difficultyToTry)
                    .collect(Collectors.toList());
            if (!questionPool.isEmpty()) {
                Collections.shuffle(questionPool);
                Question nextQuestion = questionPool.get(0);
                availableQuestions.remove(nextQuestion);
                return nextQuestion;
            }
        }
        return null;
    }

    private void setupTimer() {
        timeLeft = new SimpleIntegerProperty(TIME_PER_QUESTION);
        timeProgressBar.progressProperty().bind(timeLeft.divide(TIME_PER_QUESTION * 1.0));
        timeline = new Timeline();
        timeline.setCycleCount(TIME_PER_QUESTION + 1);
        timeline.getKeyFrames().add(
            new KeyFrame(Duration.seconds(1), e -> {
                timeLeft.set(timeLeft.get() - 1);
            })
        );
        timeline.setOnFinished(event -> {
            playSound("wrong.mp3");
            questionsAnswered++;
            displayNextQuestion();
        });
    }

    private void showFinalResults() {
        if (timeline != null) {
            timeline.stop();
        }
        if (currentUser != null) {
            ScoreDAO scoreDAO = new ScoreDAO();
            Score finalScore = new Score(currentUser.getUsername(), score);
            scoreDAO.saveOrUpdateRecord(finalScore);
        }
        quizPane.setVisible(false);
        quizPane.setManaged(false);
        finalScoreLabel.setText("Você obteve um total de " + score + " pontos nessa jogada, que tal tentar novamente?");
        endGamePane.setVisible(true);
        endGamePane.setManaged(true);
    }
    
    private void showDifficultyToast(String message, String color) {
        difficultyStatusLabel.setText(message);
        difficultyStatusLabel.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 20;");
        difficultyStatusLabel.setVisible(true);
        difficultyStatusLabel.setManaged(true);
        difficultyStatusLabel.setOpacity(1.0);
        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), difficultyStatusLabel);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> {
            difficultyStatusLabel.setVisible(false);
            difficultyStatusLabel.setManaged(false);
        });
        pause.setOnFinished(e -> fadeOut.play());
        pause.play();
    }

    private void setButtonsDisabled(boolean disabled) {
        for (Button button : optionButtons) {
            button.setDisable(disabled);
        }
    }

    private void resetButtonsStyle() {
        for (Button button : optionButtons) {
            button.setStyle(DEFAULT_BUTTON_STYLE);
            button.setDisable(false);
        }
    }

    @FXML
    private void handlePlayAgainAction() {
        startQuiz();
    }

    @FXML
    private void handleRankingAction() {
        try {
            Stage currentStage = (Stage) rankingButton.getScene().getWindow();
            Parent root = App.loadFXML("RankingView");
            root.setOpacity(0);
            Scene scene = new Scene(root, 1280, 720);
            currentStage.setScene(scene);
            currentStage.setTitle("Ranking dos Campeões");
            FadeTransition ft = new FadeTransition(Duration.millis(500), root);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela de ranking.");
            e.printStackTrace();
        }
    }
    
    // ===== NOVO MÉTODO PARA O LOGOUT ADICIONADO AQUI =====
    @FXML
    private void handleLogoutAction() {
        try {
            // Importante: Para o timer do quiz se ele estiver rodando
            if (timeline != null) {
                timeline.stop();
            }

            Stage currentStage = (Stage) logoutLink.getScene().getWindow();

            Parent root = App.loadFXML("primary");
            root.setOpacity(0);

            Scene newScene = new Scene(root, 1280, 720);
            currentStage.setScene(newScene);
            currentStage.setTitle("Quiz Animado - Versão Alpha 1.3");

            FadeTransition ft = new FadeTransition(Duration.millis(500), root);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playSound(String soundName) {
        try {
            String soundFile = "src/main/resources/sounds/" + soundName;
            Media sound = new Media(new File(soundFile).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        } catch (Exception e) {
            System.err.println("Erro ao tocar o som: " + soundName);
        }
    }
}