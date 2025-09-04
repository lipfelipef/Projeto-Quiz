package br.com.projetoquiz.quiz.ProjetoQuiz;

import java.io.IOException;
import br.com.projetoquiz.quiz.ProjetoQuiz.model.User;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SecurityCheckController {

    @FXML
    private PasswordField pinField;

    @FXML
    private Label statusLabel;

    private User userToVerify;

    public void initData(User user) {
        this.userToVerify = user;
    }

    @FXML
    private void handleConfirmAction() {
        String enteredPin = pinField.getText();

        if (userToVerify != null && userToVerify.getPin().equals(enteredPin)) {
            // PIN CORRETO! Vamos para o quiz.
            try {
                // 1. Pega a janela atual (a do PIN)
                Stage currentStage = (Stage) pinField.getScene().getWindow();

                FXMLLoader loader = new FXMLLoader(App.class.getResource("/br/com/projetoquiz/quiz/ProjetoQuiz/QuizView.fxml"));
                Parent root = loader.load();

                QuizController quizController = loader.getController();
                quizController.initData(userToVerify);

                // Animação de Fade-in
                root.setOpacity(0);
                
                // 2. Cria a nova cena e a define na JANELA ATUAL
                Scene newScene = new Scene(root, 1280, 720);
                currentStage.setScene(newScene);
                currentStage.setTitle("Quiz de Desenhos Animados");

                FadeTransition ft = new FadeTransition(Duration.millis(500), root);
                ft.setFromValue(0);
                ft.setToValue(1);
                ft.play();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // PIN INCORRETO!
            statusLabel.setText("PIN incorreto. Tente novamente.");
        }
    }
}