package br.com.projetoquiz.quiz.ProjetoQuiz;

import java.io.IOException;
import java.util.List;
import br.com.projetoquiz.quiz.ProjetoQuiz.dao.ScoreDAO;
import br.com.projetoquiz.quiz.ProjetoQuiz.model.Score;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RankingController {

    @FXML
    private VBox rankingContainer;

    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        loadRankingData();
    }

    private void loadRankingData() {
        ScoreDAO scoreDAO = new ScoreDAO();
        List<Score> topScores = scoreDAO.getTopScores();

        rankingContainer.getChildren().clear();

        int position = 1;
        for (Score score : topScores) {
            HBox scoreRow = createScoreRow(position, score.getUsername(), score.getScore());
            rankingContainer.getChildren().add(scoreRow);
            position++;
        }
    }

    private HBox createScoreRow(int position, String username, int scoreValue) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setSpacing(20.0);
        row.setPadding(new Insets(15.0));
        row.setMaxWidth(800.0);
        
        String style;
        Color scoreColor;
        
        String medal = "  ";
        if (position == 1) {
            style = "-fx-background-color: linear-gradient(to right, #FFD700, #F0C400); -fx-background-radius: 12;";
            medal = "🥇 ";
            scoreColor = Color.web("#A16D00");
        } else if (position == 2) {
            style = "-fx-background-color: linear-gradient(to right, #C0C0C0, #B0B0B0); -fx-background-radius: 12;";
            medal = "🥈 ";
            scoreColor = Color.web("#5A5A5A");
        } else if (position == 3) {
            style = "-fx-background-color: linear-gradient(to right, #CD7F32, #B8732E); -fx-background-radius: 12;";
            medal = "🥉 ";
            scoreColor = Color.web("#6B3D12");
        } else {
            style = "-fx-background-color: white; -fx-background-radius: 12;";
            medal = " ";
            scoreColor = Color.web("#2c3e50");
        }
        row.setStyle(style);
        row.setEffect(new DropShadow(5, Color.rgb(0, 0, 0, 0.2)));

        Label posLabel = new Label(medal + position + ".");
        posLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        posLabel.setTextFill(Color.web("#2c3e50"));
        posLabel.setMinWidth(60);

        Label userLabel = new Label(username);
        userLabel.setFont(Font.font("System", FontWeight.NORMAL, 20));
        userLabel.setMaxWidth(Double.MAX_VALUE);
        
        // ===== CORREÇÃO ADICIONADA AQUI =====
        userLabel.setTextFill(Color.web("#2c3e50")); // Garante que o texto seja sempre escuro

        Label scoreLabel = new Label(String.valueOf(scoreValue));
        scoreLabel.setFont(Font.font("System", FontWeight.BOLD, 22));
        scoreLabel.setTextFill(scoreColor);

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        row.getChildren().addAll(posLabel, userLabel, spacer, scoreLabel);
        return row;
    }

    @FXML
    private void handleBackButtonAction() {
        try {
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            Parent root = App.loadFXML("primary");
            root.setOpacity(0);
            Scene scene = new Scene(root, 1280, 720);
            currentStage.setScene(scene);
            currentStage.setTitle("Quiz Animado");
            FadeTransition ft = new FadeTransition(Duration.millis(500), root);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}