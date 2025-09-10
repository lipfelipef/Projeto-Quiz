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

            try {

                Stage currentStage = (Stage) pinField.getScene().getWindow();

                FXMLLoader loader = new FXMLLoader(App.class.getResource("/br/com/projetoquiz/quiz/ProjetoQuiz/QuizView.fxml"));
                Parent root = loader.load();

                QuizController quizController = loader.getController();
                quizController.initData(userToVerify);


                root.setOpacity(0);
                

                Scene newScene = new Scene(root, 1280, 720);
                currentStage.setScene(newScene);
                currentStage.setTitle("Quiz Animado");

                FadeTransition ft = new FadeTransition(Duration.millis(500), root);
                ft.setFromValue(0);
                ft.setToValue(1);
                ft.play();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            statusLabel.setText("O PIN inserido está incorreto, tente novamente.");
        }
    }
}